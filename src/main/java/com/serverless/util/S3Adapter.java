package com.serverless.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

public class S3Adapter {

    private static final String BUCKET_NAME = System.getenv("BUCKET_NAME");
    private static final String BUCKET_FOLDER_NAME = System.getenv("BUCKET_FOLDER_NAME");
    private static final int URL_EXPIRATION_TIME = 15 * 60; // 15 minutes
    private static final String REGION = System.getenv("REGION");

    public static URL uploadToS3(String msj){

        try {
            System.out.println("[S3-Client] Message Received: "+msj);

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(REGION)).build();

            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy-HHmmss");
            String formattedTime = currentTime.format(formatter);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> jsonMap = objectMapper.readValue(msj, Map.class);
            String fileName = "output-" + formattedTime + ".json";
            String objectKey = BUCKET_FOLDER_NAME + "/" +fileName;

            InputStream inputStream = new ByteArrayInputStream(objectMapper.writeValueAsBytes(jsonMap));

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(objectMapper.writeValueAsBytes(jsonMap).length);
            metadata.setContentType("application/json");

            s3Client.putObject(new PutObjectRequest(BUCKET_NAME, objectKey, inputStream, metadata));

            // pre-signed URL
            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(BUCKET_NAME, objectKey);
            urlRequest.setMethod(HttpMethod.GET);
            urlRequest.setExpiration(Date.from(Instant.now().plusSeconds(URL_EXPIRATION_TIME)));
            URL preSignedUrl = s3Client.generatePresignedUrl(urlRequest);

            System.out.println("[S3-Client] PRESIGNED URL: "+preSignedUrl);

        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return null;
    }

}
