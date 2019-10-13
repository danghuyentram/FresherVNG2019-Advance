package com.example.demojdbcconnectdb;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRollBack {

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
        pstmt.setDouble(1, 4000);
        pstmt2.setDouble(2, 1);

        // WHEN

        boolean autoCommit = con.getAutoCommit();
        String position = "lead developer";
        try {
            con.setAutoCommit(false);
            pstmt2.executeUpdate();
            pstmt.executeUpdate();

            con.commit();
        } catch (SQLException exc) {
            exc.printStackTrace();
            System.out.println("rollback");
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
        }catch (Exception e){
            System.out.println(e.getMessage());
        } finally
        {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }




        // THEN
        Assert.assertEquals(updatePosition,position);


    }

}
