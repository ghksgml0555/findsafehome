package com.swyg.findingahomesafely.controller;

import com.swyg.findingahomesafely.common.exception.SwygException;
import com.swyg.findingahomesafely.common.response.ResponseResult;
import com.swyg.findingahomesafely.domain.error.SY_ERR_MSG_I;
import com.swyg.findingahomesafely.repository.TestRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestRepository testRepository;

    @GetMapping("/test")
    public ResponseResult<?> testController() {

        SY_ERR_MSG_I syErrMsgI = SY_ERR_MSG_I.builder()
                .errCd("TEST05")
                .errMsg("테스트 커스텀 익셉션05")
                .build();

        testRepository.save(syErrMsgI);

        return ResponseResult.body(syErrMsgI);
    }

    @GetMapping("/testSelect")
    public ResponseResult<?> testSelectController() {

        SY_ERR_MSG_I syErrMsgI = testRepository.findByErrCd("TEST01").orElseThrow(
                () -> new SwygException("TEST01"));
        System.out.println("===========================================================");
        System.out.println(syErrMsgI.getCreatedAt());

        List<SY_ERR_MSG_I> all = testRepository.findAll();

        System.out.println("===========================================================");

        all.stream().forEach(System.out::println);

        return ResponseResult.body(syErrMsgI);
    }

    @GetMapping("/soup")
    public ResponseResult<?> testSoupController() {

        //TODO yml에 넣기.
        String crawlingEnterUrl = "https://www.molit.go.kr/USR/NEWS/m_71/lst.jsp?search_section=p_sec_2";

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

    }

}
