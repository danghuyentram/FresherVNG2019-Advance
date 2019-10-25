package com.example.demospringjdbc;

import com.example.demospringjdbc.model.Employee;
import com.example.demospringjdbc.model.ProfilingAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLException;


@SpringBootApplication
public class DemoSpringJdbcApplication implements CommandLineRunner {


	public static void main(String[] args) {

		SpringApplication.run(DemoSpringJdbcApplication.class, args);
	}

	public boolean checkConnection() throws SQLException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource("jdbc:mysql://localhost:3306/myDB", "user1", "password");


		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		jdbcTemplate.execute("SELECT COUNT(*) FROM employees");


		jdbcTemplate.getDataSource().getConnection().close();
		if(	jdbcTemplate.getDataSource().getConnection()!=null)
			return true;
		return false;
	}

	@Override
	public void run(String... args) throws Exception {

		System.out.println(checkConnection());

//		String sql = "select * from employees where emp_id = :emp_id";
//
//		SqlParameterSource namedParameters = new MapSqlParameterSource("emp_id", 1);
//		NamedParameterJdbcTemplate namedParameterJdbcTemplate= new NamedParameterJdbcTemplate(dataSource);
//
//
//		Employee employee = new Employee();
//		employee.setName("jb");
//
//		String selectSQL = "select * from employees where name= :name";
//
//
//		System.out.println(namedParameterJdbcTemplate.queryForObject(sql, namedParameters,Integer.class));


	}
}
