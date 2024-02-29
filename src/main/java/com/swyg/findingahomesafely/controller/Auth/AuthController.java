package com.swyg.findingahomesafely.controller.Auth;

import com.swyg.findingahomesafely.common.exception.SwygException;
import com.swyg.findingahomesafely.common.response.ResponseResult;
import com.swyg.findingahomesafely.dto.loginDto.LoginDto;
import com.swyg.findingahomesafely.dto.loginDto.LoginResponseDto;
import com.swyg.findingahomesafely.dto.loginDto.TokenRequestDto;
import com.swyg.findingahomesafely.dto.memberDto.MemberRequestDto;
import com.swyg.findingahomesafely.service.auth.AuthService;
import com.swyg.findingahomesafely.service.auth.KakaoService;
import com.swyg.findingahomesafely.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final KakaoService kakaoService;

    @PostMapping("/signup")
    public ResponseResult<?> signup(@Valid @RequestBody MemberRequestDto memberRequestDto, Errors errors) {
        if(errors.hasErrors()){
            throw new SwygException("VE0000", "유효하지 않은 회원가입 입력입니다.");
        }

        return ResponseResult.body(authService.signup(memberRequestDto));
        //return ResponseEntity.ok(authService.signup(memberRequestDto));
    }

    @PostMapping("/login")
    public ResponseResult<?> login(@RequestBody LoginDto loginDto) {
        return ResponseResult.body(authService.login(loginDto));
        //return ResponseEntity.ok(authService.login(loginDto));
    }

    //인가코드가 /login/oauth2/code/kakao로 code 파라미터로 넘어옴
    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestParam(value="code") String code, HttpServletRequest request){
        try{
            return ResponseEntity.ok(kakaoService.kakaoLogin(code));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Item Not Found");
        }
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
