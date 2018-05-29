package com.example.repositories;

import com.example.models.Greeting;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

@EnableScan
public interface GreetingRepository extends CrudRepository<Greeting, String> {

    List<Greeting> findAll();

    List<Greeting> findByCreateDateGreaterThanEqual(Date date);
}
