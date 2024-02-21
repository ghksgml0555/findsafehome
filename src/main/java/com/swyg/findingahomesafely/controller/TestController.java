package com.swyg.findingahomesafely.controller;

import com.swyg.findingahomesafely.common.exception.SwygException;
import com.swyg.findingahomesafely.common.response.ResponseResult;
import com.swyg.findingahomesafely.domain.error.SyErrMsgI;
import com.swyg.findingahomesafely.repository.TestRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestRepository testRepository;

    @Value("${crawling.url}")
    private String crawlingUrl;

    @GetMapping("/test")
    public ResponseResult<?> testController() {

        SyErrMsgI syErrMsgI = SyErrMsgI.builder()
                .errCd("TEST07")
                .errMsg("테스트 커스텀 익셉션07")
                .build();

        testRepository.save(syErrMsgI);

        return ResponseResult.body(syErrMsgI);
    }

    @GetMapping("/testSelect")
    public ResponseResult<?> testSelectController() {

        SyErrMsgI syErrMsgI = testRepository.findByErrCd("C99999").orElseThrow(
                () -> new SwygException("C99999"));
        System.out.println("===========================================================");
        System.out.println(syErrMsgI.getCreatedAt());

        List<SyErrMsgI> all = testRepository.findAll();

        System.out.println("===========================================================");

        all.stream().forEach(System.out::println);

        return ResponseResult.body(syErrMsgI);
    }

    @GetMapping("/testException")
    public ResponseResult<?> testExceptionController() {

        throw new SwygException("C99999");

    }
    @GetMapping("/testRuntimeException")
    public ResponseResult<?> testRuntimeExceptionController() {

        throw new RuntimeException();

    }

    @GetMapping("/soup")
    public ResponseResult<?> testSoupController() {

        String crawlingEnterUrl = crawlingUrl;

        Connection conn = Jsoup.connect(crawlingEnterUrl);

        Document document = null;

        try{
            document = conn.get();
        }catch (IOException e) {
            e.printStackTrace();
        }


        for (int i=1; i<=10; i++) {
            Elements titleElements = document.select("#contents > table > tbody > tr:nth-child("+i+") > td.bd_title > a");
            String enterTitle = titleElements.get(0).absUrl("href");
            String enterUrl = titleElements.get(0).text();

            System.out.println(i + " / enterTitle: " +enterTitle);
            System.out.println(i + " / enterUrl: " +enterUrl);
            System.out.println();
        }

        return ResponseResult.body();
    }




    @Getter
    @Setter
    public class Asd{

        private String name;
        private String mail;
        private String test;

    }

}
