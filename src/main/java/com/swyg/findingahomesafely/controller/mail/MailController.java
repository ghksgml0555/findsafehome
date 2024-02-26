package com.swyg.findingahomesafely.controller.mail;

import com.swyg.findingahomesafely.common.response.ResponseResult;
import com.swyg.findingahomesafely.service.code.CodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MailController {
    private final CodeService codeService;

    @PostMapping("/emails/verification-requests")
    public ResponseResult<?> sendMessage(@RequestParam("email") String email) {
        codeService.sendCodeToEmail(email);
        return ResponseResult.body();
        //return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/emails/verifications")
    public ResponseResult<?> verificationEmail(@RequestParam("email") String email,
                                            @RequestParam("code") String authCode) {
        codeService.verifiedCode(email, authCode);
        return ResponseResult.body();
        //return new ResponseEntity<>(HttpStatus.OK);
    }
}
