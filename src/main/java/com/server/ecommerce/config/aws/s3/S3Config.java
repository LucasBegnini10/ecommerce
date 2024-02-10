package com.server.ecommerce.config.aws.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Bean
    public AmazonS3 amazonS3(){
        BasicAWSCredentials credentials = new BasicAWSCredentials(System.getenv("ACCESS_KEY_AWS"), System.getenv("SECRET_KEY_AWS"));

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(System.getenv("REGION_AWS"))
                .build();
    }

}
