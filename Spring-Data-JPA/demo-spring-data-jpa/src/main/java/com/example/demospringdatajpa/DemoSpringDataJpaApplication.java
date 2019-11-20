package com.example.demospringdatajpa;

import com.example.demospringdatajpa.entity.*;
import com.example.demospringdatajpa.repository.DepartmentRepository;
import com.example.demospringdatajpa.repository.RoleRepository;
import com.example.demospringdatajpa.repository.UserRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;


@SpringBootApplication
public class DemoSpringDataJpaApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

//	private QUser qUser = QUser.user;

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringDataJpaApplication.class, args);

	}


	public List<Role> createRoles(){
		List<Role> roles = Arrays.asList(
			new Role(1,"Main vocal"),
				new Role(2,"Main dancer"),
				new Role(3,"Lead vocal"),
				new Role(4,"Lead dancer"),
				new Role(5,"Rapper")


		);

		List<User> users = Arrays.asList(
				new User(1,"Im","Jaebum",new Date(),26,1),
				new User(5,"Choi","YoungJae",new Date(),24,1),
				new User(7,"Kim","YuGyeom",new Date(),23,1),
				new User(3,"Park","JinYoung",new Date(),26,1),
				new User(4,"Wang","Jackson",new Date(),26,1),
				new User(6,"Bam","Bam",new Date(),23,1),
				new User(2,"Tuan","Mark",new Date(),27,1)
		);

		List<Department> departments = Arrays.asList(
				new Department(1,"Seoul, Korean"),
				new Department(2,"Incheon, Korean"),
				new Department(3,"Mokpo, Korean"),
				new Department(4,"HongKong"),
				new Department(5,"ThaiLand"),
				new Department(6,"NewYork"),
				new Department(7,"VietNam"),
				new Department(8,"Chineses")
		);

		users.get(0).addDepartment(departments.get(0));
		users.get(1).addDepartment(departments.get(1));
		users.get(2).addDepartment(departments.get(2));
		users.get(3).addDepartment(departments.get(3));
		users.get(4).addDepartment(departments.get(4));
		users.get(5).addDepartment(departments.get(5));
		users.get(6).addDepartment(departments.get(6));
		users.get(6).addDepartment(departments.get(7));


		roles.get(0).addUser(users.get(0));
		roles.get(0).addUser(users.get(1));
		roles.get(1).addUser(users.get(2));
		roles.get(2).addUser(users.get(3));
		roles.get(3).addUser(users.get(4));
		roles.get(3).addUser(users.get(5));
		roles.get(4).addUser(users.get(6));

		roles.forEach(role -> roleRepository.save(role));

		return roles;
	}


	@Override
	public void run(String... args) throws Exception {
		System.out.println("-------------------------------------:: List user " );
		List<Role> roles = createRoles();

//
//		System.out.println("-------------------------------------:: " );

//		BooleanExpression booleanExpression = QUser.user.active.eq(1).and(QUser.user.age.gt(24));
//		Iterable<User> users = userRepository.findAll(booleanExpression);
//		users.forEach(System.out::println);
//		Iterable<User> users = userRepository.findAll();
//		users.forEach(user -> System.out.println(user.toString()));

//		Iterable<Role> roles1 = roleRepository.findAll();
//		roles1.forEach(role -> System.out.println(role.toString()));
//
//		System.out.println("-------------------------------------:: " );
//
//
//		BooleanExpression booleanExpression = QRole.role.users.any().age.lt(26);
//		Iterable<Role> roles2 = roleRepository.findAll(booleanExpression);
//		roles2.forEach(role -> System.out.println(role.toString()));
//
//		System.out.println("-------------------------------------:: " );


//		BooleanExpression booleanExpression1 = QRole.role.users.any().departments.any().countDistinct().lt(2);
//		Iterable<Role> roles3 = roleRepository.findAll(booleanExpression1);
//		roles3.forEach(role -> System.out.println(role.toString()));

//		BooleanExpression booleanExpression = QUser.user.count().gt(2);
//		Iterable<User> users = userRepository.findAll(booleanExpression);
//		users.forEach(user -> System.out.println(user.toString()));

//		Department department = new Department(1,"Seoul, Korean");
//		System.out.println("-------------------------------------:: count " );
//		System.out.println(departmentRepository.findById(1));
//


	}
}