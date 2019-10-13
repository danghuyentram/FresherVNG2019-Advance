# Spring

## 1. Core Technologies
### 1.1 IoC: Inversion of Control
- Dependency Inversion Principle (DIP): một nguyên lý để thiết kế và viết code 
  
- Inversion of Control(IoC): một design pattern được tạo ra để code tuân thủ nguyên lý Dependency Inversion. Kiến trúc phần mềm được áp dụng thiết kế này sẽ đảo ngược quyền điều khiển so với kiểu lập trình hướng thủ tục. Trong lập trình hướng thủ tục, các đoạn mã được thêm vào sẽ gọi các thư viện nhưng với IoC thì những IoC container sẽ inject các dependencies vào khi nó khởi tạo bean.
  
- Dependency Injection (DI): là 1 pattern để implement IoC. Các dependencies sẽ được inject vào module khi khởi tạo. 
  
### 1.2 IoC container: 
Container sau khi inject các dependency khi nó tạo bean. 

Spring Framework's Ioc container cơ bản:
- org.springframework.beans package
- org.springframework.context

BeanFactory interface cung cấp cơ chế advanced configurtion để quản lý bất kì loại object nào

ApplicationContext là sub-interface của BeanFactory. Nó thêm vào:
- Dễ dàng tích hợp với Sping's AOP feature
- Message resource handling
- Event publication
- Application-layer specific contexts: như WebApplicationContext dùng cho web application.

### 1.3 ApplicationContext

org.springframework.context.ApplicationContext interface đại diện cho Spring IoC container và chịu trách nhiệm instantiating, configuring, asembling bean. Container lấy instructions của nó từ objects để instantiate, configure, assemble bằng cách đọc configuration metadata. 

Spring cung cấp nhiều cách để implement ApplicationContext interface:
- Stand-alone application: nó thường dùng để tạo instance của ClassPathXmlApplicationContext hoặc FileSystemXmlApplicationContext. 

![](https://docs.spring.io/spring/docs/current/spring-framework-reference/images/container-magic.png)

Diagram trên là high-level view về cách làm việc của Spring. Các application class sẽ được combined với configuration metadata, vậy nên sau khi ApplicationContext được create và initialized, ta sẽ có được đầy đủ configured và executable system hoặc application.

#### Configuration Metadata
Configuration metadata giúp developer báo cho Spring container để instantiate, configure và asemble các object trong application.

3 cách cung cấp configuration metadata cho Spring container thông qua:
- XML configuration file
- Annottion-based configuration
- Java-based configuration


### 1.4 Bean
Spring bean là những Java Object mà từ đó tạo nên khung sườn của 1 Spring application. 

Spring IoC container quản lí 1 hoặc nhiều bean. Những bean này được tạo với configuration metadata mà bạn cung cấp cho container đó.

Trong mỗi container, những bean definitions này được biểu diễn như 1 BeanDefinition object, nó bao gồm metadata sau:
- A package-qualified class name: 
- Bean behavioral configuration elements: là state mà bean sẽ behave trong container (scope, lifecycle callbacks, and so forth)
- References tới các bean khác là cấn thiết để bean đó thực hiện được công việc của nó. Những reference này được gọi là collaborator hay dependency
- Những configuration setting để set vào các object mới được tạo: vd như size limit của pool hay số connection dùng trong bean để quản lý connection pool.

### 1.5 Bean scopes
- singleton
- prototype
- request
- session
- application
- websocket

#### 1.5.1 Singleton scopes
Default scope của Spring.

![](https://docs.spring.io/spring/docs/current/spring-framework-reference/images/singleton.png)

Với singleton scope thì Spring container sẽ tạo 1 single instant cho bean sau đó lưu vào cache, tất cả cá request tới bean đó sẽ trả về duy nhất instant này. 

Ví dụ: tạo bean với scope là singleton bằng cách dùng @Scope annotation

```
public class Student {
    private String name;
    private int age;
    @Bean
    @Scope("singleton")
    public Student studentSingleton(){
        return new Student();
    }
}
```

có thể sử dụng constant thay vì sử dụng String value

```
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
```

hoặc dùng XML configuration

```
<bean id="studentSingleton" class="org.khoa.nguyen.dang.beans.Student" scope="singleton" />
```

#### 1.5.2 Prototype scopes
![](https://docs.spring.io/spring/docs/current/spring-framework-reference/images/prototype.png)

1 bean được định nghĩa với scope là prototype thì Spring container sẽ trả về các instant khác nhau cho mỗi request.

1 nguyên tắc là bạn nên sử dụng prototype scope cho tất cả stateful bean và singleton scope cho các stateless bean.

Ví dụ: dùng annotation @Scope để định nghĩa 1 bean có scope là prototype:

```
@Bean
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public Student studentPrototype() {
    return new Student();
}
```

hoặc dùng XML

```
<bean id="studentProtorype" class="org.khoa.nguyen.dang.beans.Student" scope="prototype" />

```

Khác với các scope khác, Spring không quản lý hết một lifecycle của prototype bean. Container instantiates, configure, asemble một prototype object và đưa nó đến client, mà không ghi lại thêm protype instance nào nữa. Do vậy, mặc dù khởi tạo lifecycle callback method được gọi trên tất cả các object bất kể scope, thì đối với prototype, configured destruction lifecycle callback sẽ không được gọi. Client phải clean up prototype scoped object và release các expensive resource mà prototype beans giữ. Để Spring container release resource giữ bởi prototype scoped bean, dùng custom bean-post-processor, nó giữ các reference tới những beans cần clean up

#### 1.5.3 Web application scopes
Request, session, application và websocket scope chỉ có khi implement ApplicationContext (vd như: XmlWebApplicationContext)

- Request scope: giống với prototype scope, tuy nhiên nó dùng cho web application, 1 instant của bean sẽ đươc tạo cho mỗi HTTP request

Vd: sử dụng @Scope annotation để định nghia một bean có scope request như sau:

```
public class MessageGenerator {
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public MessageGenerator generateHelloMessage(){
        return new MessageGenerator();
    }
}
```

Tiếp theo, hãy định nghĩa một Controller và inject bean generateHelloMessage vào

```
@Controller
public class MessageController {
    @Resource(name = "generateHelloMessage")
    MessageGenerator generateHelloMessage;
    
    @RequestMapping("/message/morning")
    public String goodMorningMessage(final Model model){
        //This will return null for every request at every time
        model.addAttribute("previousMessage", generateHelloMessage.getMessage());
        generateHelloMessage.setMessage("Good morning!");
        model.addAttribute("currentMessage", generateHelloMessage.getMessage());
        return "Example";
    }
}
```

- Session scope: mỗi instant của bean sẽ được tạo cho mỗi HTTP session

Vd: dùng @Scope để định nghĩa 1 session bean:

```
@Bean
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public MessageGenerator generateSessionMessage(){
    return new MessageGenerator();
}
```

Tiếp theo, Sử dụng lại MessageController và inject bean generateSessionMessage vào

```
@Controller
public class MessageController {
    @Resource(name = "generateSessionMessage")
    MessageGenerator generateSessionMessage;
    @RequestMapping("/message/session")
    public String sessionMessage(final Model model){
        //This will return null for the first time. But, after changed the value is retained for every request
        model.addAttribute("previousMessage", generateSessionMessage.getMessage());
        generateSessionMessage.setMessage("Good morning!");
        model.addAttribute("currentMessage", generateSessionMessage.getMessage());
        return "Example";
    }
}
```


- Application scope: sẽ tạo một bean instance cho lifecycle của một ServletContext. Giống với signleton-scope nhưng application-scope sẽ được shared giữa multipe servlet-based running trên cùng 1 ServletContex trong khi singleton-scope thì scoped cho 1 application context

Vd: tạo 1 bean application-scope như sau:

```
@Bean
@Scope(value = WebApplicationContext.SCOPE_APPLICATION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public MessageGenerator generateApplicationMessage(){
    return new MessageGenerator();
}
```

Và sử dụng generateApplicationMessag ebean trong controller

```
@Controller
public class MessageController {
    @Resource(name = "generateApplicationMessage")
    MessageGenerator generateApplicationMessage;
    @RequestMapping("/message/application")
    public String getApplicationScopeMessage(final Model model) {
        model.addAttribute("previousMessage", generateApplicationMessage.getMessage());
        generateApplicationMessage.setMessage("Good Morning!");
        model.addAttribute("currentMessage", generateApplicationMessage.getMessage());
        return "Example";
    }
}
```

- Websocket scope: Webt-scope bean như sau:
WebSocket-scope bean được khởi tạo và lưu trữ tại WebSocket session attributes. Instance return là giống nhau khi có request từ WebSocket session.

```
@Bean
@Scope(value = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
public MessageGenerator generateSocketMessage(){
    return new MessageGenerator();
}
```

### 1.6 Dependency
1 enterprise application không chỉ bao gồm 1 single object được, kể cả là ứng dụng đơn giản nhất tSocket-scope bean được khởi tạo và lưu trữ tại WebSocket session attributes. Instance return là giống nhau khi có request từ WebSocket session.

Vd: khởi tạo websockehì cũng gồm một số oject làm việc với nhau. Vậy làm sao để define các bean definition đứng độc lập mà vẫn biết được đầy đủ về ứng dụng.

### 1.7 Dependency injection
Nguyên lý cuối cùng trong S.O.L.I.D chính là Dependency Inversion (Đảo ngược sự phụ thuộc)
Nội dung của nguyên lý đó như sau :

1. Các module cấp cao không nên phụ thuộc vào các module cấp thấp. Cả 2 nên phụ thuộc vào abstraction.
2. Interface (abstraction) không nên phụ thuộc vào chi tiết, mà ngược lại, chi tiết nên phụ thuộc vào abstraction. ( Các class giao tiếp với nhau thông qua interface, không phải thông qua implementation)

Dependency injection (DI) là quá trình mà object định nghĩa các dependency của mình chỉ thông qua constructor arguments, arguments của factory method, hoặc properties mà nó sẽ được gán cho object instance sau khi nó được khởi tạo hay return từ 1 factory method. Container sau khi inject các dependency khi nó tạo bean. 

Code sẽ clean hơn với DI principle, và sự tách rời nhau sẽ hiệu quả hơn khi object có nhiều dependency. Object sẽ không cần tìm kiếm hay biết các class của các dependency của nó. Két quả là class sẽ dễ test hơn, hơn nữa nếu dependencies là interface hay abtract base class, nó cho phép dùng stub hay mock implementaion cho unit test.

#### Dependency resolution process
Container perform bean dependency resolution như sau:
- ApplicationContext được created và initialized với configuration metadata mô tả tất cả các bean. 
- Với mỗi bean, các dependency của nó sẽ được xem như 1 form của các property, constructor argument hoặc argument cho static-factory method. Các dependency này được cung cấp cho bean khi bean thật sự được tạo.
- Mối property hay constructor argument là một giá trị được set hoặc reference tới bean khác trong container
- Mỗi property hay constructor argument là giá trị được chuyển từ specified format thành autual type of that property hay constructor argument. Mặc định, Spring có thể convert 1 value dạng string thành tất cả dạng khác như: int, long String, boolean, ...

Spring container validates the configuration cho mỗi bean khi container được tạo. Tuy nhiên, các property của bean sẽ không được set cho đến khi bean đó thực sự được tạo. Bean là singleton-scoped và set là pre-instantiated (default) thì được tạo khi mà container được tạo. Mặt khác bean chỉ được tạo duy nhất khi nó được yêu cầu. Việc tạo 1 bean có khả năng tạo ra 1 đồ thị các bean, mà mỗi dependency của bean và các dependency của các dependency đó được tạo và assign. 

DI gồm 2 biến thế chính: Constructor-based dependency injection và Setter-based dependency injection

#### 1.7.1 Constructor-based Dependency injection and setter-based dependency injection

| Constructor-based DI                                                                                                                                                                 | Setter-based DI                                                                                                                                                                                    |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Contructor-based DI được thực thi bởi container gọi contructor với các argument ứng với mỗi dependency.  Hoặc gọi static factory method với các argument đặc biệt để construct bean  | Setter-based DI được thực hiện bởi container gọi setter method của beans sau khi gọi no-argument constructor hay no-argument static factory method để khởi tạo bean                                |
| Dùng constructor cho những mandatory dependency                                                                                                                                      | dùng setter modethod hoặc configuration method cho các optional dependency. Dùng @Required annotation trên setter method dùng để khiến property trở thành 1 required dependency                    |
| Cho phép implement application components như 1 immutable object và  đảm bảo các required dependency là not null.                                                                    | setter injection nên được dùng chủ yếu cho các optional dependency mà có thể gán reasonable default values trong class. Not-null phải được kiểm tra ở mọi nơi trong code mà dùng tới dependency đó |
| Contructor-injected component luôn trả về cho client code ở trạng thái được khởi tạo đầy đủ                                                                                          | Setter method khiến các object của class đó có thể reconfiguration hay re-injection lại được.                                                                                                      |
| Số lượng lớn các constructor argument sẽ gây ra bad code và nên được refactored lại                                                                                                  |                                                                                                                                                                                                    |

Ví dụ: 
- Constructor-based DI

```
public class SimpleMovieLister {

    // the SimpleMovieLister has a dependency on a MovieFinder
    private MovieFinder movieFinder;

    // a constructor so that the Spring container can inject a MovieFinder
    public SimpleMovieLister(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // business logic that actually uses the injected MovieFinder is omitted...
}
```

- Setter-based DI

```
public class SimpleMovieLister {

    // the SimpleMovieLister has a dependency on the MovieFinder
    private MovieFinder movieFinder;

    // a setter method so that the Spring container can inject a MovieFinder
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // business logic that actually uses the injected MovieFinder is omitted...
}
```

### 1.8 Cicular dependency
Nếu bạn dùng constructor injection chủ yếu, nó có thể xảy ra 1 cicurlar dependency không giải quyết được

Ví dụ: class A yêu cầu 1 instance từ class B thông qua constructor injection, và class B yêu cầu 1 instance từ class A cũng thông qua contructor injection. Nếu bạn configure bean cho class A và B injected vào nhau, Spring IoC container sẽ detect vòng tròn reference này lúc runtime và throws lỗi BeanCurrentlyInCreationException.

1 giải pháp là đổi source code một số class từ constructor sang setter hoặc tránh xài constructor injection và chỉ dùng setter injection. 

### 1.9 Annotation-based Container Configuration
- ```@Required``` annotation  applies to bean property setter methods
  ```
  public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Required
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
    }
    ```

#### 1.9.1 ```@Autowired``` to constructors
    ```
    public class MovieRecommender {

    private final CustomerPreferenceDao customerPreferenceDao;

    @Autowired
    public MovieRecommender(CustomerPreferenceDao customerPreferenceDao) {
        this.customerPreferenceDao = customerPreferenceDao;
    }

    // ...
    }
    ```
- Cũng có thể apply ```@Autowired``` annotation cho traditional setter method:
    ```
  public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Autowired
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
    }
    ```
- Có thể kết hợp ```@Autowired``` cho các field và kết hợp nó với constructor:
    ```
    public class MovieRecommender {

    private final CustomerPreferenceDao customerPreferenceDao;

    @Autowired
    private MovieCatalog movieCatalog;

    @Autowired
    public MovieRecommender(CustomerPreferenceDao customerPreferenceDao) {
        this.customerPreferenceDao = customerPreferenceDao;
    }

    // ...
    }
    ```

- Có thể cung cấp cho tất cả bean 1 type cụ thể từ ```ApplicationContext``` bằng cách thêm annotation cho field hay method:
    ```
  public class MovieRecommender {

    @Autowired
    private MovieCatalog[] movieCatalogs;

    // ...
    }
    ```
    
    hoặc kiểu collection:

    ```
    public class MovieRecommender {

    private Set<MovieCatalog> movieCatalogs;

    @Autowired
    public void setMovieCatalogs(Set<MovieCatalog> movieCatalogs) {
        this.movieCatalogs = movieCatalogs;
    }

    // ...
    }
    ```

    kể cả kiểu Map với key là String:

    ```
    public class MovieRecommender {

    private Map<String, MovieCatalog> movieCatalogs;

    @Autowired
    public void setMovieCatalogs(Map<String, MovieCatalog> movieCatalogs) {
        this.movieCatalogs = movieCatalogs;
    }

    // ...
    }
    ```

Mặc định, autowiring fail nếu ko matching được 1 bean đang tồn tại với injection point. Trong trường hợp khai báo mảng, collection hay map thì ít nhất 1 phần tử sẽ được tìm thấy

Mặc định của annotated method và field là required dependency. Có thể thay đổi nó như sau:

```
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Autowired(required = false)
    public void setMovieFinder(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    // ...
}
```

1 non-required method sẽ không được gọi nếu những dependency của nó không tồn tại. 

- Tùy chỉnh annotation-based Autowiring với ```@Primary```
  
  ```@Primary``` giúp chỉ ra 1 bean cụ thể sẽ được ưu tiên khi có nhiều bean là candidates để autowired cho 1 single-value dependency. Nếu chỉ có duy nhất 1 primary bean trong những candidate thì nó sẽ là autowired value

Ví dụ dưới đây ```firstMovieCatalog``` là primary ```MovieCatalog```

    ```
    @Configuration
    public class MovieConfiguration {

    @Bean
    @Primary
    public MovieCatalog firstMovieCatalog() { ... }

    @Bean
    public MovieCatalog secondMovieCatalog() { ... }

    // ...
    }
    ```

- Tùy chỉnh annotation-base autowiring với Qualifiers
  @Qualifier xác định một Bean mà bạn muốn chỉ định inject dựa vào 1 số tham số cụ thể

  Ví dụ: 
    ```
  public class MovieRecommender {

    @Autowired
    @Qualifier("main")
    private MovieCatalog movieCatalog;

    // ...
    }
    ```

```@Qualifier``` annotation có thể dùng trên từng constructor argument hoặc method parameter:
    
    ```
    public class MovieRecommender {

    private MovieCatalog movieCatalog;

    private CustomerPreferenceDao customerPreferenceDao;

    @Autowired
    public void prepare(@Qualifier("main") MovieCatalog movieCatalog,
            CustomerPreferenceDao customerPreferenceDao) {
        this.movieCatalog = movieCatalog;
        this.customerPreferenceDao = customerPreferenceDao;
    }

    // ...
    }
    ```

bean definition

    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>

    <bean class="example.SimpleMovieCatalog">
        <qualifier value="main"/> 

        <!-- inject any dependencies required by this bean -->
    </bean>

    <bean class="example.SimpleMovieCatalog">
        <qualifier value="action"/> 

        <!-- inject any dependencies required by this bean -->
    </bean>

    <bean id="movieRecommender" class="example.MovieRecommender"/>

    </beans>
    ```

Bean với qualifier value là main sẽ được kết nối với constructor argument mà có qualified cùng value

Có thể define bean với id như 1 qualifier element. 


### 1.10 Classpath Scanning and Managed Components

#### 1.10.1 @Component
Thông thường, chúng ta khai báo tất cả các bean hoặc component trong file XML để Spring container có thể tìm và quản lý các bean.

Thực tế, Spring có khả năng tự động tìm, dò và tạo thể hiện của bean từ các định nghĩa ban đầu ở package, class mà không cần phải khai báo chúng trong file XML

![](https://stackjava.com/wp-content/uploads/2017/12/spring-annotation.png)

- @Component – biểu thị đây là một component được tự động scan.
- @Repository – biểu thị đây là  một DAO component trong tầng persistence.
- @Service – biểu thị đây là một  Service component trong tầng business.
- @Controller – biểu thị đây là một Controller component trong tầng presentation

#### 1.10.2 Using Meta-annotations and Composed Annotations
Rất nhiều annotation Spring cung cấp có thể dùng như 1 meta-annotaiton trong code. 1 meta-annotation có thể applied từ 1 annotation khác. Ví dụ như @Service annotattion là 1 meta-annotation của @Component.

#### 1.10.3 Automatically Detecting Classes and Registering Bean Definitions
Spring có thể tự động detect steoreotyped class và register ```BeanDefinition``` instances đúng với ```ApplicationContext```. 

Ví dụ: 
```
@Service
public class SimpleMovieLister {

    private MovieFinder movieFinder;

    @Autowired
    public SimpleMovieLister(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }
}
```

```
@Repository
public class JpaMovieFinder implements MovieFinder {
    // implementation elided for clarity
}
```

Để autodetect các class và register đúng bean, cần thêm ```@ComponentScan``` cho ```@Configuration``` class, 

```
@Configuration
@ComponentScan(basePackages = "org.example")
public class AppConfig  {
    ...
}
```

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="org.example"/>

</beans>
```

```AutowiredAnnotationBeanPostProcessor``` và ```CommonAnnotationBeanPostProcessor``` đều là mặc định ngầm khi dùng component-scan element. Nghĩa là cả 2 components đều sẽ được autodetec và kết nối với nhau, ngay cả khi không có bean configuration metadata được cung cấp trong XML

#### 1.10.4 Using Filters to Customize Scanning
Bạn có thể tùy chỉnh và thêm vào behavior bằng cách thêm các custom filter. Thêm ```includeFilters``` hay ```excludeFilters``` parameter của @ComponentScan annotation. Mỗi phần tử filter đều yêu cầu: type và expression attributes

Filter types

| Filter Type          | Example Expression         | Description                                                                                |
|----------------------|----------------------------|--------------------------------------------------------------------------------------------|
| annotation (default) | org.example.SomeAnnotation | An annotation to be present at the type level in target components.                        |
| assignable           | org.example.SomeClass      | A class (or interface) that the target components are assignable to (extend or implement). |
| aspectj              | org.example..*Service+     | An AspectJ type expression to be matched by the target components.                         |
| regex                | org\.example\.Default.*    | A regex expression to be matched by the target components class names.                     |
| custom               | org.example.MyTypeFilter   | A custom implementation of the org.springframework.core.type .TypeFilter interface.        |

Ví dụ: configuration bỏ qua tất cả @Repository annotations và dùng "stub" repository thay thế

```
@Configuration
@ComponentScan(basePackages = "org.example",
        includeFilters = @Filter(type = FilterType.REGEX, pattern = ".*Stub.*Repository"),
        excludeFilters = @Filter(Repository.class))
public class AppConfig {
    ...
}
```

```
<beans>
    <context:component-scan base-package="org.example">
        <context:include-filter type="regex"
                expression=".*Stub.*Repository"/>
        <context:exclude-filter type="annotation"
                expression="org.springframework.stereotype.Repository"/>
    </context:component-scan>
</beans>
```

