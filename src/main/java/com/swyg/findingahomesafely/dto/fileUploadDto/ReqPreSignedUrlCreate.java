package com.swyg.findingahomesafely.dto.fileUploadDto;


import lombok.Getter;

@Getter
public class ReqPreSignedUrlCreate {

    private String key;
    private String uploadId;
    private String partNumber;

}
