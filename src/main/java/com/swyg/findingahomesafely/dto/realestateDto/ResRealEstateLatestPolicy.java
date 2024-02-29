package com.swyg.findingahomesafely.dto.realestateDto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResRealEstateLatestPolicy {

    private String totalSize;
    private String startPage;
    private String endPage;
    private List<RealEstateLatestPolicy> list;

    @Builder
    public ResRealEstateLatestPolicy(String totalSize, String startPage, String endPage, List<RealEstateLatestPolicy> list) {
        this.totalSize = totalSize;
        this.startPage = startPage;
        this.endPage = endPage;
        this.list = list;
    }


    @Getter
    public static class RealEstateLatestPolicy {

        private String enterTitle;
        private String enterUrl;

        @Builder
        public RealEstateLatestPolicy(String enterTitle, String enterUrl) {
            this.enterTitle = enterTitle;
            this.enterUrl = enterUrl;
        }
    }
}