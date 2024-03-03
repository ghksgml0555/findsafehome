package com.swyg.findingahomesafely.controller.Auth;

import com.swyg.findingahomesafely.common.exception.SwygException;
import com.swyg.findingahomesafely.common.response.ResponseResult;
import com.swyg.findingahomesafely.dto.loginDto.LoginDto;
import com.swyg.findingahomesafely.dto.loginDto.LoginResponseDto;
import com.swyg.findingahomesafely.dto.loginDto.TokenDto;
import com.swyg.findingahomesafely.dto.loginDto.TokenRequestDto;
import com.swyg.findingahomesafely.dto.memberDto.MemberRequestDto;
import com.swyg.findingahomesafely.dto.passwordFindDto.PasswordFindDto;
import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstateLatestPolicy;
import com.swyg.findingahomesafely.dto.realestateDto.ResRealEstatePolicyLetter;
import com.swyg.findingahomesafely.service.auth.AuthService;
import com.swyg.findingahomesafely.service.auth.GoogleService;
import com.swyg.findingahomesafely.service.auth.KakaoService;
import com.swyg.findingahomesafely.service.code.CodeService;
import com.swyg.findingahomesafely.service.mail.MailService;
import com.swyg.findingahomesafely.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@Tag(name="회원가입/로그인")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final MailService mailService;

    @Operation(summary = "자체 이메일 회원가입")
    @ApiResponses({
                    @ApiResponse(responseCode = "200", description = "가입 성공-이메일 반환")
            })
    @PostMapping("/signup")
    public ResponseResult<?> signup(@Valid @RequestBody MemberRequestDto memberRequestDto, Errors errors) {
        if(errors.hasErrors()){
            throw new SwygException("VE0000", "유효하지 않은 회원가입 입력입니다.");
        }

        return ResponseResult.body(authService.signup(memberRequestDto));
        //return ResponseEntity.ok(authService.signup(memberRequestDto));
    }

    @Operation(summary = "사이트 자체 로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공-jwt토큰 반환", content = @Content(schema = @Schema(implementation = TokenDto.class)))
    })
    @PostMapping("/login")
    public ResponseResult<?> login(@RequestBody LoginDto loginDto) {
        return ResponseResult.body(authService.login(loginDto));
        //return ResponseEntity.ok(authService.login(loginDto));
    }

    @Operation(summary = "비밀번호 찾기(임시비밀번호 발급)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "임시비밀번호 발급성공")
    })
    @PostMapping("/findpassword")
    public ResponseResult<?> findPassword(@RequestBody PasswordFindDto passwordFindDto){
        if(authService.userValidCheck(passwordFindDto)==true){
            String tempPassword = authService.createTempPassword();
            String toEmail = passwordFindDto.getEmail();
            String title = "세로운집 임시 비밀번호";
            mailService.sendEmail(toEmail, title, tempPassword);

            authService.changeTempPassword(toEmail, tempPassword);

            return ResponseResult.body();
        }
        else {
            throw new SwygException("NEM0001","주어진 정보와 일치하는 멤버가 없습니다.");
        }
    }

    //인가코드가 /login/oauth2/code/kakao로 code 파라미터로 넘어옴
    @Operation(summary = "카카오 소셜 로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공- 회원정보 반환", content = @Content(schema = @Schema(implementation = LoginResponseDto.class)))
    })
    @GetMapping("/login/oauth2/code/kakao")
    public ResponseResult<?> kakaoLogin(@RequestParam(value="code") String code, HttpServletRequest request){
        try{
            return ResponseResult.body(kakaoService.kakaoLogin(code));
        } catch (NoSuchElementException e) {
            throw new SwygException("KE0001","카카오 로그인 에러.");
        }
    }

    @Operation(summary = "구글 소셜 로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공- 회원정보 반환", content = @Content(schema = @Schema(implementation = LoginResponseDto.class)))
    })
    @GetMapping("/login/oauth2/code/google")
    public ResponseResult<?> googleLogin(@RequestParam(value="code") String code){
        try{
            return ResponseResult.body(googleService.googleLogin(code));
        }catch (NoSuchElementException e) {
            throw new SwygException("GE0001","구글 로그인 에러.");
        }
    }

    @Operation(summary = "토큰 재발급")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재발급 성공", content = @Content(schema = @Schema(implementation = TokenDto.class)))
    })
    @PostMapping("/reissue")
    public ResponseResult<?> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseResult.body(authService.reissue(tokenRequestDto));
        //return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

}
