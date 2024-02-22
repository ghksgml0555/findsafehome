package com.swyg.findingahomesafely.dto.FileUploadDto;

import lombok.Builder;
import lombok.Getter;

@Getter

public class ReqPreSignedUploadInitiate {

    private String originalFileName;
    private String fileType;
    private Long fileSize;

}
