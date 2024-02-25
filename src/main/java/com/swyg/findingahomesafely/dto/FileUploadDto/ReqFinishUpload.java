package com.swyg.findingahomesafely.dto.FileUploadDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReqFinishUpload {

    private String uploadId;
    private List<Part> parts;

    @Getter
    @Setter
    public static class Part{
        private int partNumber;
        private String eTag;
    }

}
