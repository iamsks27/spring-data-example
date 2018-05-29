package com.example.controllers;

import com.example.models.Greeting;
import com.example.models.GreetingModified;
import com.example.services.GreetingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/greet")
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/create")
    public void createTable() throws InterruptedException {
        greetingService.createTable();
    }

    @PostMapping("/insert")
    public Greeting saveGreeting(@RequestBody final Greeting greeting) {
        return greetingService.insertNewGreeting(greeting);
    }

    @GetMapping("/all")
    public List<Greeting> getAllGreetings() {
        return greetingService.getAllGreetings();
    }

    @GetMapping("/all/lastThirtyDays")
    public List<Greeting> getGreetingInThirtyDays() {
        return greetingService.getAllGreetingInLastThirtyDays();
    }

    @GetMapping("/all/sort")
    public List<Greeting> getAllGreetingSorted() {
        return greetingService.getAllGreetingSorted();
    }

    @GetMapping("/insert/_bulk")
    public void bulkInsert() {
        greetingService.insetInBulk();
    }

    @GetMapping("/update/greeting")
    public void updateGreeting() {
        greetingService.updateGreeting();
    }

    @GetMapping("insert/greetingModified/_bulk")
    public void bulkInsertIntoGreetingModified() {
        greetingService.insertNewGreetingModified();
    }

    @GetMapping("/greetingModified/sort")
    public List<GreetingModified> getModifiedGreeting() {
        return greetingService.getAllGreetingModifiedSorted();
    }

    @GetMapping("/greetingModified")
    public Iterable<GreetingModified> getGreetingModified() {
        return greetingService.getAllGreetingModified();
    }

    @PostMapping("/test")
    public void test(@RequestBody final Map<String, String> test) {
        System.out.println(test);
    }
}
