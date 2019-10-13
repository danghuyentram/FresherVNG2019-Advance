package com.example.demojdbcconnectdb;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTimeReUseLessThanTimeReCreate {

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
        try {
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        try {
                con.close();
            } catch (Exception e) {
            e.printStackTrace();
        }


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

}
