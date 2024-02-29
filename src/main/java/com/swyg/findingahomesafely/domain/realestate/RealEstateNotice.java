package com.swyg.findingahomesafely.domain.realestate;

import com.swyg.findingahomesafely.common.codeconst.YN;
import com.swyg.findingahomesafely.domain.Timestamped;
import com.swyg.findingahomesafely.domain.image.Image;
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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "THUMBNAIL_IMG_URL")
    private Image thumbnailImgUrl;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "CONTENT_IMG_URL")
    private Image contentImgUrl;

    @Column(name = "USE_YN",nullable = false)
    @Enumerated(EnumType.STRING)
    private YN useYn = YN.N;

    @Builder
    public RealEstateNotice(Image thumbnailImgUrl, Image contentImgUrl, YN useYn) {
        this.thumbnailImgUrl = thumbnailImgUrl;
        this.contentImgUrl = contentImgUrl;
        this.useYn = useYn;
    }
}
