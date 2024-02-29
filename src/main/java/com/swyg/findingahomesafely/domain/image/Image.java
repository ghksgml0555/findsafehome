package com.swyg.findingahomesafely.domain.image;

import com.swyg.findingahomesafely.domain.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "SY_IMAGE_I")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Image extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IMG_URL",unique = true, nullable = false)
    private String imageUrl;

    @Builder
    public Image(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
