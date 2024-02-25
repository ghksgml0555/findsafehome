package com.swyg.findingahomesafely.controller;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.swyg.findingahomesafely.common.response.ResponseResult;
import com.swyg.findingahomesafely.dto.FileUploadDto.ReqFinishUpload;
import com.swyg.findingahomesafely.dto.FileUploadDto.ReqPartUpload;
import com.swyg.findingahomesafely.dto.FileUploadDto.ReqPreSignedUploadInitiate;
import com.swyg.findingahomesafely.dto.FileUploadDto.ReqPreSignedUrlCreate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private final AmazonS3 amazonS3Client;
    private final String bucket = "swyg-bucket";
    private final String S3FilePath = "test/";

    @PostMapping("/initiate-upload")
    public ResponseResult<?> initiateUpload(@RequestBody ReqPreSignedUploadInitiate reqPreSignedUploadInitiate){

        String objectKey = S3FilePath + reqPreSignedUploadInitiate.getOriginalFileName();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(reqPreSignedUploadInitiate.getFileSize());
        objectMetadata.setContentType(URLConnection.guessContentTypeFromName(reqPreSignedUploadInitiate.getFileType()));

        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucket,objectKey,objectMetadata);

        System.out.println(objectKey);
        System.out.println(objectMetadata.getContentType());

        return ResponseResult.body(amazonS3Client.initiateMultipartUpload(initiateMultipartUploadRequest));
    }


    @PostMapping("/presigned-url")
    public ResponseResult<?> initiateUpload(@RequestBody ReqPreSignedUrlCreate reqPreSignedUrlCreateRequest){
        String objectKey = reqPreSignedUrlCreateRequest.getKey();

        Date expirationTime  = Date.from(
                LocalDateTime.now().plusMinutes(10).atZone(ZoneId.systemDefault()).toInstant()
        );

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, objectKey)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expirationTime);

        generatePresignedUrlRequest.addRequestParameter("uploadId", reqPreSignedUrlCreateRequest.getUploadId());
        generatePresignedUrlRequest.addRequestParameter("partNumber", reqPreSignedUrlCreateRequest.getPartNumber());

        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return ResponseResult.body(url);
    }

    @PostMapping("/complete-upload")
    public ResponseResult<?> initiateUpload(@RequestBody ReqFinishUpload reqFinishUpload){
        String objectKey = reqFinishUpload.getKey();

        List<PartETag> partETags = reqFinishUpload.getParts().stream().map(
                item -> new PartETag(item.getPartNumber(),item.getETag())
        ).toList();

        CompleteMultipartUploadRequest completeMultipartUploadRequest
                = new CompleteMultipartUploadRequest(bucket, objectKey, reqFinishUpload.getUploadId(),partETags);

        CompleteMultipartUploadResult completeMultipartUploadResult = amazonS3Client.completeMultipartUpload(completeMultipartUploadRequest);

        /**
         *
         */
//        ReqFinishUpload reqFinishUpload1 = new ReqFinishUpload();
//        reqFinishUpload1.setUploadId("asd");
//
//        List<ReqFinishUpload.Part> parts = new ArrayList<>();
//
//
//        ReqFinishUpload.Part part = new ReqFinishUpload.Part();
//        part.setPartNumber(1);
//        part.setETag("asd");
//
//        parts.add(part);
//
//        reqFinishUpload1.setParts(parts);
        System.out.println(completeMultipartUploadResult.getETag());

        return ResponseResult.body(completeMultipartUploadResult);
    }

    @PostMapping("/file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("url") String url ) {
        try {
            String fileName = file.getOriginalFilename();
            String fileUrl = url + "nsw";
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
