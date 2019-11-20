package com.example.demospringdatajpa;

import com.example.demospringdatajpa.entity.*;
import com.example.demospringdatajpa.repository.DepartmentRepository;
import com.example.demospringdatajpa.repository.RoleRepository;
import com.example.demospringdatajpa.repository.UserRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
class DemoSpringDataJpaApplicationTests {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	EntityManager entityManager;

	QUser qUser= QUser.user;
	QDepartment qDepartment = QDepartment.department;
	QRole qRole = QRole.role;

	@Test
	void testFindUsersHaveMoreThanOneDepartments(){
//		JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
//		List<Tuple> users = jpaQueryFactory.select(qUser.firstname,qDepartment.count().as("count") )
//									.from(qDepartment.department)
//									.groupBy(qUser.id)
//									.having(qDepartment.count().gt(1))
//									.fetch();

		BooleanExpression booleanExpression = qUser.departments.size().gt(0);
		OrderSpecifier<Integer> orderSpecifier = qUser.departments.size().asc();
		Iterable<User> users = userRepository.findAll(booleanExpression,orderSpecifier);

		System.out.println("User have more than one department "+users);
//		Assert.assertTrue(users.size()==1);
	}


	@Test
	void testFindUserPaging(){
		Page<User> users = userRepository.findAll(PageRequest.of(0,4));
		System.out.println("Users in page 0 "+users.toList().toString());

		users.nextPageable();
		System.out.println("Users in page 1 "+users.toList().toString());

		System.out.println("Total page: "+users.getTotalPages());
	}

	@Test
	void testFindUserByFirstnameAndLastname(){
		BooleanExpression booleanExpression = qUser.firstname.eq("Im").or(qUser.lastname.eq("YoungJae"));
		Iterable<User> users = userRepository.findAll(booleanExpression);

		System.out.println("user "+users);
	}

	@Test
	void testLazyRole(){
		BooleanExpression booleanExpression = qRole.users.size().gt(1);
		Iterable<Role> roles = roleRepository.findAll(booleanExpression);

		System.out.println("roles: "+roles);

	}

	@Test
	void test(){
		System.out.println("abc"+userRepository.findByDepartments_Id(1));
//		System.out.println("count user "+userRepository.countByDepartmentsAbcGreaterThan((long)1));
//		System.out.println(roleRepository.findById((long)1));

	}






}
