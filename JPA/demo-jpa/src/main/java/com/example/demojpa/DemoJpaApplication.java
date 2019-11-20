package com.example.demojpa;

import com.example.demojpa.entity.Department;
import com.example.demojpa.entity.Employee;
import com.example.demojpa.entity.NonTeachingStaff;
import com.example.demojpa.entity.TeachingStaff;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoJpaApplication {

	public static void main(String[] args) {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Employee_JPA");

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();


		// @OneToMany relation and @ManyToOne relation
//		// Create employee1 entity
//		Employee employee1 = new Employee();
//		employee1.setEname("JB");
//		employee1.setSalary(450000);
//		employee1.setDeg("Technical");
//
//		// Create employee2 entity
//		Employee employee2 = new Employee();
//		employee2.setEname("Jackson");
//		employee2.setSalary(450000);
//		employee2.setDeg("Technical");
//
//		// Create employee3 entity
//		Employee employee3 = new Employee();
//		employee3.setEname("JinYoung");
//		employee3.setSalary(450000);
//		employee3.setDeg("Technical");
//
//		// Store employee
//		entityManager.persist(employee1);
//		entityManager.persist(employee2);
//		entityManager.persist(employee3);
//
//		// Create EmployeeList
//		List<Employee> employeeList = new ArrayList();
//		employeeList.add(employee1);
//		employeeList.add(employee2);
//		employeeList.add(employee3);
//
//
//		// Create department Entity
//		Department department = new Department();
//		department.setName("Dev");
//		department.setEmployeeList(employeeList);
//
//		// Store department
//		entityManager.persist(department);


		// @OneToOne relation
 		// Create department Entity
		Department department = new Department();
		department.setName("Dev");

		// Store department
		entityManager.persist(department);

		// Create employee entity
		Employee employee = new Employee();
		employee.setEname("JB");
		employee.setSalary(40000);
		employee.setDeg("Technical");
		employee.setDepartment(department);

		// Create employee3 entity
		Employee employee3 = new Employee();
		employee3.setEname("JinYoung");
		employee3.setSalary(450000);
		employee3.setDeg("Technical");
		System.out.println("id employee "+employee3.getEid()+" "+employee.getEid());
		System.out.println("id department "+department.getId());

		// Store employee
		entityManager.persist(employee3);
		entityManager.persist(employee);

		// Named query
//		Query query = entityManager.createNamedQuery("find employee by id");
//
//		query.setParameter("id", 1);
//		List<Employee> list = query.getResultList( );
//
//		for( Employee e:list ){
//			System.out.print("Employee ID :" + e.getEid( ));
//			System.out.println("\t Employee Name :" + e.getEname( ));
//		}

		//Teaching staff entity
//		TeachingStaff ts1=new TeachingStaff("Gopal","MSc MEd","Maths");
//		TeachingStaff ts2=new TeachingStaff( "Manisha", "BSc BEd", "English");
//
//		//Non-Teaching Staff entity
//		NonTeachingStaff nts1=new NonTeachingStaff( "Satish", "Accounts");
//		NonTeachingStaff nts2=new NonTeachingStaff( "Krishna", "Office Admin");
//
//		//storing all entities
//		entityManager.persist(ts1);
//		entityManager.persist(ts2);
//		entityManager.persist(nts1);
//		entityManager.persist(nts2);

		entityManager.getTransaction().commit();


		// Criteria API
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object> criteriaQuery = criteriaBuilder.createQuery();
		Root<Employee> from = criteriaQuery.from(Employee.class);

		// select all records
		System.out.println("Select all records");
		CriteriaQuery<Object> select = criteriaQuery.select(from);
		TypedQuery<Object> typedQuery = entityManager.createQuery(select);
		List<Object> resultList = typedQuery.getResultList();

		for(Object o:resultList){
			Employee e = (Employee) o;
			System.out.println("EID a: "+e.getEid()+" Ename: "+e.getEname());
		}

		// Ordering the records
		System.out.println("Select all records y follow ordering");
		CriteriaQuery<Object> select1 = criteriaQuery.select(from);
		select1.orderBy(criteriaBuilder.asc(from.get("ename")));
		TypedQuery<Object> typedQuery1 = entityManager.createQuery(select1);
		List<Object> resultlist1 = typedQuery1.getResultList();

		for(Object o:resultlist1){
			Employee e = (Employee) o;
			System.out.println("EID b : " + e.getEid() + " Ename : " + e.getEname());
		}


		entityManager.close();
		entityManagerFactory.close();
	}

}
