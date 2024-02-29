package com.swyg.findingahomesafely.dto.realestateDto;

import com.swyg.findingahomesafely.dto.Paging;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ResRealEstatePolicyLetter extends Paging {

    private List<RealEstatePolicyLetter> list;

    @Builder
    public ResRealEstatePolicyLetter(List<RealEstatePolicyLetter> list) {
        this.list = list;
    }

    @Getter
    public static class RealEstatePolicyLetter {

        private Long id;

        private String title;

        private String thumbnailImgUrl;

        private String contentImgUrl;

        private String author;

        private LocalDateTime lastChngRegDttm;

        @Builder
        public RealEstatePolicyLetter(Long id, String title, String thumbnailImgUrl, String contentImgUrl, String author, LocalDateTime lastChngRegDttm) {
            this.id = id;
            this.title = title;
            this.thumbnailImgUrl = thumbnailImgUrl;
            this.contentImgUrl = contentImgUrl;
            this.author = author;
            this.lastChngRegDttm = lastChngRegDttm;
        }
    }
}
