# Spring Boot

![](https://howtodoinjava.com/wp-content/uploads/2018/08/spring-boot-modules.png)

# Boot

# 1. Boot
## 1.1 Starter template
Spring Boot starter là các template là tập hợp tất cả relevant transitive dependencies cần để start 1 function cụ thể.

Ví dụ: nếu bạn cần tạo 1 Spring WebMVC application bằng cách truyền thống, bạn phải tự included tất cả dependencies cần thiết bằng tay. Điều này dễ khiến bị conflict version và cuối cùng dẫn đến runtime exception

Với Spring boot, tạo MVC application, bạn chỉ cần import spring-boot-starter-web dependency:

pom.xml
```
<!-- Parent pom is mandatory to control versions of child dependencies -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.6.RELEASE</version>
    <relativePath />
</parent>
 
<!-- Spring web brings all required dependencies to build web application. -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Với spring-boot-starter-web dependency, tự động import tất cả dependency và thêm vào project bạn. 

Lưu ý cách mà các dependency được direct tới và nó sẽ refer tới những starter templates khác và tiếp tục download các dependency khác

Không cần cung cấp version cho các dependency con. Tất cả version sẽ được resolve tới version của parent start

vd
```
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-json</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
    </dependency>
    <dependency>
        <groupId>org.hibernate.validator</groupId>
        <artifactId>hibernate-validator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
    </dependency>
</dependencies>
```

**Khác nhau giữa `<dependencies>` và `<dependencyManagement>`**
- Các artifact được chỉ định trong `<dependencies>` section luôn được included như 1 dependency vào chil module
- Các artifact được chỉ định trong `<dependencyManagement>` section, chỉ được include vào child module nếu nó được chỉ định trong `<dependencies>` section của child module




## 1.2 Spring boot autoconfiguration
Autoconfiguration được enable với @EnableAutoConfiguration annotation. Spring boot sẽ auto configuration scan các classpath, tìm các library trong classpath và cố thử 1 cái best configuration cho chúng, cuối cùng là configure tất cả thành các bean

Auto-configuration cố thông minh nhất có thể và sẽ quay lại khi bạn define thêm configuration

Auto-configuration luôn được apply sau khi các bean được user define được registered

Spring boot auto-configuration logic được implement trong spring-boot-autoconfigure.jar, bạn có thể verify list package sau đây:

Spring boot autoconfiguration packages

![](https://howtodoinjava.com/wp-content/uploads/2018/08/Spring-boot-autoconfiguration-packages.png)

Vd: nhìn vào auto-configuration cho Spring AOP, nó sẽ gồm:
- Scan classpath sẽ có nếu  EnableAspectJAutoProxy, Aspect, Advice and AnnotatedElement classes được present
- Nếu các class không present, không autoconfigureation sẽ được làm bởi Spring AOP
- Nếu các class được tìm thấy thì AOP được configured bởi Java config annotation @EnableAspectJAutoProxy
- Nó sẽ check property spring.aop là true or false
- Dựa vào value của property, proxyTargetClass attribute sẽ được set

AopAutoConfiguration.java
```
@Configuration
@ConditionalOnClass({ EnableAspectJAutoProxy.class, Aspect.class, Advice.class,
        AnnotatedElement.class })
@ConditionalOnProperty(prefix = "spring.aop", name = "auto", havingValue = "true", matchIfMissing = true)
public class AopAutoConfiguration
{
 
    @Configuration
    @EnableAspectJAutoProxy(proxyTargetClass = false)
    @ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "false", matchIfMissing = false)
    public static class JdkDynamicAutoProxyConfiguration {
 
    }
 
    @Configuration
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    @ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "true", matchIfMissing = true)
    public static class CglibAutoProxyConfiguration {
 
    }
 
}
```

## 1.3 Embedded server
Tomcat là một web server cũng được phát triển bởi Apache Software Foundation, vì vậy tên chính thức của nó là Apache Tomcat. Nó cũng là một server HTTP, tuy nhiên, nó hỗ trợ mạnh cho ứng dụng Java thay vì website tĩnh. Tomcat có thể chạy nhiều bản Java chuyên biệt như Java Servlet, JavaServer Pages (JSP), Java EL, và WebSocket.

- Tomcat được tạo đặc biệt riêng cho Java apps, mặc dù Apache là vẫn là một server HTTP. Bạn có thể sử dụng Apache với nhiều ngôn ngữ lập trình khác (PHP, Python, Perl, vâng vâng.) với sự giúp đỡ của module Apache phù hợp (mod_php, mod_python, mod_perl, etc.).
- Mặc dù bạn có thể sử dụng Tomcat server để phục vụ trang web tĩnh, nhưng nó không hiệu quả như là khi sử dụng Apache. Ví dụ, Tomcat sẽ tải máy ảo Java lên trước và những thư viện Java liên quan khác, mà website thông thường thì không cần thiết.
- Tomcat cũng khó cấu hình hơn các web server khác. Ví dụ, để chạy WordPress, hãy dùng các server dành cho HTTP như là Apache hoặc NGINX.


Spring boot application luôn bao gồm tomcat như 1 embedded server dependency. Nghĩa là khi bạn có thể  chạy 1 Spring boot application từ command prompt mà không cần 1 server phức tạp

Bạn có thể không dùng tomcat và thêm bắt kì server nào bạn cần hoặc có thể bỏ hoàn toàn server environment. 

Ví dụ: dưới đây bỏ tomcat và thay bằng jetty như embedded server

pom.xml
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
 
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

## 1.4 Bootstrap the application
Để chạy 1 application, ta cần dùng @SpringBootApplication annotation nó tương đương với @Configuration, @EnableAutoConfiguration, and @ComponentScan

Nó ennable scanning config các class, file và load chúng vào spring context. Ví dụ dưới đây, execution bắt đầu với main(). Nó bắt đầu load tất cả config files, configure chúng và bootstrap application dựa trên application properties trong file application.properties trong /resources folder

MyApplication.java
```
@SpringBootApplication
public class MyApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }
}
```

application.properties
```
### Server port #########
server.port=8080
  
### Context root ########
server.contextPath=/home
```

để execute application, bạn có thể chạy main() trong IED hoặc build file jar là excute nó từ comman prompt

console
```
$ java -jar spring-boot-demo.jar
```

## 1.5 Lợi ích của Spring boot
- Spring boot giúp giải quyết dependency conflict. Nó là identify các dependecy cần thiết và import chúng cho bạn
- Nó chứa thông tin về các version tương thích cho tất cả các dependency. Nó giúp giảm vấn đề runtime classloader
- Nó là cấu hình mặc định giúp bạn configure hầu hết các phần quan trọng ở đằng sau. Override chúng chỉ khi bạn cần, không thì mọi thứ vẫn hoạt động bình thường. Nó giúp tránh boilerpate code, annotation và XML configuration
- Nó cung cấp embedded HTTP server Tomcat nên bạn có thể develop và test 1 cách nhanh chóng
- Nó là sự tích hợp hoàn hảo vơi những IDE như eclipse hay intelliJ

# 2 Configuration với Boot
Spring boot cho phép externalize configuration nên bạn có thể làm với cùng 1 code application ở nhiều môi trường khác nhau. Bạn có thể dùng properties file, YAML file, environment variable và command-line argument để externalize configuration. Property value có thể được injected vào bean bằng cách dùng @Value annotaion, truy cập thông qua Spring's Environment abstraction, hoặc là ràng buộc với các structured object thông qua @ConfigurationProperties

Spring boot dùng 1 PropertySource order cụ thể cho phép overriding hợp lý các giá trị. Các property được quan tâm với thứ tự sau:
1. Devtools global settings properties trong home directory (~/.spring-boot-devtools.properties when devtools is active).
2. @TestPropertySource annotations on your tests.
   https://www.concretepage.com/spring-5/testpropertysource-example-spring-test
3. ```properties``` attribute on your tests. Available on @SpringBootTest and the test annotations for testing a particular slice of your application.
4. Command line arguments.
5. Properties từ  SPRING_APPLICATION_JSON (trong JSON embedded ở environment variable hay system property)
6. ```ServletConfig``` init parameters
7. ```ServletContext``` init parameters
8. JNDI attributes từ java:comp/env
9.  Java System properties (System.getProperties()).
10. OS environment variables.
11. ```RandomValuePropertySource``` nơi chứa các property chỉ trong ```random.*```
12. Profile-specific application properties outside of your packaged jar (application-{profile}.properties and YAML variants).
13. Profile-specific application properties packaged inside your jar (application-{profile}.properties and YAML variants).
14. Application properties bên ngoài packaged jar (application.properties and YAML variants).
15. Application properties packaged bên trong jar (application.properties and YAML variants).
16. ```@PropertySource``` annotation trong @Configuration classes
    https://docs.spring.io/spring/docs/5.1.10.RELEASE/javadoc-api/org/springframework/context/annotation/PropertySource.html
17. Default properties (specified by setting SpringApplication.setDefaultProperties).

## 2.1 Configuring random value
```RandomValuePropertySource``` hữu dụng cho việc inject các random value. Nó có thể tạo ra integers, long, uuid, string như ví dụ sau:

```
my.secret=${random.value}
my.number=${random.int}
my.bignumber=${random.long}
my.uuid=${random.uuid}
my.number.less.than.ten=${random.int(10)}
my.number.in.range=${random.int[1024,65536]}
```

```random.int*``` systax là ```OPEN value (,max) CLOSE``` OPEN,CLOSE có thể là bất kì kí tự và value,mã là integer. Nếu có max thì value sẽ là min value và max là max value

## 2.2 Accessing command line properties
Mặc định, SpringApplication convert bất kì command line option argument nào (argument được bắt đầu với -- như --server.port=9000) cho 1 property và thêm nó vào Spring environment). Command line properties luôn được ưu tiên hơn các property source khác

Nếu bạn không muốn command line properties được add vào Environment, có thể disable chúng bằng cách dùng SpringApplication.setAddCommandLineProperties(false)

## 2.3 Application property files
```SpringApplication``` load các property từ ```application.properties``` file trong các location sau và thêm chúng vào Spring Environment:
1. A /config subdirectory of the current directory
2. The current directory
3. A classpath /config package
4. The classpath root

list trên được xếp theo thứ tự ưu tiên (các property được define ở location cao hơn trong list sẽ override các define của nó ở location thấp hơn)

Nếu không muốn dùng `application.properties` như tên file cấu hình, bạn có thể chuyển thành tên file khác bằng `spring.config.name` environment property. Có thể rể tới các location cụ thể bằng cách dùng `spring.config.location` environment property (dùng dấu , để ngăn cách các phần khác nhau)
vd:
```
$ java -jar myproject.jar --spring.config.name=myproject

```

```
$ java -jar myproject.jar --spring.config.location=classpath:/default.properties,classpath:/override.properties
```

`spring.config.name` và `spring.config.location` được dùng rất sớm để quyết định file nào sẽ được load lên. Nó cần phải được define như 1 enviroment property (thường là OS environment variable, system property hay command line argument)

Nếu `spring.config.location` bao gồm các directory hoặc file, nó nên được kết thúc bởi '/' (và khi runtime, nó sẽ được nối thêm vào tên được tạo ra bởi `spring.config.name` đã được load lên trước đó, kể cả tên file profile-specific). Các file specified trong `spring.config.location` được sử dụng nguyên trạng, không cần support từ các profile-specific variant, và sẽ được override lại bởi bất kì profile-specific properties nào.

Config location được tìm kiếm trong reverse order. Mặc định, configured location là: `classpath:/,classpath:/config/,file:./,file:./config/`. Kết quả search order là:
1. file:./config/
2. file:./
3. classpath:/config/
4. classpath:/

Khi muốn custom config location bằng cách dùng `spring.config.location`, ta thay thế default location.
Vd: nếu `spring.config.location` được config với giá trị `classpath:/custom-config/,file:./custom-config/`, thì thứ tự search sẽ là:
1. file:./custom-config/
2. classpath:custom-config/

hơn nữa, khi muốn custom config location bằng cách dùng `spring.config.additional-location`, chúng được dùng thêm vào các default location. Các additional location sẽ được search trước các default location. Ví dụ: nếu cấu hình additonal location là `classpath:/custom-config/,file:./custom-config/`, thứ tự search sẽ là:
1. file:./custom-config/
2. classpath:custom-config/
3. file:./config/
4. file:./
5. classpath:/config/
6. classpath:/

Thứ tự search này cho phép bạn chỉ định các default value trong cùng 1 file config và lựa chọn override các value đó như thế nào. Bạn có thể cung cấp default value cho application trong `application.properties` (hoặc bất kì chỗ nào khác basename `spring.config.name`) tại 1 trong những default location. Các default value này có thể được override lúc runtime với các file located khác nhau tại 1 trong số các custom location.

Nếu bạn dùng environment variable nhiều hơn system properties, hầu hết các operating system không cho phép các key name tách biệt, dùng _ thay thế( vd SPRING_CONFIG_NAME thay vì spring.config.name)

Nếu application chạy trong container thì JNDI properties (trong java:comp/env) hay servlet context initialization parameter có thể dùng thay thế, hoặc tốt hơn thì là environment variable hay system properties

## 2.4 Profile-specific properties
Ngoài `application.properties` file, profile-specific properties có thể được define bằng các dùng tên quy ước sau: `application-{profile}.properties`. Environment có 1 set các default profile (mặc định là [default]) nó được dùng nếu không có active profile nào được set. Nói cách khác, nếu không có profile nào được activate thì các property từ application-default.properties sẽ được load.

Profile-specific properties sẽ được load từ các location giống nhau như standard `application.properties`, với profile-specific file luôn override các non-specific, kể cả khi profile-specific file nằm trong hay ngoài packaged.jar

Nếu có nhiều profile được chỉ định, thì sẽ áp dụng chiến lược last-win strategy. VD: các profile được chỉ định bởi `spring.profiles.active` property được thêm sau khi nó đã được configured bởi `SpringApplication` API và được ưu tiên hơn.


### Default property value
By default, a missing property causes an exception. But, it doesn’t have to. You may decide to make an optional property. When the key is missing in the application.properties file, you can instruct Spring to inject a default value for a property key.

```
@Value("${sbpg.init.welcome-message:Hello world}")
```

## 2.5 Placeholder in properties
Các value trong `application.properties` được filter thông qua `Environment` hiện có khi nó được dùng, nên bạn có thể refer ngược lại defined value trước đó

http://zetcode.com/spring/propertyplaceholder/

```
app.name=MyApp
app.description=${app.name} is a Spring Boot application
```

## 2.6 Encrypting properties
Spring boot ko cung cấp bắt kì built nào để support encrypting property values, tuy nhiên nó cung cấp các hook point cần thiết để modify value nằm trong Spring `Environment`. `EnvironmentPostProcessor` interface cho phép điều khiển `Environment` trước khi application start.

## 2.7 Dùng YAML thay vì Properties
YAML (YAML Ain't Markup Language) là 1 superset của JSON và là 1 format tiện lợi để chỉ định thứ bậc configuration data. `SpringApplication` class tự độn support YAML như 1 bản thay thế cho properties bất kì khi nào bạn có `SnakeYAML` library trong classpath

Nếu dùng 'starters', SnakeYAML sẽ được tự động cung cấp bởi `spring-boot-starter`

### YAML style
- Conventional block format: dùng - + space
  ```
    --- # Favorite movies
     - Casablanca
     - North by Northwest
     - The Man Who Wasn't There
  ```

- Inline Format: , + space
    ```
        --- # Shopping list
   [milk, groceries, eggs, juice, fruits]
   ```
- Folded Text:
  ```
    - {name: John Smith, age: 33}
    - name: Mary Smith
      age: 27
  ```


### 2.7.1 Loading YAML
Spring framework cung cấp 2 class dùng để load YAML document. `YamlPropertiesFactoryBean` load YAML như là Properties  và `YamlMapFactoryBean` load YAML như 1 Map

Vd: có 1 YAML document như sau:
```
environments:
	dev:
		url: https://dev.example.com
		name: Developer Setup
	prod:
		url: https://another.example.com
		name: My Cool App
```

Vd trước sẽ chuyển thành các properties sau:

```
environments.dev.url=https://dev.example.com
environments.dev.name=Developer Setup
environments.prod.url=https://another.example.com
environments.prod.name=My Cool App
```

YAML list sẽ được biểu diễn như các property key với [index].

Vd với YAML như sau:

```
my:
servers:
	- dev.example.com
	- another.example.com
```

Vd trước sẽ được chuyển thành các properties sau:

```
my.servers[0]=dev.example.com
my.servers[1]=another.example.com
```

Để bind tới các properties như trên bằng cách dùng Spring Boot's `Binder` utilities (đó là những gì mà @ConfigurationProperties làm), bạn cần có 1 property trong target bean của type `java.util.List` (hoặc `Set`) và ko cần phải cung cấp setter hay initialize nó với 1 mutable value.

Vd: bind tới properties ở trên

```
@ConfigurationProperties(prefix="my")
public class Config {

	private List<String> servers = new ArrayList<String>();

	public List<String> getServers() {
		return this.servers;
	}
}
```

### 2.7.2 Exposing YAML như Properties trong Sping Environment
`YamlPropertySourceLoader` class sẽ được dùng để expose YAML như `PropertySource` trong Spring `Environment. Điều đó cho phép dùng @Value annotation với placeholder systax để access YAML properties

### 2.7.3 Multi-profile YAML documnent
Bạn có thể chỉ định multiple profile-specific YAML document trong 1 file duy nhất bằng cách dùng `spring.profiles` key để indicate khi document apply, như vd sau:

```
server:
	address: 192.168.1.100
---
spring:
	profiles: development
server:
	address: 127.0.0.1
---
spring:
	profiles: production & eu-central
server:
	address: 192.168.1.120
```
trong vd trước, nếu development profile được active, `server.address` property là 127.0.0.1. Tương tự, nếu production và eu-central profile được active, `server.address` property sẽ là 192.168.1.120. Nếu không có profiles nào được enable thì value cho property sẽ là 192.168.1.100

`spring.profiles` có thể chứa các tên profile đơn giản (vd như production)
 hoặc 1 profile expresion. 1 profile expression cho phép nhiều profile logic phức tạp được biểu diễn, vd production & (eu-central | eu-west)

 Nếu không có cái nào được active rõ ràng khi application context start, default profile sẽ được activate. Nên trong YAML sau, ta set a value cho `spring.security.user.password` chỉ available trong default profile

 ```
 server:
  port: 8000
---
spring:
  profiles: default
  security:
    user:
      password: weak
 ```

Ngược lại vd sau, password sẽ luôn được set vì nó không đính kèm với bất kì profile nào, nên nó sẽ phải được reset rõ ràng trong mọi profile nếu cần thiết.

```
server:
  port: 8000
spring:
  security:
    user:
      password: weak
```

Spring profiles được design bằng cách dùng `spring.profiles` element, thêm ! để phủ định (negated). Nếu cả negated và non-negated profile đều được chỉ định trong cùng 1 document, ít nhất phải match với 1 non-negated profile  và ko negated profile match

### 2.7.4 YAML shortcomings
YAML file có thể ko được load bởi 
`@PropertySource`. Nên trong trường hợp đó cần load value bằng cách đó, bạn cần dùng 1 properties file

Dùng multi YAML document syntax trong profile-specific YAML file có thể dẫn đến trường hợp không lường trước được.
Vd config 1 file sau:

application-dev.yml. 
```
server:
  port: 8000
---
spring:
  profiles: "!test"
  security:
    user:
      password: "secret"
```

Nếu bạn run application với argument `--spring.profiles.active=dev"` và mong đợi `security.user.password` sẽ được set là secret nhưng không

Document lồng nhau (nested document) sẽ bị filter vì main file có tên là `application-dev.yml`. Nó đã được xem là 1 profile-specific và nested document sẽ bị ignored

Recommend là không nên mĩ profile-specific YAML file với multiple YAML document. Chỉ nên dùng 1 trg 2

## 2.8 Type-safe configuration properties
Dùng `@Value("${property}")` annotation để inject configuration properties đôi khi sẽ trở nên cồng kênh, đặc biệt là khi bạn làm việc với multiple properties hoặc data là dạng phân cấp. Spring boot cung cấp 1 alternative method làm việc với properties 


# 3. 
## 3.1 Unit test


## 3.2 spring-boot-starter-test, @RunWith(SpringRunner.class)
Spring boot cung cấp 1 số các unitilities và annotation để hỗ trợ test.
Test được support bởi 2 module:
- spring-boot-test: bao gồm các core-item
- spring-boot-test-autoconfigure: support auto-configuration cho test

Thường dùng spring-boot-starter-test để import cả 2 spring boot test module trên

## Test Scope dependencies:
spring-boot-starter-test "Starter" bao gồm các library dưới đây:
- JUnit 4: The de-facto standard for unit testing Java applications.
- Spring Test & Spring Boot Test: Utilities and integration test support for - Spring Boot applications.
- AssertJ: A fluent assertion library.
- Hamcrest: A library of matcher objects (also known as constraints or predicates).
- Mockito: A Java mocking framework.
- JSONassert: An assertion library for JSON.
- JsonPath: XPath for JSON.


## 3.3 Build runnable jar 
JAR (Java Archive)

Spring Boot Loader-compatible jar files should be structured in the following way:

```
example.jar
 |
 +-META-INF
 |  +-MANIFEST.MF
 +-org
 |  +-springframework
 |     +-boot
 |        +-loader
 |           +-<spring boot loader classes>
 +-BOOT-INF
    +-classes
    |  +-mycompany
    |     +-project
    |        +-YourClasses.class
    +-lib
       +-dependency1.jar
       +-dependency2.jar
```

### Spring Boot Maven Plugin
https://www.geeksforgeeks.org/jar-files-java/

Spring Boot Maven Plugin: cho phép:
- package executable jar or war archives
- run app in-place

https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html

- *.jar
- *.jar.original

```
mvn package
```

- Maven packaging `package` Phase tạo ra *.jar file
- execute `spring-boot:repackage` repack bản previous *.jar thành *.jar.original
- generate spring boot executable jar package file *.jar


- run jar file with profile
```
java -jar myapp.jar --spring.profiles.active=dev
```


- How can I create an executable JAR with dependencies using Maven?
  https://stackoverflow.com/questions/574594/how-can-i-create-an-executable-jar-with-dependencies-using-maven?page=1&tab=votes#tab-top

- Fat/uber jar 
  https://dzone.com/articles/the-skinny-on-fat-thin-hollow-and-uber

- build fat/uber jar with spring boot
  https://www.baeldung.com/deployable-fat-jar-spring-boot

- maven lifecycle
- use a spring boot application as a dependency: share class cho các project khác, recommend chuyển code đó thành các seperate module => depended vào các project khác. Nếu ko thì chuyền thành separate artifact và dùng như 1 dependency. Executable archive không tìm đươc nếu executable jar được dùng như 1 dependency. Để pro