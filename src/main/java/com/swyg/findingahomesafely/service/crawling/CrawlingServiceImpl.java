package com.swyg.findingahomesafely.service.crawling;

import com.swyg.findingahomesafely.common.exception.SwygException;
import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstateLatestPolicy;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CrawlingServiceImpl implements CrawlingInterface{

    @Value("${crawling.url}")
    private String crawlingUrl;

    private List<ResRealEstateLatestPolicy> listEnterTitleAndEnterUrl;

    @Bean
    @Override
    public void makeCrawling(){

        log.info("[Bean-makeCrawling] start");

        Connection conn = null;
        Document document = null;

        try{
            conn = Jsoup.connect(crawlingUrl);
            document = conn.get();
        }catch (IOException e) {
            e.printStackTrace();
            throw new SwygException("C99999");
        }

        List<ResRealEstateLatestPolicy> list = new ArrayList<>();

        for (int i=1; i<=10; i++) {
            Elements titleElements = document.select("#contents > table > tbody > tr:nth-child("+i+") > td.bd_title > a");

            ResRealEstateLatestPolicy res = ResRealEstateLatestPolicy.builder()
                    .enterTitle(titleElements.get(0).text())
                    .enterUrl(titleElements.get(0).absUrl("href"))
                    .build();

            list.add(res);
        }

        this.setListEnterTitleAndEnterUrl(list);

        //TODO 지우기
        listEnterTitleAndEnterUrl.stream().forEach(
                item -> {
                    System.out.println("title : " + item.getEnterTitle());
                    System.out.println("url : " + item.getEnterUrl());
                }
        );


        log.info("[Bean-makeCrawling] end");
    }

    @Override
    public List<ResRealEstateLatestPolicy> getListEnterTitleAndEnterUrl() {
        return this.listEnterTitleAndEnterUrl;
    }

    @Override
    public void setListEnterTitleAndEnterUrl(List<ResRealEstateLatestPolicy> list) {
        this.listEnterTitleAndEnterUrl = list;
    }
}
