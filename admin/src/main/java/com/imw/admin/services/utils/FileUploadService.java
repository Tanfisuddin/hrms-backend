package com.imw.admin.services.utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.imw.admin.config.S3Config;
import com.imw.admin.security.UserDetailsImpl;
import com.imw.commonmodule.dto.ResponseDTO;
import com.imw.commonmodule.persistence.UserFile;
import com.imw.commonmodule.repository.UserFileRepository;
import com.imw.commonmodule.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;


@Service
public class FileUploadService {

    @Autowired
    private S3Config s3Config;

    @Autowired
    UserFileRepository userFileRepository;

    @Autowired
    UserRepository userRepository;

    private Logger log = LoggerFactory.getLogger(FileUploadService.class);

    public ResponseDTO fileUpload(UserDetailsImpl currentUser, MultipartFile file) {
        ResponseDTO responseDTO = new ResponseDTO();
        try{
            if (file==null || file.isEmpty()) {
                responseDTO.setSuccess(false);
                responseDTO.setMessage("File is empty");
                return responseDTO;
            }
            String fileName = file.getOriginalFilename().replaceAll(" ", "-");
            String uploadFileName = UUID.randomUUID() + "-" + currentUser.getOrganization().getId() + "-" + fileName;
            String uploadFileURL = s3Config.getEndpoint() + "/" + s3Config.getBucketName() + "/" + uploadFileName;

            byte[] bytes = file.getBytes();
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(s3Config.getAccessKey(), s3Config.getSecretKey());
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3Config.getEndpoint(), null))
                    .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            InputStream inputStream = new ByteArrayInputStream(bytes);
            PutObjectRequest request = new PutObjectRequest(s3Config.getBucketName(), uploadFileName, inputStream, metadata);

            AccessControlList accessControlList = new AccessControlList();
            accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);
            request.setAccessControlList(accessControlList);
            s3Client.putObject(request);

            UserFile userFile = new UserFile();
            userFile.setUser(userRepository.getReferenceById(currentUser.getId()));
            userFile.setFileUrl(uploadFileURL);
            userFile.setOriginalFileName(file.getOriginalFilename());
            UserFile savedFile = userFileRepository.save(userFile);
            responseDTO.setData(savedFile);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("File uploaded successfully");
        }catch(Exception e){
            log.error("Error uploading file: {}", e.getMessage());
            responseDTO.setSuccess(false);
            responseDTO.setMessage("Error uploading file");
        }
        return responseDTO;
    }

}
