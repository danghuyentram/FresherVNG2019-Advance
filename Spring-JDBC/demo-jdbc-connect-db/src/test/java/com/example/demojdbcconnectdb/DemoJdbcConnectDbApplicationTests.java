package com.example.demojdbcconnectdb;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoJdbcConnectDbApplicationTests {



//	@Test
//	public void checkConnectionClose() throws ClassNotFoundException, SQLException {
//		// GIVEN
//
////	 	check and update max connection in mysql
////		SHOW VARIABLES LIKE "max_connections";
////		SET GLOBAL max_connections = 2;
//
//
//		Class.forName("com.mysql.cj.jdbc.Driver");
//		Connection con = DriverManager
//				.getConnection("jdbc:mysql://localhost:3306/myDB", "user1", "password");
//
//
//		// WHEN
//		try{
//			con = null;
//			con = DriverManager
//					.getConnection("jdbc:mysql://localhost:3306/myDB", "user1", "password");
//
//		}catch (SQLNonTransientConnectionException e){
//			e.printStackTrace();
//		}
//
//		// THEN
//		Assert.assertNull(con);
//	}

	@Test
	public void checkrollback() throws ClassNotFoundException, SQLException {
		// GIVEN

		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager
				.getConnection("jdbc:mysql://localhost:3306/myDB", "user1", "password");

		String updatePositionSql = "UPDATE employees SET position=? WHERE emp_id=?";
		PreparedStatement pstmt = con.prepareStatement(updatePositionSql);
		pstmt.setString(1, "lead developer update");
		pstmt.setInt(2, 1);



		String updateSalarySql = "UPDATE employees SET salary=? WHERE emp_id=?";
		PreparedStatement pstmt2 = con.prepareStatement(updateSalarySql);
		pstmt2.setDouble(1, 4000);
		pstmt2.setDouble(2, 1);

		boolean autoCommit = con.getAutoCommit();
		String position = "lead developer";
		try {
			con.setAutoCommit(false);
			pstmt.executeUpdate();
			pstmt2.executeUpdate();
			con.commit();
		} catch (SQLException exc) {
			exc.printStackTrace();
			con.rollback();
		} finally {
			con.setAutoCommit(autoCommit);
		}

		String updatePosition = "";
		try{
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from employees where emp_id=1");
			if(resultSet.next())
				updatePosition = resultSet.getString("position");
			System.out.println(updatePosition);
		}catch (Exception e){
			System.out.println(e.getMessage());
		}


		// THEN
		Assert.assertNotEquals(updatePosition,position);


	}




	public Long getTimeReuseConnection() throws ClassNotFoundException, SQLException {
		long startTime = System.nanoTime();

		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager
				.getConnection("jdbc:mysql://localhost:3306/myDB", "user1", "password");

		String querySql ="select * from employees where emp_id=1";
		Statement statement = con.createStatement();
		statement.execute(querySql);

		querySql ="select * from employees where emp_id=2";
		statement = con.createStatement();
		statement.execute(querySql);


		long endTime = System.nanoTime();
		con.close();
		return  (endTime - startTime);
	}

	public Long getTimeRecreateConnection() throws ClassNotFoundException, SQLException {
		long startTime = System.nanoTime();

		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager
				.getConnection("jdbc:mysql://localhost:3306/myDB", "user1", "password");

		String querySql ="select * from employees where emp_id=1";
		Statement statement = con.createStatement();
		statement.execute(querySql);

		con.close();


		con = DriverManager
				.getConnection("jdbc:mysql://localhost:3306/myDB", "user1", "password");

		querySql ="select * from employees where emp_id=2";
		statement = con.createStatement();
		statement.execute(querySql);

		long endTime = System.nanoTime();
		con.close();

		return  (endTime - startTime);
	}

	@Test
	public void checkTimeReUseLessThanTimeReCreate() throws ClassNotFoundException, SQLException {
		// GIVEN
		long timeReuse = getTimeReuseConnection();
		long timeRecreate = getTimeRecreateConnection();

		// WHEN

		// THEN
		System.out.println(timeRecreate+ " "+timeReuse);
		Assert.assertTrue(timeReuse < timeRecreate);
	}



	String getQueryString(String name){
		String querySql = "Select * from employees where name="+name;
		return querySql;
	}

	Integer getNumberRowOfResultSet(ResultSet resultSet) throws SQLException {
		int size =0;
		if (resultSet != null)
		{
			resultSet.last();    // moves cursor to the last row
			size = resultSet.getRow(); // get row id
		}
		return size;
	}

	@Test
	public void checkSQLInjectionWithStatement() throws ClassNotFoundException, SQLException {
		// GIVEN
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager
				.getConnection("jdbc:mysql://localhost:3306/myDB", "user1", "password");

		Statement stmt = con.createStatement();

		// WHEN
		String querySql = getQueryString("'jb' or 1=1");
		ResultSet resultSet = stmt.executeQuery(querySql);
		Integer row = getNumberRowOfResultSet(resultSet);

		con.close();

		// THEN
		Assert.assertTrue(row>1);
	}

	@Test
	public void checkSQLInjectionWithPrepareStatementNoneArgument() throws ClassNotFoundException, SQLException {
		// GIVEN
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager
				.getConnection("jdbc:mysql://localhost:3306/myDB", "user1", "password");


		// WHEN
		String querySql = getQueryString("'jb' or 1=1");
		PreparedStatement preparedStatement = con.prepareStatement(querySql);
		ResultSet resultSet = preparedStatement.executeQuery();
		Integer row = getNumberRowOfResultSet(resultSet);
		con.close();

		// THEN
		Assert.assertTrue(row>1);
	}

	@Test
	public void checkSQLInjectionWithPrepareStatementWithArgument() throws ClassNotFoundException, SQLException {
		// GIVEN
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager
				.getConnection("jdbc:mysql://localhost:3306/myDB", "user1", "password");

		Integer row=0;
		// WHEN
		try{
			String querySql = getQueryString("?");
			PreparedStatement preparedStatement = con.prepareStatement(querySql);
			preparedStatement.setString(1,"jb");
			ResultSet resultSet =preparedStatement.executeQuery();
			row = getNumberRowOfResultSet(resultSet);

		}catch (Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		finally {
			try {
				con.close();
			} catch (Exception e) {

			}
		}

		// THEN
		Assert.assertTrue(row==1);
	}


}
