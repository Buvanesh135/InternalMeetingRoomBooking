package com.divum.MeetingRoomBlocker.Config;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {
    @Value("${aws.s3.credentials.access}")
    private String awsToken;
    @Value("${aws.s3.credentials.secret}")
    private String awsSecret;
    @Value("${aws.s3.credentials.bucket.name}")
    private String bucketName;
    private AWSCredentials getAwsCredentials(){
        AWSCredentials awsCredentials = new BasicAWSCredentials(
               awsToken,
               awsSecret
        );
        return awsCredentials;
    }
    @Bean
    public AmazonS3 awsS3ClientBuilder(){
        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(getAwsCredentials()))
                .withRegion(Regions.AP_SOUTH_1)
                .build();
        if(!s3Client.doesBucketExistV2(bucketName)) {
            s3Client.createBucket(bucketName);
        }
        return s3Client;
    }
}
