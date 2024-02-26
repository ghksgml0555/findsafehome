package com.swyg.findingahomesafely.dto.fileUploadDto;

import lombok.Getter;

@Getter
public class ReqPreSignedUploadInitiate {

    private String originalFileName;
    private String fileType;
    private Long fileSize;

}
