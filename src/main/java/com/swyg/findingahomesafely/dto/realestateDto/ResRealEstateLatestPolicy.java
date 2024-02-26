package com.swyg.findingahomesafely.dto.realestateDto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResRealEstateLatestPolicy {

    private String enterTitle;
    private String enterUrl;

    @Builder
    public ResRealEstateLatestPolicy(String enterTitle, String enterUrl) {
        this.enterTitle = enterTitle;
        this.enterUrl = enterUrl;
    }
}