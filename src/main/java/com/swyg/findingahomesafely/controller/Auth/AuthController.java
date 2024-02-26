package com.swyg.findingahomesafely.controller.Auth;

import com.swyg.findingahomesafely.common.response.ResponseResult;
import com.swyg.findingahomesafely.dto.loginDto.LoginDto;
import com.swyg.findingahomesafely.dto.loginDto.TokenRequestDto;
import com.swyg.findingahomesafely.dto.memberDto.MemberRequestDto;
import com.swyg.findingahomesafely.service.auth.AuthService;
import com.swyg.findingahomesafely.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseResult<?> signup(@RequestBody MemberRequestDto memberRequestDto) {
        return ResponseResult.body(authService.signup(memberRequestDto));
        //return ResponseEntity.ok(authService.signup(memberRequestDto));
    }

    @PostMapping("/login")
    public ResponseResult<?> login(@RequestBody LoginDto loginDto) {
        return ResponseResult.body(authService.login(loginDto));
        //return ResponseEntity.ok(authService.login(loginDto));
    }

    @PostMapping("/reissue")
    public ResponseResult<?> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseResult.body(authService.reissue(tokenRequestDto));
        //return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @PostMapping("/loginTest")
    public Long test() {
        return SecurityUtil.getCurrentMemberId();
    }
}
