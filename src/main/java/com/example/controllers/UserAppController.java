package com.example.controllers;

import com.example.models.UserApp;
import com.example.services.UserAppService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userapp")
public class UserAppController {

    private final UserAppService appService;

    public UserAppController(final UserAppService appService) {
        this.appService = appService;
    }

    @GetMapping("/test")
    public String test() {
        return "Test";
    }

    @GetMapping("/count")
    public Integer getAllCount() {
        appService.getAllUserApp();
        return 0;
    }

    @GetMapping("/{id}")
    public UserApp getUserAppByHiveId(@PathVariable final String id) {
        return appService.getUserAppByHiveId(id);
    }

    @GetMapping("/package")
    public List<UserApp> getUserAppByPackages() {
        System.out.println("Getting UserDao App using packages");
        return appService.getUserAppsUsingPackage("appwala.myphoto.keyboard.gallery");
    }
}
