package com.example.Config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableDynamoDBRepositories(basePackages = {"com.example.repositories"})
public class DynamoDBConfig {

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {

        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new ProfileCredentialsProvider("bucket")).build();
    }

    @Bean
    public DynamoDB getDynamodb() {
        return new DynamoDB(amazonDynamoDB());
    }

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1)
                .withCredentials(new ProfileCredentialsProvider("bucket")).build();
    }
}
