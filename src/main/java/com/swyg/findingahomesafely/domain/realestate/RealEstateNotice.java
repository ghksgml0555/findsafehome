package com.swyg.findingahomesafely.domain.realestate;

import com.swyg.findingahomesafely.domain.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "RE_REAL_ESTATE_NOTICE_I")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RealEstateNotice extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "THUMBNAIL_IMG_URL",unique = true)
    private String thumbnailImgUrl;

    @Column(name = "CONTENT_IMG_URL",unique = true)
    private String contentImgUrl;

    @Builder
    public RealEstateNotice(String thumbnailImgUrl, String contentImgUrl) {
        this.thumbnailImgUrl = thumbnailImgUrl;
        this.contentImgUrl = contentImgUrl;
    }
}
