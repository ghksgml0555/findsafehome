package com.swyg.findingahomesafely.dto.realestateDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReqRealEstateNotice {

    private String thumbnailImgUrl;

    private String contentImgUrl;

    @Builder
    public ReqRealEstateNotice(String thumbnailImgUrl, String contentImgUrl) {
        this.thumbnailImgUrl = thumbnailImgUrl;
        this.contentImgUrl = contentImgUrl;
    }

}
