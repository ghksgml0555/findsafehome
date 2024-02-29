package com.swyg.findingahomesafely.dto.realestateDto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResRealEstateNotice {

    private String thumbnailImgUrl;

    private String contentImgUrl;

    private String useYn;

    private LocalDateTime lastChngRegDttm;

    @Builder
    public ResRealEstateNotice(String thumbnailImgUrl, String contentImgUrl, String useYn, LocalDateTime lastChngRegDttm) {
        this.thumbnailImgUrl = thumbnailImgUrl;
        this.contentImgUrl = contentImgUrl;
        this.useYn = useYn;
        this.lastChngRegDttm = lastChngRegDttm;
    }

}
