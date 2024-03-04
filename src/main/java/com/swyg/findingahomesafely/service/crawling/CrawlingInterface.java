package com.swyg.findingahomesafely.service.crawling;

import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstateLatestPolicy;
import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstateLatestPolicy.RealEstateLatestPolicy;

import java.util.List;

public interface CrawlingInterface {

    void makeCrawling(int page);

    List<RealEstateLatestPolicy> getListEnterTitleAndEnterUrl();

    void setListEnterTitleAndEnterUrl(List<RealEstateLatestPolicy> list);

    String getSize();

    void setSize(String size);

    String getStartPage();
    String getEndPage();
    void setEndPage(String endPage);
}
