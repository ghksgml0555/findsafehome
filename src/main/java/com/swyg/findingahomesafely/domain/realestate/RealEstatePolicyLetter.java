package com.swyg.findingahomesafely.domain.realestate;

import com.swyg.findingahomesafely.domain.Timestamped;
import com.swyg.findingahomesafely.domain.image.Image;
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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "THUMBNAIL_IMG_URL",unique = true)
    private Image thumbnailImgUrl;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "CONTENT_IMG_URL",unique = true)
    private Image contentImgUrl;

    @Column(name = "AUTHOR",unique = true)
    private String author;

    @Builder
    public RealEstatePolicyLetter(String title, Image thumbnailImgUrl, Image contentImgUrl, String author) {
        this.title = title;
        this.thumbnailImgUrl = thumbnailImgUrl;
        this.contentImgUrl = contentImgUrl;
        this.author = author;
    }
}
