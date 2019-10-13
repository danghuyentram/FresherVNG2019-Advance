package com.example.demojdbcconnectdb;


import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestNotCloseConnection {
    //	@Test
	public void checkConnectionClose() throws ClassNotFoundException, SQLException {
		// GIVEN

//	 	check and update max connection in mysql
//		SHOW VARIABLES LIKE "max_connections";
//		SET GLOBAL max_connections = 2;


		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager
				.getConnection("jdbc:mysql://localhost:3306/myDB", "user1", "password");


		// WHEN
		try{
			con = null;
			con = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/myDB", "user1", "password");

		}catch (SQLNonTransientConnectionException e){
			e.printStackTrace();
		}


		// THEN
		Assert.assertNull(con);
	}
}
