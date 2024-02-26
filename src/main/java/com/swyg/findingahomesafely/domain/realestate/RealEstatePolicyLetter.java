package com.swyg.findingahomesafely.domain.realestate;

import com.swyg.findingahomesafely.domain.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "RE_REAL_ESTATE_POLICY_LETTER_I")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RealEstatePolicyLetter extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE",unique = true)
    private String title;

    @Column(name = "THUMBNAIL_IMG_URL",unique = true)
    private String thumbnailImgUrl;

    @Column(name = "CONTENT_IMG_URL",unique = true)
    private String contentImgUrl;

    @Column(name = "AUTHOR",unique = true)
    private String author;

    @Builder
    public RealEstatePolicyLetter(String title, String thumbnailImgUrl, String contentImgUrl, String author) {
        this.title = title;
        this.thumbnailImgUrl = thumbnailImgUrl;
        this.contentImgUrl = contentImgUrl;
        this.author = author;
    }
}
