package com.swyg.findingahomesafely.dto.FileUploadDto;


import lombok.Getter;

@Getter
public class ReqPreSignedUrlCreate {

    private String key;
    private String uploadId;
    private String partNumber;

}
