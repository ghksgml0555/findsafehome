package com.swyg.findingahomesafely.dto.realestateDto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResRealEstatePolicyLetter {

    private Long id;

    private String title;

    private String thumbnailImgUrl;

    private String contentImgUrl;

    private String author;

    private LocalDateTime modifiedAt;

    @Builder
    public ResRealEstatePolicyLetter(Long id, String title, String thumbnailImgUrl, String contentImgUrl, String author, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.thumbnailImgUrl = thumbnailImgUrl;
        this.contentImgUrl = contentImgUrl;
        this.author = author;
        this.modifiedAt = modifiedAt;
    }
}
