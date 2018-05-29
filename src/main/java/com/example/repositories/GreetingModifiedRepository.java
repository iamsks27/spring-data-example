package com.example.repositories;

import com.example.models.GreetingModified;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

@EnableScan
public interface GreetingModifiedRepository extends CrudRepository<GreetingModified, String> {
    List<GreetingModified> findByRandomIdIn(List<String> randomId);

    List<GreetingModified> findByRandomId(String s);

    Set<GreetingModified> findByHiveidInAndCountLessThan(List<String> hiveIds, int count);

    List<GreetingModified> findByHiveidIn(List<String> hiveIds);
    //Set<GreetingModified> findByHiveIdInAndCountLessThan(Set<String> hiveIds, int count);
}
