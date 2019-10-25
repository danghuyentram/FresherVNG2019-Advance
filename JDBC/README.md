# JDBC

# 1. JDBC and JDBC driver
## 1.1 JDBC
- JDBC(Java Database Connectivity): API để connect và execute các câu query trong db. JDBC có thể làm việc với bất kì db nào miễn có cung cấp driver thích hợp

JDBC API cung cấp các class và interface sau:
- DriverManager: Lớp này quản lý các Database Driver. Ánh xạ các yêu cầu kết nối từ ứng dụng Java với Data driver thích hợp bởi sử dụng giao thức kết nối phụ.
- Driver: Interface này xử lý các kết nối với Database Server. Hiếm khi, bạn tương tác trực tiếp với các đối tượng Driver này. Thay vào đó, bạn sử dụng các đối tượng DriverManager để quản lý các đối tượng kiểu này.
- Connection: Đối tượng Connection biểu diễn ngữ cảnh giao tiếp. Interface này chứa nhiều phương thức đa dạng để tạo kết nối với một Database.
- Statement: Bạn sử dụng các đối tượng được tạo từ Interface này để đệ trình các lệnh SQL tới Database. Ngoài ra, một số Interface kết thừa từ nó cung chấp nhận thêm các tham số để thực thi các thủ tục đã được lưu trữ.
- ResultSet: Các đối tượng này giữ dữ liệu được thu nhận từ một Database sau khi bạn thực thi một truy vấn SQL. Nó nóng vai trò như một Iterator để cho phép bạn vọc qua dữ liệu của nó.
- SQLException: Lớp này xử lý bất cứ lỗi nào xuất hiện trong khi làm việc với Database.

## 1.2 JDBC driver
JDBC driver là 1 JDBC API implement dùng để connect tới 1 loại db cụ thể. Một số loại JDBC driver
- Loại 1: chứa ánh xạ tới data access API khác. VD: JDBC-ODBC driver
- Loại 2: là 1 implementation dùng các client-side library của target db, có thể gọi là native-API driver
- Loại 3: dùng middleware để convert JDBC call thành database-specific call, gọi là network protocol driver
- Loại 4: connect trực tiếp tới db bằng cách convert JDBC call thành db-specific call, gọi là db protocol driver hoặc thin driver

Loại 4 được dùng phổ biến nhất, vì nó là 1 platform independent. Connect trực tiếp tới db server cung cấp performance tốt hơn các dạng khác. Nhược điểm của loại này là db-specific cho mỗi db thì có protocol cụ thể của riêng nó

# 2. Connect to db
- initialize driver
- open db connection

## 2.1 Registering the driver
Vd: dùng loại 4: db protocol driver
- Dùng MySQL db, add `mysql-connector-java` dependency

```
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>6.0.6</version>
</dependency>
```
- register driver dùng Class.forName() method giúp dynamically load driver class

```
Class.forName("com.mysql.cj.jdbc.Driver");
```

## 2.2 Tạo connection
- Để mở 1 connection, dùng `getConnection()` method của DriverManager class. Method này yêu cầu 1 connection URL string parameter:

```
Connection con = DriverManager
  .getConnection("jdbc:mysql://localhost:3306/myDb", "user1", "pass");
```

Syntax của connection URL phù thuộc vào loại db bạn dùng. vd:

```
jdbc:mysql://localhost:3306/myDb?user=user1&password=pass

jdbc:postgresql://localhost/myDb

jdbc:hsqldb:mem:myDb
```

Để connect tới db cụ thể là mydb db, ta tạo db và 1 user, add grant 1 necessary access

```
CREATE DATABASE myDb;
CREATE USER 'user1' IDENTIFIED BY 'pass';
GRANT ALL on myDb.* TO 'user1';
```

# 3. Executing SQL statements

Để gửi 1 câu lệnh sql tới db, ta dùng instance của Statement, PreparedStatement hoặc CallableStatement. Cả 3 đều là Connection object

## 3.1 Statement
Statement interface chứa các function cần để executing SQL command

- Tạo 1 Statement object
```
Statement stmt = con.createStatement();
```
- Execute câu lệnh SQL thông qua 3 method:
  - `executeQuery()` : select
  - `executeUpdate()`: update data hoặc update cấu trúc db
  - `execute()`: dùng cho cả 2 trường hợp trên nếu ko biết trước kết quả

- thêm 1 table: student vào db:
    ```
    String tableSql = "CREATE TABLE IF NOT EXISTS employees"
    + "(emp_id int PRIMARY KEY AUTO_INCREMENT, name varchar(30),"
    + "position varchar(30), salary double)";
    stmt.execute(tableSql);
    ```

    Nếu `execute()` method dùng để update data, thì `stmt.getUpdateCount() method` sẽ trả về số row afected

    Nếu result = 0  thì ko row nào được afected hoặc nó là câu lệnh update db structure

    Nếu value = -1 thì câu lệnh là select. Có thể lấy result ra bằng `stmt.getResultSet().` 


- thêm 1 record vào table dùng `executeUpdate()` method:
    ```
    String insertSql = "INSERT INTO employees(name, position, salary)"
    + " VALUES('john', 'developer', 2000)";
    stmt.executeUpdate(insertSql);
    ```

    Method return số row afected hoặc 0 nếu là update db structure
    Có thể lấy được record từ table bằng cách dùng `executeQuery()` method, nó sẽ return 1 object type `ResultSet`

    ```
    String selectSql = "SELECT * FROM employees";
    ResultSet resultSet = stmt.executeQuery(selectSql);
    ```

## 3.2 PreparedStatement
PreparedStatement object chứa chuỗi SQL được biên dịch trước. Nó có thể chứa 1 hay nhiều parameter kí hiệu là ?
- tạo 1 `PreparedStatement` để update record trong employees dựa vào parameters:

    ```
    String updatePositionSql = "UPDATE employees SET position=? WHERE emp_id=?";
    PreparedStatement pstmt = con.prepareStatement(updatePositionSql);
    ```

- để thêm parameters cho `PreparedStatement`, ta dùng setter: setX: với X là kiểu dữ liệu của parameter, method argument là order và value của parameter
  ```
    pstmt.setString(1, "lead developer");
    pstmt.setInt(2, 1);
  ```

- statement được execute bằng 1 trong 3 method như trên:
  ```
  int rowsAffected = pstmt.executeUpdate();
  ```


## 3.3 CallableStatement
`CallableStatement` interface cho phép gọi 1 stored procedures
- tạo 1 `CallableStatement` object, dùng prepareCall() method của Connection
    ```
    String preparedSql = "{call insertEmployee(?,?,?,?)}";
    CallableStatement cstmt = con.prepareCall(preparedSql);
    ```

- Set input values cho stored procedure giống `PreparedStatement` interface, dùng setX() method:
  ```
    cstmt.setString(2, "ana");
    cstmt.setString(3, "tester");
    cstmt.setDouble(4, 2000);
  ```
- Nếu stored procedure có output parameter, add thêm `registerOutParameter()` method
  ```
  cstmt.registerOutParameter(1, Types.INTEGER);
  ```

- để execute statement và nhận giá trị trả về dùng getX() method tương ứng:
  ```
    cstmt.execute();
    int new_id = cstmt.getInt(1);
  ```

VD:
- tạo 1 store procedure trong mysql db
  ```
  delimiter //
    CREATE PROCEDURE insertEmployee(OUT emp_id int, 
    IN emp_name varchar(30), IN position varchar(30), IN salary double) 
    BEGIN
    INSERT INTO employees(name, position,salary) VALUES (emp_name,position,salary);
    SET emp_id = LAST_INSERT_ID();
    END //
    delimiter ;
  ```

- stored procedure trên sẽ insert 1 record mới vào employees table và trả về id của record mới
- Để có thể run stored procedure từ Java, connection user cần truy cập vào stored procedure's metadata bằng cách cấp quyền cho user với mọi stored procedure trong mọi db:
  ```
  GRANT ALL ON mysql.proc TO 'user1';
  ```
- Hoặc mở connection với property là noAccessToProcedureBodies set = true:
  ```
  con = DriverManager.getConnection(
  "jdbc:mysql://localhost:3306/myDb?noAccessToProcedureBodies=true", 
  "user1", "pass");
  ```

  Nó nói với JDBC API rằng user không có quyền để read procedure metadata nên nó sẽ tạo tất cả parameter như INOUT String parameters.

  # 4. ResultSet
  Sau khi execute query, result sẽ đươc biểu diễn như 1 `ResultSet` object, nó là cấu trúc giống table có hàng và cột

  ## 4.1 ResultSet Interface
  `ResultSet` dùng next() method để chuyển tới dòng tiếp theo:

  - tạo 1 Employee class để lưu record nhận được:
    ```
    public class Employee {
        private int id;
        private String name;
        private String position;
        private double salary;
    
    // standard constructor, getters, setters
    }
    ```

- truy vết ResultSet và tạo  Employee object ứng với từng record
  ```
    String selectSql = "SELECT * FROM employees";
    ResultSet resultSet = stmt.executeQuery(selectSql);
            
    List<Employee> employees = new ArrayList<>();
            
    while (resultSet.next()) {
        Employee emp = new Employee();
        emp.setId(resultSet.getInt("emp_id"));
        emp.setName(resultSet.getString("name"));
        emp.setPosition(resultSet.getString("position"));
        emp.setSalary(resultSet.getDouble("salary"));
        employees.add(emp);
    }
  ```
- Dùng getX() method để lấy từng value ra

## 4.2 Updateable ResultSet
`ResultSet` object chỉ có thể được truy vết ra chứ không modifiedduojcwd.
Nếu muốn dùng `ResultSet` để update data và truy vết nó thì cần tạo 1 `Statement` object với additional parameters

```
stmt = con.createStatement(
  ResultSet.TYPE_SCROLL_INSENSITIVE, 
  ResultSet.CONCUR_UPDATABLE
);
```

Để navigate type ResultSet này, ta cần dùng 1 trong số các method sau:
- first(), last(), beforeFirst(), beforeLast() : để chuyển đến dòng đầu hoặc cuối của `ResultSet` hoặc dòng trước đó
- next(), previous(): di chuyển tiến hay lùi trong `ResultSet`
- getRow(): lấy số row hiện tại
- moveToInsertRow(), moveToCurrentRow(): di chuyển đến 1 row mới đến insert và trả lại vị trí hiện tại nếu nó đang trên row mới
- absolute(int row) : di chuyển đến 1 row cụ thể
- relative(int nrRows) : di chuyển con trỏ với số row

Update `ResultSet` có thể dùng với method updateX() x là type của cell data. Method này chỉ update `ResultSet` object mà không update database table

Để đảm bảo nhất quán `ResultSet` thay đổi db, ta phải dùng thêm 1 trong số các method sau:
- updateRow(): để nhất quán thay đổi với row hiện tại trong db
- insertRow(), deleteRow() : để thêm 1 row mới hay xóa row hiện tại trong db
- refreshRow(): refresh `ResultSet` với bất kì sự thay đổi nào trong db
- cancelRowUpdates(): cancel thay đổi với row hiện tại

vd:
```
Statement updatableStmt = con.createStatement(
  ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
ResultSet updatableResultSet = updatableStmt.executeQuery(selectSql);
 
updatableResultSet.moveToInsertRow();
updatableResultSet.updateString("name", "mark");
updatableResultSet.updateString("position", "analyst");
updatableResultSet.updateDouble("salary", 2000);
updatableResultSet.insertRow();
```

# 5. Parsing Metadata
JDBC API cho phép tra cứu infor về db gọi là metadata

## 5.1 DatabaseMetadata
DatabaseMetadata interface dùng để có được thông tin chung về db như tables, stored procedures hoặc SQL dialect

vd: lấy infor về db tables:
```
DatabaseMetaData dbmd = con.getMetaData();
ResultSet tablesResultSet = dbmd.getTables(null, null, "%", null);
while (tablesResultSet.next()) {
    LOG.info(tablesResultSet.getString("TABLE_NAME"));
}
```

## 5.2 ResultSetMetadata
`ResultSetMetadata` interface dùng để tìm infor về 1 ResultSet nhất định, như số cột của nó

```
ResultSetMetaData rsmd = rs.getMetaData();
int nrColumns = rsmd.getColumnCount();
 
IntStream.range(1, nrColumns).forEach(i -> {
    try {
        LOG.info(rsmd.getColumnName(i));
    } catch (SQLException e) {
        e.printStackTrace();
    }
});
```

# 6 Handling transaction
- set `autoCommit` property của Connection = false, sau đó dùng commit() và rollback() method để control transaction

```
String updatePositionSql = "UPDATE employees SET position=? WHERE emp_id=?";
PreparedStatement pstmt = con.prepareStatement(updatePositionSql);
pstmt.setString(1, "lead developer");
pstmt.setInt(2, 1);
 
String updateSalarySql = "UPDATE employees SET salary=? WHERE emp_id=?";
PreparedStatement pstmt2 = con.prepareStatement(updateSalarySql);
pstmt.setDouble(1, 3000);
pstmt.setInt(2, 1);
 
boolean autoCommit = con.getAutoCommit();
try {
    con.setAutoCommit(false);
    pstmt.executeUpdate();
    pstmt2.executeUpdate();
    con.commit();
} catch (SQLException exc) {
    con.rollback();
} finally {
    con.setAutoCommit(autoCommit);
}
```

# 7. Close connection
Nếu không dùng nữa thì nên đóng connection để release db resource

```
con.close();
```

Nếu không đóng connection:
- memory leak

# 8. Connection pooling
Enabling Connection pooling allows the pool manager to keep connections in a “pool” after they are closed

# 9. Statement pooling
 Enabling statement pooling allows the driver to re-use Prepared Statement objects

# 10 SQL injection
https://viblo.asia/p/sql-injection-va-cach-phong-chong-OeVKB410lkW

http://javabypatel.blogspot.com/2015/09/how-prepared-statement-in-java-prevents-sql-injection.html


