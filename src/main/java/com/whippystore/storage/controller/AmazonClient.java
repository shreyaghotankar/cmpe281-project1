package com.whippystore.storage.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class AmazonClient {

    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;
    
    private static final String delimiter = "/";

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_WEST_2).build();
    }

    public String uploadFile(MultipartFile multipartFile) {

        String fileUrl = "";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = ((OAuth2User)auth.getPrincipal()).getAttribute("email");
        System.out.println("user: " + username);
        
        //check if username folder exists
        try {
        	if (!checkFolder(bucketName, username)) {
        		createFolder(bucketName, username);
        	}
        }catch (AmazonServiceException e){
        	System.out.println("Error" + e);
        	//catch amazonclientexception later
        }
        
        //Using Multipart upload
        try {
            File file = convertMultiPartToFile(multipartFile);
            
            fileUrl = username + "/"+ multipartFile.getOriginalFilename();
            uploadFileTos3bucket(multipartFile.getOriginalFilename(), username, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    //converting file into multipart for better upload performance
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    //calling putobject of S3
    private void uploadFileTos3bucket(String fileName, String folderName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, folderName + delimiter +  fileName, file));
    }
    
    //Creating folder with username
    public void createFolder(String bucketName, String folderName) {
        // create meta-data for your folder and set content-length to 0
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        // create empty content
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
        // create a PutObjectRequest passing the folder name suffixed by /
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
                folderName + delimiter, emptyContent, metadata);
        // send request to S3 to create folder
        s3client.putObject(putObjectRequest);
    }
    
    //checking if folder exists
    private boolean checkFolder(String bucketName, String folderName) {
    	return s3client.doesObjectExist(bucketName, folderName + delimiter);
    	
    }
    
    public void deleteFile(String path) {
    	s3client.deleteObject(new DeleteObjectRequest(bucketName, path));
    }
}
