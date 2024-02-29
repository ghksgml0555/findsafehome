package com.swyg.findingahomesafely.service.crawling;

import com.swyg.findingahomesafely.common.exception.SwygException;
import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstateLatestPolicy;
import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstateLatestPolicy.RealEstateLatestPolicy;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.ast.tree.from.MappedByTableGroup;
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

    private List<RealEstateLatestPolicy> listEnterTitleAndEnterUrl;

    private String size;

    private String startPage = "0";
    private String endPage;

    @Override
    public void makeCrawling(String page){

        Connection conn = null;
        Document document = null;

        try{
            conn = Jsoup.connect(crawlingUrl+page);
            document = conn.get();
        }catch (IOException e) {
            e.printStackTrace();
            throw new SwygException("C99999");
        }

        //페이지 총 건수
        Elements ele = document.select("#contents > div.bd_info > strong");
        String totalSize = ele.get(0).text();
        this.setSize(totalSize);

        //시작 페이지, 마지막 페이지
        int temp = (int) Math.ceil(Integer.parseInt(totalSize)/10.0);
        this.setEndPage(String.valueOf(temp-1));

        List<RealEstateLatestPolicy> list = new ArrayList<>();
        for (int i=1; i<=10; i++) {
            Elements titleElements = document.select("#contents > table > tbody > tr:nth-child("+i+") > td.bd_title > a");

            RealEstateLatestPolicy res = RealEstateLatestPolicy.builder()
                    .enterTitle(titleElements.get(0).text())
                    .enterUrl(titleElements.get(0).absUrl("href"))
                    .build();

            list.add(res);
        }
        this.setListEnterTitleAndEnterUrl(list);

    }

    @Override
    public List<RealEstateLatestPolicy> getListEnterTitleAndEnterUrl() {
        return this.listEnterTitleAndEnterUrl;
    }

    @Override
    public void setListEnterTitleAndEnterUrl(List<RealEstateLatestPolicy> list) {
        this.listEnterTitleAndEnterUrl = list;
    }

    @Override
    public String getSize() {
        return this.size;
    }

    @Override
    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String getStartPage() {
        return this.startPage;
    }

    @Override
    public String getEndPage() {
        return this.endPage;
    }

    @Override
    public void setEndPage(String endPage) {
        this.endPage = endPage;
    }
}
