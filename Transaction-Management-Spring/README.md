https://docs.spring.io/spring/docs/current/spring-framework-reference/data-access.html#transaction

# Transaction Management in Spring

# 1. Transaction management

## 1.1 Advantages of the Spring Framework’s Transaction Support Model
Thông thường, Java EE developer có 2 lựa chọn cho transaction management: 
- global transactions
- local transactions

Mỗi loại có những hạn chế riêng.

### 1.1.1 Global transaction
Global transactions cho phép làm việc với multiple transaction resource, thường là CSDL quan hệ và message queues. Application server quản lí các global transaction thông qua JTA (Java Transaction API), nó là một API cồng kềnh (một phần do exception model của nó). Hơn nữa 1 JTA `UserTransaction` thông thường cần có nguồn gốc từ JNDI (Java Naming Directory Interface), nghĩa là bạn cũng cần sử dụng JNDI để sử dụng JTA. Việc sử dụng global transaction hạn chế việc tái sử dụng code application, vì JTA thường chỉ available trong môi trường application server

### 1.1.2 Local transaction
Local transactions là resource-specific, như là 1 transaction liên kết với 1 JDBC connection. Local transaction dễ dùng hơn nhưng cũng có 1 số hạn chế. Bạn không thể làm việc trên multiple transaction resources. Vd như: code để quản lí transaction bằng cách dùng JDBC connection không thể run trong 1 JTA transaction. Vì application server không liên quan đến transaction management, nó không thể đảm bảo tính đúng đắn giữa multiple resource (Chẳng vấn đề gì vì hầu hết các application đều dùng single transaction resource). Và một hạn chế nữa là local transaction ảnh hưởng đến progamming model

### 1.1.3 Spring Framework’s Consistent Programming Model
Spring giải quyết những hạn chế của global và local transaction, cho phép các developer dùng 1 mô hình consistent programming trong bất kì môi trường nào. Spring framework cung cấp cả declarative và programatic transaction management, thường dùng declarative nhiều hơn

- **Programmatic transaction management**: nghĩa là bạn phải quản lí transaction bằng cách lập trình, nó sẽ linh hoạt hơn nhưng khó để maintain
- **Declarative transaction management**: nghĩa là bạn phân chia việc quản lí transaction từ business code. Bản chỉ cần dùng annotation hoặc XML-based configuration để quản lí các transaction

Declarative được ưa dùng hơn programmatic mặc dù nó ít linh hoạt hơn. Nhưng declarative có để được mô dun hóa với phương pháp AOP. Spring hỗ trợ declarative thông qua Spring AOP framework

link: https://www.tutorialspoint.com/spring/spring_transaction_management.htm

Với programmatic transaction management, developer làm việc với Spring Framework transaction abstraction, nó có thể run với bất kì cơ sở hạ tầng transaction cơ bản này. Với sự ưa thích dùng declarative model, dev thường code ít hoặc không cần code để quản lí transaction, vì thế không phù thuộc vào Spring Framework transaction API hay bất kì transaction API nào khác


## 1.2 Spring Framework Transaction Abstraction
Transaction strategy được defined bởi `org.springframework.transaction.PlatformTransactionManager` interface, như sau:

```
public interface PlatformTransactionManager {

    TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException;

    void commit(TransactionStatus status) throws TransactionException;

    void rollback(TransactionStatus status) throws TransactionException;
}
```

Đây chủ yếu là một service provider interface (SPI). Vì `PlatformTransactionManager` là 1 interface, nên nó có thể được mock hoặc stub dễ dàng. Nó không giống với lookup strategy như JNDI, `PlatformTransactionManager` implement được định nghĩa như bất kì object hay bean nào trong Spring framework IOC container. Điều này làm cho Spring framework transactions trở thành 1 abstraction giá trị, kể cả khi làm việc với JTA. Bạn có thể test transaction code dễ dàng hơn dùng trực tiếp JTA

Để giữ được nguyên lí của Spring, `TransactionException` có thể được thrown bởi bất kì `PlatformTransactionManager` interface method nào bị unchecked(nó là extends `java.lang.RuntimeException` class). Kiến trúc transaction fail hầu hết là fatal. 

`getTransaction(..)` method return 1 `TransactionStatus` object, phù thược vào `TransactionDefinition` parameter. `TransactionStatus` trả về có thể là 1 transaction mới hoặc 1 transaction đã tồn tại, nếu nó matching transaction đang tồn tại trong call stack hiện tại. 

`TransactionDefinition` interface specifies:
- Propagation: Thông thường, mọi code exectuted bên trong 1 transaction scope run trong chính transaction đó. Tuy nhiên, bạn có thể chỉnh định hành vi nếu transaction method được executed khi 1 transaction context khác đã tồn tại. Vd: code có thể tiếp tục run trong 1 transaction đã tồn tại (trường hợp phổ biến), hoặc transaction đã tồn tại đó bị gián đoạn và 1 transaction mới được tạo ra. 
  - REQUIRED: hỗ trợ 1 transaction hiện tại, tạo 1 cái mới nếu nó không tồn tại
  - REQUIRES_NEW: tạo 1 transaction mới và dừng transaction hiện tại nếu nó không tồn tại
  - MANDATORY: hỗ trợ 1 transaction hiện tại, throw exeception nếu nó không tồn tại
  - NESTED: executes bên trong 1 nested transaction nếu tồn tại transaction hiện tại
  - SUPPORTS: hỗ trợ transaction hiện tại nhưng executed non-transactionally nếu nó không tồn tại
- Isolation: là transaction isolation level, transaction isolated với các transaction khác
  - DEFAULT — default isolation level of the datasource
  - READ_COMMITTED — indicates dirty reads to be prevented, non-repeatable, and phantom reads can occur.
  - READ_UNCOMMITTED — indicates that dirty reads, non-repeatable, and phantom reads can occur
  - REPEATABLE_READ — indicates dirty and non-repeatable reads are prevented but phantom reads can occur    
  - SERIALIZABLE — indicates dirty read phantom read, and non-repeatable reads are prevented

- readOnly: transaction là read-only hay read/write
- timeout: transaction timeout
- rollbackFor: mảng các exeception class object mà gây ra rollback trong transaction
- rollbackForClassName: mảng các exeception class names mà gây ra rollback trong transaction
- noRollbackFor: mảng các exeception class object mà không gây ra rollback trong transaction
- noRollbackForClassName: mảng các exeception class names mà không gây ra rollback trong transaction

link: https://dzone.com/articles/bountyspring-transactional-management

## 1.3 Synchronizing Resources with Transactions

### 1.3.1 High-level Synchronization Approach
Cách hay dùng là dùng highest-level template của Spring dựa trên persistence integration APIs hoặc dùng native ORM API với transaction-aware factory bean hoặc proxy để quản lý các native resource factory. Những transaction-aware solution này handle nội bộ tạo resource, reuse, cleanup, optional transaction synchronization của resource, exeception mapping. Do vậy, user data access code không cần phải giải quyết các task này nhưng có thể tập trung hoàn toàn vào non-boilerplate persistence logic. Nói chung, bạn dùng native ORM API hoặc dùng JdbcTemplate cho JDBC access. 

### 1.3.2 Low-level Synchronization Approach
Những class như `DataSourceUtils` (cho JDBC), `EntityManagerFactoryUtils` (cho JPA), `SessionFactoryUtils` cho(Hibernate),... tồn tại ở lower level. Khi bạn muốn application code làm việc trực tiếp với các loại resource của native persistence API, bạn dungf những class này để đảm bảo proper Spring Framework-managed instance thu được, transaction được synchronized, exception xảy ra khi process được map với 1 consistent API

Ví dụ: với JDBC, thay vì cách tiếp cận thông thường của JDBC là gọi `getConnection()` trên `DataSource`, có thể dùng `org.springframework.jdbc.datasource.DataSourceUtils` class để thay thế như sau:

```
Connection conn = DataSourceUtils.getConnection(dataSource);
```

Nếu transaction đang tồn tại có 1 connection synchronized(linked) tới nó, instance đó sẽ được return. Nếu không thì, method sẽ gọi trigger tạo 1 connection mới, được (tùy chọn) synchronized với bất kì transaction nào hiện tồn tại và sẵn sàng cho việc tái sử dụng tiếp theo trong cùng một transaction. 

### 1.3.3 TransactionAwareDataSourceProxy
`TransactionAwareDataSourceProxy` class tồn tại ở level thấp nhất, là 1 proxy cho `DataSource` đích, nó wrap `DataSource` đích để thêm nhận thức về các transaction mà Spring quản lý. Với góc nhìn này, nó gần giống với 1 transactional JNDI `DataSource` cung cấp bởi Java EE server

## 1.4  Declarative transaction management
Spring framework's declarative transaction management giống với EJB CMT, điểm khác nhau giữa 2 loại transaction management này là:


| EJB CMT                                                                                               | Declarative transaction management                                                                                                                    |
|-------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|
| gán liền với JTA                                                                                      | có thể làm việc trên bất kì môi trường nào như:  JTA transaction, local transaction dùng JDBC, JPA, Hibernate bằng cách điều chỉnh configuration file |
| chỉ áp dụng cho một số class đặc biệt                                                                 | áp dụng cho bất kì class nào                                                                                                                          |
| không có rollback rules                                                                               | cả programmatic và declarative đều hỗ trợ rollback rules                                                                                              |
| không thể thay đổi container's transaction management, ngoại trừ với 'setRollbackOnly()`              | Spring framework cho phép customize transaction bằng cách dùng AOP                                                                                    |
| hỗ trợ propagation của transaction context thông qua remote call, như high-end application server làm | không hỗ trợ propagation                                                                                                                              |

### AOP concept 
Aspect Oriented Programming (AOP) – lập trình hướng khía cạnh: là một kỹ thuật lập trình (kiểu như lập trình hướng đối tượng) nhằm phân tách chương trình thành cách moudule riêng rẽ, phân biệt, không phụ thuộc nhau.

Khi hoạt động, chương trình sẽ kết hợp các module lại để thực hiện các chức năng nhưng khi sửa đổi 1 chức năng thì chỉ cần sửa 1 module.

AOP không phải dùng để thay thế OOP mà để bổ sung cho OOP.

![](https://viblo.asia/uploads/1abc0e2f-0989-4cf7-8183-b413d66ffe35.jpg)

![](https://www.baeldung.com/wp-content/uploads/2017/11/Program_Execution-768x461.jpg)

- Business object: 1 class thông thường chứa business logic và không có bất kì Spring-related annotation
  ```
    public class SampleAdder {
    public int add(int a, int b) {
        return a + b;
    }
    }
  ```

- Core concern/primary concern: là requirement, logic xử lý chính của chương trình
- Cross-cutting concern: những logic xử lí phụ cần thực hiện của chương trình khi core concern được gọi như logging, security, ...

```
@Aspect
public class EmployeeAspectPointcut {

	@Before("getNamePointcut()")
	public void loggingAdvice(){
//		System.out.println("Executing loggingAdvice on getName()");
	}

	@Before("getNamePointcut()")
	public void secondAdvice(){
//		System.out.println("Executing secondAdvice on getName()");
	}

	@Pointcut("execution(public String getName())")
	public void getNamePointcut(){}

	@Before("allMethodsPointcut()")
	public void allServiceMethodsAdvice(){
//		System.out.println("Before executing service method");
	}

	//Pointcut to execute on all the methods of classes in a package
	@Pointcut("within(com.journaldev.spring.service.*)")
	public void allMethodsPointcut(){}

}
```

- Aspect: tương tự như 1 java class, một Aspect đóng gói toàn bộ cross-cutting concern và có thể chứa các JointPoint, PointCut, Advice.
- Advice: những xử lý phụ (crosscutting concern) được thêm vào xử lý chính (core concern), code để thực hiện các xử lý đó được gọi Advice. Advice được chia thành các loại sau:
    - Before: được thực hiện trước join point.
    - After: được thực hiện sau join point.
    - Around: được thực hiện trước và sau join point.
    - After returning : được thực hiện sau join point hoàn thành một cách bình thường.
    - After throwing : được thực hiện sau khi join point được kết thúc bằng một Exception.
- Joinpoint: là một điểm trong chương trình, là những nơi có thể được chèn những cross-cutting concern. Chẳng hạn chúng ta cần ghi log lại sau khi chạy method nào đó thì điểm ngay sau method đó được thực thi gọi là một Jointpoint. Một Jointpoint có thể là một phương thức được gọi, một ngoại lệ được throw ra, hay một field được thay đổi.
- Pointcut: có nhiều cách để xác định Joinpoint, những cách như thế được gọi là Pointcut. Nó là các biểu thức được sử dụng để kiểm tra nó có khớp với các Jointpoint để xác định xem Advice có cần được thực hiện hay không.
- Introduction: định nghĩa thêm các method hay field như 1 type, tạo interface mới cho bất kì advised object
- Target object: object được advised bởi 1 hay nhiều aspect
- AOP proxy: object được tạo bởi AOP framwork để implement aspect (advice method execution). Trong Spring framework, AOP proxy là JDK dynamic proxy hoặc CGLIB proxy
- Waeving: liên kết aspect với các application type hay object khác để tạo advised object

link: https://gpcoder.com/5112-gioi-thieu-aspect-oriented-programming-aop/
https://www.journaldev.com/2583/spring-aop-example-tutorial-aspect-advice-pointcut-joinpoint-annotations


### Spring AOP Capabilities and Goals
Spring AOP hiện tại chỉ support method execution join point. 
Mục tiêu Spring AOP là cung cấp 1 sự kết nối chặt chẽ giữa AOP implementation với Spring IOC để giải quyết các vấn đề trong application lớn

### AOP proxies


### 1.4.1 Spring Framework’s Declarative Transaction Implementation
Spring Framework's declarative transaction hỗ trợ enabled AOP proxies và các transaction advice được điều khiển bởi metadata(XML hoặc annotation-based). Sự kết hợp của AOP với transactional metadata tạo ra 1 AOP proxy dùng `TransactionInterceptor` kết hợp với 1 `PlatformTransactionManager` implementation thích hợp để vận hành transaction quanh các method invocation

![](https://docs.spring.io/spring/docs/current/spring-framework-reference/images/tx.png)

### 1.4.2 Declarative Transaction Implementation

### 1.4.3 Rolling Back a Declarative Transaction
Default configuration, Spring Framework's transaction infrastructure code đánh dấu 1 transaction rollback chỉ trong trường hơp runtime, unchecked exception. Đó là khi thrown exception là 1 instance hay subclass của `RuntimeException` (tương tự với `Error` instance). Checked exception được throw từ 1 transaction method không gây ra rollback trong default configuration

- rollback-for
```
<tx:advice id="txAdvice" transaction-manager="txManager">
    <tx:attributes>
    <tx:method name="get*" read-only="true" rollback-for="NoProductInStockException"/>
    <tx:method name="*"/>
    </tx:attributes>
</tx:advice>
```

- no-rollback-for
```
<tx:advice id="txAdvice">
    <tx:attributes>
    <tx:method name="updateStock" no-rollback-for="InstrumentNotFoundException"/>
    <tx:method name="*"/>
    </tx:attributes>
</tx:advice>
```

### 1.4.4 Configuring Different Transactional Semantics for Different Beans

### 1.4.5 <tx:advice/> Settings
Default <tx:advice/> Settings:
- propagation setting: `REQUIRED`
- isolation level: `DEFAULT`
- transaction: read-write
- transaction timeout default là hết default timeout của hệ thống transaction cơ bản hoặc không có nếu timeout không được supported
- Bất kì `RuntimeException` trigger rollback, và bất kì checked `Exception` không thực hiện

### 1.4.6 Dùng @Transactional

```
// the service class that we want to make transactional
@Transactional
public class DefaultFooService implements FooService {

    Foo getFoo(String fooName) {
        // ...
    }

    Foo getFoo(String fooName, String barName) {
        // ...
    }

    void insertFoo(Foo foo) {
        // ...
    }

    void updateFoo(Foo foo) {
        // ...
    }
}
```

Mỗi method có thể có @Transactional annotation riêng, nhưng class-level annotation sẽ không apply cho các class cha của nó, nhưng method cần phải redeclared locally để tham gia vào subclass-level annotation

#### Method visibility and @Transactional
Khi dùng proxies, chỉ có thể apply @Transactional annotation trên các method public. Nếu dùng annotation này trên các method protected, private hay cùng package thì mặc dù không có lỗi được raise nhưng method được annotated sẽ không thực hiện được configured transactional settings. 

Bạn có thể apply @Transactional annotation trên interface, 1 method trong interface, 1 class hay 1 public method trong class. Tùy nhiên chỉ có @Transaction annotation thì không đủ để activate transactional behavior. @Transactional annotation chỉ đơn thuần là 1 metadata có thể được dùng bởi 1 số runtime infrastructure như `@Transactional-aware` hoặc dùng metadata để configure một số bean thích hợp với transactional behavior. 

Spring recommend dùng @Transactional annotation chỉ trên các concrete classe hoặc các method của concrete class, chứ không dùng với interface. Vì Java annotation không được kế thừa từ interface, nếu bạn dùng class-based proxies (proxy-target-class="true") hoặc weaving-base aspect (mode="aspectj"), transaction setting sẽ không được tìm thấy bởi proxy và weaving infrastructure, và object sẽ không được bọc bởi transactional proxy

Trong proxy mode(default), chỉ những method bên ngoài call xuyên qua proxy mới được intercepted. Nghĩa là seft-invocation (method trong target object gọi method khác trong chính object đó) thì sẽ không có transaction lúc runtime kể cả khi method được gọi được mark @Transactional. Vì vậy, proxy phải được khởi tạo đầy đủ để cung cấp expected behavior, vậy nên không cần @PostContruct trong code khởi tạo

- `proxy-target-class` attribute control loại transactional proxies nào được tạo cho class với @Transactional annotation.
  - true: class-based proxies được tạo
  - false hoặc không có: standard JDK interface-based proxies được tạo
- `@EnableTransactionManagement` và `<tx:annotation-driven/>` tìm kiếm `@Transactional` chỉ trên bean được defined trong application context hiện tại. 
  
#### @Transactional Settings
Default `@Transactional` Settings:
- propagation setting: `PROPAGATION_REQUIRED`
- isolation level: `ISOLATION_DEFAULT`
- transaction là read-write
- transaction timeout default là default timeout của hệ thống transaction cơ bản hoặc không có nếu timeout không được support
- bất kì `RuntimeException` đều rollback và bất kì checked `Exception` thì không

Có thể thay đổi default setting theo bảng như sau:

| Property               | Type                                                               | Description                                                                                     |
|------------------------|--------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| value                  | String                                                             | Optional qualifier that specifies the transaction manager to be used.                           |
| propagation            | enum: Propagation                                                  | Optional propagation setting.                                                                   |
| isolation              | enum: Isolation                                                    | Optional isolation level. Applies only to propagation values of REQUIRED or REQUIRES_NEW.       |
| timeout                | int (in seconds of granularity)                                    | Optional transaction timeout. Applies only to propagation values of REQUIRED or REQUIRES_NEW.   |
| readOnly               | boolean                                                            | Read-write versus read-only transaction. Only applicable to values of REQUIRED or REQUIRES_NEW. |
| rollbackFor            | Array of Class objects, which must be derived from Throwable.      | Optional array of exception classes that must cause rollback.                                   |
| rollbackForClassName   | Array of class names. The classes must be derived from Throwable.  | Optional array of names of exception classes that must cause rollback.                          |
| noRollbackFor          | Array of Class objects, which must be derived from Throwable.      | Optional array of exception classes that must not cause rollback.                               |
| noRollbackForClassName | Array of String class names, which must be derived from Throwable. | Optional array of names of exception classes that must not cause rollback.                      |


### 1.4.7 Transaction Propagation
- physical transaction: database level
- logical transaction: application level

#### `PROPAGATION_REQUIRED`
![](https://docs.spring.io/spring/docs/current/spring-framework-reference/images/tx_prop_required.png)

`PROPAGATION_REQUIRED` thực thi cả physical transaction, hoặc locally cho current scope nếu không có transaction nào tồn tại hoặc tham gia vào 1 transaction đang tồn tại bên ngoài cho một large scope. 

`PROPAGATION_REQUIRED`, 1 logical transaction được tạo cho mỗi method mà setting được applied. Với mỗi logical transaction scope như vậy có thể tự xác định rollback-only status, với 1 outer transaction scope thì độc lập với inner transaction scope. Với trường hợp standard `PROPAGATION_REQUIRED` behavior, mọi scope của nó được map tới cùng 1 physical transaction. rollback-only marker set trên inner transaction scope sẽ ảnh hưởng tới đến sự commit của outer transaction

Tuy nhiên, trường hợp inner transaction scope set rollback-only marker, outer transaction không tự quyết định rollback chính nó được, nên rollback (kích hoạt ngầm bởi inner transaction scope) không lường trước được. `UnexpectedRollbackException` tương ứng được thrown ra ở đây, đây là expected behavior để người gọi transaction không bị nhầm khi một commit được thực hiện nhưng thực sự là không. Vì vậy, nếu inner transaction (mà outer caller không biết) ngầm đánh dấu 1 transaction là rollback-only, outer caller vẫn gọi commit. Outer caller cần nhận được `UnexpectedRollbackException` để biết rõ 1 rollback đã được thực hiện thay thế

### `PROPAGATION_REQUIRES_NEW`
![](https://docs.spring.io/spring/docs/current/spring-framework-reference/images/tx_prop_requires_new.png)

`PROPAGATION_REQUIRES_NEW`, trái ngược với `PROPAGATION_REQUIRED`, luôn dùng 1 physical transaction độc lập cho mỗi affected transaction scope, không tham gia vào 1 transaction hiện có cho outer scope. Vì sự sắp xếp như vậy, resource transaction cơ bản là khác nhau nên commit hay rollback là độc lập, outer transaction không bị ảnh hưởng bởi inner transaction rollback và inner transation sẽ trả lock ngay khi nó hoàn thành. Như thế các inner transaction độc lập có thể tự định nghĩa isolation level, timeout, read-only setting và không cần kế thừa outer transaction's characteristics

### `PROPAGATION_NESTED`
`PROPAGATION_NESTED` dùng single physical transaction với multiple savepoint để nó rollback. Inner transaction scoper kích hoạt rollback cho scope của nó, outer transaction vẫn có thể tiếp tục physical transaction kể cả khi 1 số operator bị roll back. Setting này thường map với JDBC savepoint, nên nó chỉ làm việc được với JDBC resource transaction




### How does @Transactional work then?
Persistence context proxy implemnt `EntityManager` không phải là component duy nhất cần để declarative transaction management work. Thật ra cần 3 component sau:
- The EntityManager Proxy itself
- The Transactional Aspect
- The Transaction Manager

#### The Transactional Aspect
Transactional Aspect: arount aspect được gọi cả ở before và after business method. Concrete class để implement aspect là `TransactionInterceptor`
Transactional aspect có 2 nhiệm vụ chính:
- Ở before: aspect cung cấp 1 hook join để xác định business method được gọi sẽ chạy trên scope của db transaction đang tồn tại hay trên một transaction mới
- Ở after: aspect cần xác định transaction nên được commit, rollback hay ngưng chạy

Ở before, Transactional aspect itself không chứa bất kì decision logic nào, decision để start 1 transaction mới sẽ được ủy thác cho Transaction Manager

#### The Transaction Manager
Transaction manager cần được quyêt định lúc before của Transactional aspect logic được gọi. Transaction manager quyết định dựa trên:
- Thật sự 1 transaction có đang được chạy hay không
- propagration attribute của transactional method

Nếu transaction manager quyết định tạo 1 transaction mới, thì như sau:
- Taọ 1 entity manager mơi
- bind entity manager với thread hiện tại
- lấy 1 connection từ DB connection pool
- bind connection đó với thread hiện tại

Entity manager và connection được lưu trong thread khi transaction running, cho tới khi Transaction Manager clean nó

Bất kì phần nào trong program cần tới entity manager hiện tại hoặc connection có thể lấy được từ thread. Component program thực hiện chính xác việc đó là EntityManager proxy.

#### The EntityManager proxy
Khi business method gọi như sau:

```
entityManager.persist()
```
lời gọi này sẽ không gọi trực tiếp tới entity manager, nó sẽ gọi tới proxy để lấy được entity manager hiện tại trong thread, nơi mà Transaction Manager bỏ vào

link: https://dzone.com/articles/how-does-spring-transactional


# 2. DAO support
Spring hỗ trợ DAO (data access object) để dễ dàng làm việc với các data access technologies (như JDBC, Hibernate, JPA). 

DAO: abstraction của data persistence, dùng database term. Có method update, repository thì không
Repository: abstraction của 1 collection các object, liên quan đến Domain model term, chỉ dùng trong Aggregate Root. Repository có thể được implement bằng DAO nhưng ngược lại thì không


## 2.1 Consistency Exception hierachy
![](https://docs.spring.io/spring/docs/current/spring-framework-reference/images/DataAccessException.png)

Spring cung cấp Exception hierachy với `DataAccessException` nằm ở root. Các exception này wrap original exception vậy nên sẽ không thể bị mất dữ liệu khi có gì đó chạy sai. Spring wrap JDBC exception, JPA, hibernate exception thành set các runtime exception. Cho phép handle hầu hết các non-recoverable persistence exception chỉ ở trong layers thích hợp, mà không cần catch throw block và khai báo exception trong DÁO, có thể catch và handle exception ở bất kì đâu 


## 2.2 Annotations Used to Configure DAO or Repository Classes
`@Repository` annotation cho phép component scan hỗ trợ tìm kiếm và configure DÁO và repository mà không cần cung cấp XML configuration các entry cho nó.



