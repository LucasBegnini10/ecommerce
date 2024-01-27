package com.server.ecommerce.file;

import com.server.ecommerce.aws.s3.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileService {

    private final S3Service s3Service;

    @Autowired
    public FileService(S3Service s3Service){
        this.s3Service = s3Service;
    }

    public void save(String key, File file){
        this.s3Service.save(key, file);
    }

    public void delete(String key){
        this.s3Service.delete(key);
    }
}
