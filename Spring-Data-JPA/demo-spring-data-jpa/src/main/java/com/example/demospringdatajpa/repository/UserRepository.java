package com.example.demospringdatajpa.repository;

import com.example.demospringdatajpa.entity.Department;
import com.example.demospringdatajpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    Optional<User> findByLastnameAndFirstname(String firstname, String lastname);

    Optional<User> findByLastnameOrFirstname(String firstname, String lastname);

    List<User> findByStartDateBetween(Date date1, Date date2);

    List<User> findByAgeLessThan(int age);

    List<User> findByAgeLessThanEqual(int age);

    List<User> findByAgeGreaterThan(int age);

    List<User> findByAgeGreaterThanEqual(int age);

    List<User> findByStartDateAfter(Date date);

    List<User> findByStartDateBefore(Date date);

    List<User> findByAgeIsNull();

    List<User> findByFirstnameLike(String firstname);

    List<User> findByFirstnameNotLike(String firstname);

    Optional<User> findByFirstnameStartingWith(String firstname);

    List<User> findByFirstnameEndingWith(String firstname);

    List<User> findByFirstnameContaining(String firstname);

    List<User> findByAgeOrderByLastnameDesc(int age);

    List<User> findByAgeOrderByLastnameAsc(int age);


    List<User> findByLastname(String lastname);

    List<User> findByLastnameNot(String lastname);

    List<User> findByLastnameContaining(String lastname);


    List<User> findByAgeIn(Collection<Integer> ages);

    List<User> findByAgeNotIn(Collection<Integer> ages);

    Stream<User> findByActiveIs(int active);


    List<User> findByFirstnameIgnoreCase(String firstname);

    List<User> findUserByDepartments(Department department);

    int countByActiveIs(int active);

    List<User> findByDepartments_Id(long id);

    int countByDepartmentsIdGreaterThan(long number);

//    int countByDepartmentsAbcGreaterThan(long number);


    @Query("select u from User u join u.departments d group by u.id having count(d.id)<?1")
    List<User> findUserHaveLessThan(long number);


}
