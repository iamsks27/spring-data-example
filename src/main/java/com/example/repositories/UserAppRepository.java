package com.example.repositories;

import com.example.models.UserApp;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface UserAppRepository extends CrudRepository<UserApp, String> {
    UserApp findByHiveid(String id);

    List<UserApp> findByPackagename(String s);
}
