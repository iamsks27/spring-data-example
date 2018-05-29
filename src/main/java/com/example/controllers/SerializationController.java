package com.example.controllers;

import com.example.models.UserDao;
import com.example.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/serialize")
public class SerializationController {

    private final UserService userService;

    public SerializationController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String save(@RequestBody final UserDao userDao) {
        System.out.println(userDao);
        this.userService.persist(userDao);
        return "user is converted and saved...!!!!";
    }

    @GetMapping
    public List<UserDao> getUsers() {
        return this.userService.getAllUsers();
    }
}
