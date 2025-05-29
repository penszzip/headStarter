package com.penszzip.headStarter.service;

import java.io.IOException;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

@Service
public class S3Service {

    @Value("${aws.access-key-id}")
    private String accessKeyId;

    @Value("${aws.secret-access-key}")
    private String secretAccessKey;

    private static final Regions CLIENT_REGION = Regions.US_EAST_2;

    private static final String BUCKET_NAME = "headstarter";

    private AmazonS3 getS3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        return AmazonS3ClientBuilder.standard()
            .withRegion(CLIENT_REGION)
            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
            .build();
    }

    public List<String> uploadFile(List<MultipartFile> images, String name) throws IOException { 
        try {
            AmazonS3 s3Client = getS3Client();
            List<String> urlList = new ArrayList<>();

            for (MultipartFile image : images) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(image.getSize());
                metadata.setContentType(image.getContentType());
        
                String imageId = name + "-" + UUID.randomUUID().toString(); // use project name and uuid as a key

                PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, imageId, image.getInputStream(), metadata);
                s3Client.putObject(request);
                urlList.add(s3Client.getUrl(BUCKET_NAME, imageId).toString());
            }
            return urlList;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw e;
        } 
    }
}
