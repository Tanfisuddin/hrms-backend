package com.imw.admin.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;


import java.net.URI;
import java.net.URISyntaxException;

@Getter
@Configuration
public class S3Config {
    @Value("${vultr.endpoint}")
    private String endpoint;

    @Value("${vultr.accessKey}")
    private String accessKey;

    @Value("${vultr.secretKey}")
    private String secretKey;

    @Value("${vultr.bucketName}")
    private String bucketName;

    @Bean
    public S3Client s3Client() throws URISyntaxException {
        return S3Client.builder()
                .region(Region.AP_EAST_1)
                .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey))
                .endpointOverride(new URI(endpoint))
                .build();
    }
}
