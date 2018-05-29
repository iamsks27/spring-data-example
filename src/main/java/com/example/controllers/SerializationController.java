package com.example.controllers;

import com.example.models.User;
import com.example.models.UserDao;
import com.example.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/serialize")
public class SerializationController {

    private final UserService userService;

    public SerializationController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDao save(@RequestBody final UserDao userDao) {
        this.userService.persist(userDao);
        return userDao;
    }
}
