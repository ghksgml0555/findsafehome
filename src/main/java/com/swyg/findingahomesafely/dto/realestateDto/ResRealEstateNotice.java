package com.swyg.findingahomesafely.dto.realestateDto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResRealEstateNotice {

    private String thumbnailImgUrl;

    private String contentImgUrl;

    @Builder
    public ResRealEstateNotice(String thumbnailImgUrl, String contentImgUrl) {
        this.thumbnailImgUrl = thumbnailImgUrl;
        this.contentImgUrl = contentImgUrl;
    }

}
