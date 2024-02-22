package com.swyg.findingahomesafely.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.swyg.findingahomesafely.common.response.ResponseResult;
import com.swyg.findingahomesafely.config.S3Config;
import com.swyg.findingahomesafely.dto.FileUploadDto.ReqPreSignedUploadInitiate;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.ref.PhantomReference;
import java.net.URLConnection;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final AmazonS3 amazonS3Client;

    @PostMapping("/initiate-upload")
    public ResponseResult<?> initiateUpload(@RequestBody ReqPreSignedUploadInitiate reqPreSignedUploadInitiate){

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(reqPreSignedUploadInitiate.getFileSize());
        objectMetadata.setContentType(URLConnection.guessContentTypeFromName(reqPreSignedUploadInitiate.getFileType()));

        System.out.println("===================================");
        System.out.println(reqPreSignedUploadInitiate.getOriginalFileName());
        System.out.println("===================================");

        InitiateMultipartUploadResult initiateMultipartUploadResult = amazonS3Client.initiateMultipartUpload(
                new InitiateMultipartUploadRequest("swyg-bucket", reqPreSignedUploadInitiate.getOriginalFileName(), objectMetadata)
        );

//        GeneratePresignedUrlRequest generatePresignedUrlRequest =
//                new GeneratePresignedUrlRequest(bucket, fileName)
//                        .withMethod(HttpMethod.GET)
//                        .withExpiration(expiration);
//
//        amazonS3Client.generatePresignedUrl();

        return ResponseResult.body(initiateMultipartUploadResult);
    }

}
