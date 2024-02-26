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

    public List<ResRealEstateLatestPolicy> getRealEstateLatestPolicyList(){
        return crawlingInterface.getListEnterTitleAndEnterUrl();
    }

}
