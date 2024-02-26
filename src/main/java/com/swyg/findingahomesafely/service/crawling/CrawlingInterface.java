package com.swyg.findingahomesafely.service.crawling;

import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstateLatestPolicy;

import java.util.List;

public interface CrawlingInterface {

    void makeCrawling();

    List<ResRealEstateLatestPolicy> getListEnterTitleAndEnterUrl();

    void setListEnterTitleAndEnterUrl(List<ResRealEstateLatestPolicy> list);

}
