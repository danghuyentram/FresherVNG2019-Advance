package com.example.demojdbcconnectdb;

import com.example.demojdbcconnectdb.model.Employee;
import org.apache.logging.log4j.LogManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

@SpringBootApplication
public class DemoJdbcConnectDbApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(DemoJdbcConnectDbApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager
				.getConnection("jdbc:mysql://localhost:3306/myDB", "user1", "password");



		Statement stmt = con.createStatement();


		// statement
//		String tableSql = "CREATE TABLE IF NOT EXISTS employees"
//				+ "(emp_id int PRIMARY KEY AUTO_INCREMENT, name varchar(30),"
//				+ "position varchar(30), salary double)";
//		stmt.execute(tableSql);

//		String insertSql = "INSERT INTO employees(name, position, salary)"
//				+ " VALUES('john', 'developer', 2000)";
//		stmt.executeUpdate(insertSql);


		// resultset
		String selectSql = "SELECT * FROM employees";
		ResultSet resultSet = stmt.executeQuery(selectSql);
//		List<Employee> employees = new ArrayList<>();
//
//		while (resultSet.next()) {
//			Employee emp = new Employee();
//			emp.setId(resultSet.getInt("emp_id"));
//			emp.setName(resultSet.getString("name"));
//			emp.setPosition(resultSet.getString("position"));
//			emp.setSalary(resultSet.getDouble("salary"));
//			employees.add(emp);
//
//			System.out.println(emp.getId()+" "+emp.getName()+" "+emp.getPosition()+" "+emp.getSalary());
//
//		}

		// update resultset
//		Statement updatableStmt = con.createStatement(
//				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//		ResultSet updatableResultSet = updatableStmt.executeQuery(selectSql);
//
//		updatableResultSet.moveToInsertRow();
//		updatableResultSet.updateString("name", "mark");
//		updatableResultSet.updateString("position", "analyst");
//		updatableResultSet.updateDouble("salary", 2000);
//		updatableResultSet.insertRow();




		// preparedstatement
//		String updatePositionSql = "UPDATE employees SET position=? WHERE emp_id=?";
//		PreparedStatement pstmt = con.prepareStatement(updatePositionSql);
//		pstmt.setString(1, "lead developer");
//		pstmt.setInt(2, 1);
//		System.out.println( pstmt.executeUpdate());


		// callablestatement
//		Connection con = DriverManager.getConnection(
//				"jdbc:mysql://localhost:3306/myDB?noAccessToProcedureBodies=true",
//				"user1", "password");
//
//		String preparedSql = "{call insertEmployee(?,?,?,?)}";
//		CallableStatement cstmt = con.prepareCall(preparedSql);
//		cstmt.setString(2, "ana");
//		cstmt.setString(3, "tester");
//		cstmt.setDouble(4, 2000);
//		cstmt.registerOutParameter(1, Types.INTEGER);
//		cstmt.execute();
//		System.out.println(cstmt.getInt(1));

		// DatabaseMetadata
//		DatabaseMetaData dbmd = con.getMetaData();
//		ResultSet tablesResultSet = dbmd.getTables(null, null, "%", null);
//		while (tablesResultSet.next()) {
//			System.out.println(tablesResultSet.getString("TABLE_NAME"));
//		}

		// ResultSetMetadata
//		ResultSetMetaData rsmd = resultSet.getMetaData();
//		int nrColumns = rsmd.getColumnCount();
//
//		IntStream.range(1, nrColumns).forEach(i -> {
//			try {
//				System.out.println(rsmd.getColumnName(i));
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		});

		// transaction
//		String updatePositionSql = "UPDATE employees SET position=? WHERE emp_id=?";
//		PreparedStatement pstmt = con.prepareStatement(updatePositionSql);
//		pstmt.setString(1, "lead developer");
//		pstmt.setInt(2, 1);
//
//		String updateSalarySql = "UPDATE employees SET salary=? WHERE emp_id=?";
//		PreparedStatement pstmt2 = con.prepareStatement(updateSalarySql);
//		pstmt.setDouble(1, 4000);
//		pstmt2.setInt(2, 1);
//
//		boolean autoCommit = con.getAutoCommit();
//		try {
//			con.setAutoCommit(false);
//			pstmt.executeUpdate();
//			pstmt2.executeUpdate();
//			con.commit();
//		} catch (SQLException exc) {
//			exc.printStackTrace();
//			con.rollback();
//		} finally {
//			con.setAutoCommit(autoCommit);
//		}
		// close connection
		con.close();

	}
}
