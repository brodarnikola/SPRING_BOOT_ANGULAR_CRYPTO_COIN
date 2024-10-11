package com.testcoin.demo.controller;

import com.testcoin.demo.model.TUser;
import com.testcoin.demo.repository.TUserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/coin")
public class UserController {

    private final TUserRepository tUserRepository;

    public UserController(TUserRepository tUserRepositoryParam) {
        tUserRepository = tUserRepositoryParam;
    }

    @GetMapping("/users")
    public ResponseEntity<List<TUser>> getAllUsers() {
        List<TUser> users = tUserRepository.findAll();  
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<TUser> getUser(@PathVariable int id) {
        Optional<TUser> user = tUserRepository.findById(id); // Retrieve from database
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}