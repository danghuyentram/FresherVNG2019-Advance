package com.example.demospringdatajpa.controller;

import com.example.demospringdatajpa.entity.QUser;
import com.example.demospringdatajpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.demospringdatajpa.entity.User;
import org.springframework.web.bind.annotation.ResponseBody;


import java.awt.print.Pageable;
import java.util.Optional;

//@Controller
//@RequestMapping("/user")
//public class UserController {
//    @Autowired
//    UserRepository userRepository;
//
//
//    @RequestMapping("/{id}")
//    public @ResponseBody ResponseEntity<User> findUserById(@PathVariable("id") Long id){
//        Optional<User> user = userRepository.findById(id);
//        System.out.println(user.toString());
//        return ResponseEntity.accepted().body(user.get());
//    }
//
//
//}
