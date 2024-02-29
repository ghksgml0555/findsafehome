package com.swyg.findingahomesafely.service.realestate;

import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstateLatestPolicy;
import com.swyg.findingahomesafely.service.crawling.CrawlingInterface;
import com.swyg.findingahomesafely.service.crawling.CrawlingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealEstateLatestPolicyService {

    private final CrawlingInterface crawlingInterface;

    public RealEstateLatestPolicyService(CrawlingServiceImpl crawlingService) {
        this.crawlingInterface = crawlingService;
    }

    public ResRealEstateLatestPolicy selectRealEstateLatestPolicyPaging(String page){
        crawlingInterface.makeCrawling(page+1);

        ResRealEstateLatestPolicy res = ResRealEstateLatestPolicy.builder()
                .totalSize(crawlingInterface.getSize())
                .startPage(crawlingInterface.getStartPage())
                .endPage(crawlingInterface.getEndPage())
                .list(crawlingInterface.getListEnterTitleAndEnterUrl())
                .build();

        return res;
    }

}
