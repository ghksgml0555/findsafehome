package com.swyg.findingahomesafely.controller;

import com.swyg.findingahomesafely.common.response.ResponseResult;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseResult<?> testController() {

        String name = "nsw";

        Asd asd = new Asd();
        asd.setMail("tlsdnr1135");
        asd.setName("신욱");

        return ResponseResult.body(asd);
    }




    @Getter
    @Setter
    public class Asd{

        private String name;
        private String mail;

    }

}
