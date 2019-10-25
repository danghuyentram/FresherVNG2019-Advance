package com.example.demopropagation.daoimpl;

import com.example.demopropagation.dao.EmployeeDao;
import com.example.demopropagation.model.Employee;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Repository
public class EmployeeDaoImpl extends JdbcDaoSupport implements EmployeeDao {

//    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize(){
        setDataSource(dataSource);
    }

    @Override
    public void insertEmployee(Employee employee) {

    }

    @Override
    public void deleteEmployeeById(String empid) {

    }
}
