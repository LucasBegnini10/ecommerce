package com.server.ecommerce.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class S3Service {

    private final AmazonS3 s3Client;

    private final String bucketName = System.getenv("BUCKET_NAME_AWS");

    @Autowired
    public S3Service(AmazonS3 amazonS3){
        this.s3Client = amazonS3;
    }

    public void save(String keyName, File file){
        this.s3Client.putObject(new PutObjectRequest(bucketName, keyName, file));
    }

    public void delete(String keyName){
        this.s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
    }
}
