# Spring JDBC

Mọi class trong Spring JDBC chia thành 4 package:
- core: core functionality của JDBC, vd: JdbcTemplate, SimpleJdbcInsert, SimpleJdbcCall and NamedParameterJdbcTemplate.
- datasource: utility class để access 1 datasource
- object: db access theo cách hướng đối tượng, cho phép executing query và return result như 1 business object, map query result giữa columns và properties của 1 business object
- support: support class cho các class dưới core và object package


# 1. DataSource
- datasource: utility class để access 1 datasource

# 2. JDBC template
Vấn đề của JDBC API:
- code quá nhiều trước và sau khi execute câu query như tạo connection, statement, close resultset, connection,...
- Cần handle các exception, transaction
- Lặp lại code với các db khác nhau

Lợi thế của Spring JDBCTemplate: loại bỏ tất cả các vấn đề trên và cung cấp các method để viết câu query trực tiếp, nhanh chóng

Spring JDBC cung cấp các cách tiếp cận tới JDBC db access:
- JdbcTemplate
- NamedParameterJdbcTemplate
- SimpleJdbcTemplate
- SimpleJdbcInsert and SimpleJdbcCall

## 2.1 JDBCTemplate class
Là class trung tâm trong Spring JDBC support class có nhiệm vụ create và release resource như connection,... nên sẽ không gây ra lỗi nào nếu quên đóng connection

Nó handle exception và cung cấp thông tin về exception message bằng cách nhờ exception class trong org.springframework.dao package.

Ta có thể biểu diễn mọi db operation bằng JDBCTemplate class như insert, update, delete, retrieval data từ db

Các method trong spring JDBCTemplate class

| No. | Method                                                         | Description                                                                                 |
|-----|----------------------------------------------------------------|---------------------------------------------------------------------------------------------|
| 1)  | public int update(String query)                                | is used to insert, update and delete records.                                               |
| 2)  | public int update(String query,Object... args)                 | is used to insert, update and delete records using PreparedStatement using given arguments. |
| 3)  | public void execute(String query)                              | is used to execute DDL query.                                                               |
| 4)  | public T execute(String sql, PreparedStatementCallback action) | executes the query by using PreparedStatement callback.                                     |
| 5)  | public T query(String sql, ResultSetExtractor rse)             | is used to fetch records using ResultSetExtractor.                                          |
| 6)  | public List query(String sql, RowMapper rse)                   | is used to fetch records using RowMapper.                                                   |

https://www.javatpoint.com/spring-JdbcTemplate-tutorial

https://docs.spring.io/spring/docs/2.0.x/reference/jdbc.html

### JDBC link to its DataSource
JDBCTemplate có thể được dùng trong 1 DAO implement thông qua khởi tạo trực tiếp với 1 DataSource reference, hoặc được configured trong Spring IOC container và đưa cho các DAOs như 1 bean reference. DataSource nên được configured như 1 bean trong Spring IOC container, để cung cấp 1 service trực tiếp, 2 là để prepared template

Các instance của JDBCTemplate class là threadsafe một khi được cấu hình. Bạn có thể configure 1 single instance của JdbcTemplate và safety inject nó vào multiple DAOs (hoặc repositories). JdbcTemplate là stateful, nó duy trì reference tới DataSource, nhưng state này không phải là conversation state

Cách dùng JdbcTemplate class phổ biến nhất (và class liên kết ` SimpleJdbcTemplate and NamedParameterJdbcTemplate`) là configure 1 DataSource trong Spring configuration file, sau đó inject DataSource bean này vào DAO class, JdbcTemplate được tạo trong setter chơ DataSource, DataSource sẽ như thế này

```
public class JdbcCorporateEventDao implements CorporateEventDao {

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // JDBC-backed implementations of the methods on the CorporateEventDao follow...
}
```

configuration sẽ như sau:
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd">

    <bean id="corporateEventDao" class="com.example.JdbcCorporateEventDao">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!-- the DataSource (parameterized for configuration via a PropertyPlaceHolderConfigurer) -->
    <bean id="dataSource" destroy-method="close" class="org.apache.commons.dbcp.BasicDataSource">
        <property name="driverClassName" value="${jdbc.driverClassName}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

</beans>
```



## 2.2 Basic query:

```
int result = jdbcTemplate.queryForObject(
    "SELECT COUNT(*) FROM EMPLOYEE", Integer.class);
```

```
public int addEmplyee(int id) {
    return jdbcTemplate.update(
        "INSERT INTO EMPLOYEE VALUES (?, ?, ?, ?)", 5, "Bill", "Gates", "USA");
}
```

## 2.3 Query với Named parameters
framework – the NamedParameterJdbcTemplate.

```
SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", 1);
return namedParameterJdbcTemplate.queryForObject(
    "SELECT FIRST_NAME FROM EMPLOYEE WHERE ID = :id", namedParameters, String.class);
```

MapSqlParameterSource
```
Employee employee = new Employee();
employee.setFirstName("James");
 
String SELECT_BY_ID = "SELECT COUNT(*) FROM EMPLOYEE WHERE FIRST_NAME = :firstName";
 
SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(employee);
return namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID, namedParameters, Integer.class);
```

## 2.4 Mapping query result to Java Object
Map query result thành Java object bằng cách implement `RowMapper` interface

```
public class EmployeeRowMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();
 
        employee.setId(rs.getInt("ID"));
        employee.setFirstName(rs.getString("FIRST_NAME"));
        employee.setLastName(rs.getString("LAST_NAME"));
        employee.setAddress(rs.getString("ADDRESS"));
 
        return employee;
    }
}
```

# 3 Spring JDBC with Spring boot
Spring boot cung cấp starter spring-boot-starter-jdbc

## 3.1 Maven dependency
with mysql db

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

## 3.2 Configuration
Spring boot auto configure data source, chỉ cần cung cấp propertues trong properties file

```
spring.datasource.url=jdbc:mysql://localhost:3306/springjdbc
spring.datasource.username=guest_user
spring.datasource.password=guest_password
```

