package com.example.demojdbcconnectdb;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSQLInjection {
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
        Integer row = 0;
        try( Connection con = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/myDB", "user1", "password");){
            // WHEN
            try(Statement stmt = con.createStatement();){
                String querySql = getQueryString("'jb' or 1=1");
                ResultSet resultSet = stmt.executeQuery(querySql);
                row = getNumberRowOfResultSet(resultSet);

            }catch (SQLException e){
                e.printStackTrace();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }


        // THEN
        Assert.assertTrue(row>1);
    }

    @Test
    public void checkSQLInjectionWithPrepareStatementNoneArgument() throws ClassNotFoundException, SQLException {
        // GIVEN
        Class.forName("com.mysql.cj.jdbc.Driver");
        Integer row = 0;
        try(Connection con = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/myDB", "user1", "password");){

            // WHEN
            String querySql = getQueryString("'jb' or 1=1");
            try(PreparedStatement preparedStatement = con.prepareStatement(querySql);){
                ResultSet resultSet = preparedStatement.executeQuery();
                row = getNumberRowOfResultSet(resultSet);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }


        // THEN
        Assert.assertTrue(row>1);
    }

    @Test
    public void checkSQLInjectionWithPrepareStatementWithArgument() throws ClassNotFoundException, SQLException {
        // GIVEN
        Class.forName("com.mysql.cj.jdbc.Driver");
        Integer row=0;

        try(Connection con = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/myDB", "user1", "password");
        ){

            // WHEN
            String querySql = getQueryString("?");
            try(PreparedStatement preparedStatement = con.prepareStatement(querySql);
            ){
                preparedStatement.setString(1,"jb");
                ResultSet resultSet =preparedStatement.executeQuery();
                row = getNumberRowOfResultSet(resultSet);
            }catch (SQLException e){
                e.printStackTrace();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        // THEN
        Assert.assertTrue(row==1);
    }

}
