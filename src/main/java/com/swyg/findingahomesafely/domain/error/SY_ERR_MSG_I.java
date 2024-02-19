package com.swyg.findingahomesafely.domain.error;

import com.swyg.findingahomesafely.domain.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class SY_ERR_MSG_I extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String errCd;

    @Column(unique = true)
    private String errMsg;

    @Builder
    public SY_ERR_MSG_I(String errCd, String errMsg) {
        this.errCd = errCd;
        this.errMsg = errMsg;
    }
}
