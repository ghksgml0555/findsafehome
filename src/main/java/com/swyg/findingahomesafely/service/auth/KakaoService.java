package com.swyg.findingahomesafely.service.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swyg.findingahomesafely.domain.member.Member;
import com.swyg.findingahomesafely.dto.loginDto.LoginResponseDto;
import com.swyg.findingahomesafely.dto.loginDto.TokenDto;
import com.swyg.findingahomesafely.jwt.TokenProvider;
import com.swyg.findingahomesafely.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static com.swyg.findingahomesafely.common.codeconst.Authority.ROLE_USER;


@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final Environment environment;

    public LoginResponseDto kakaoLogin(String code){
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String redirectUrl = environment.getProperty("spring.security.oauth2.client.registration.kakao.redirect-uri");

        String accessToken = getAccessToken(code, redirectUrl);

        // 2. 토큰으로 카카오 API 호출
        HashMap<String, Object> userInfo= getKakaoUserInfo(accessToken);

        //3. 카카오ID로 회원가입 & 로그인 처리
        LoginResponseDto kakaoUserResponse= kakaoMemberLogin(userInfo);

        return kakaoUserResponse;
    }

    private String getAccessToken(String code, String redirectUri) {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "335d3b85731ba1f046c3d07aa53d735f");
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        String clientsecret = environment.getProperty("spring.security.oauth2.client.registration.kakao.client-secret");
        body.add("client_secret",clientsecret);
        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonNode.get("access_token").asText(); //토큰 전송
    }

    private HashMap<String, Object> getKakaoUserInfo(String accessToken) {
        HashMap<String, Object> userInfo= new HashMap<String,Object>();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();

        userInfo.put("id",id);
        userInfo.put("email",email);
        userInfo.put("nickname",nickname);

        return userInfo;
    }

    private LoginResponseDto kakaoMemberLogin(HashMap<String, Object> userInfo){

        Long uid= Long.valueOf(userInfo.get("id").toString());
        String kakaoEmail = userInfo.get("email").toString();
        String nickName = userInfo.get("nickname").toString();

        Member kakaoMember = memberRepository.findByEmail(kakaoEmail).orElse(null);

        if (kakaoMember == null) {    //회원가입
            kakaoMember= new Member(uid,kakaoEmail,"",nickName,"","",false,true,ROLE_USER);
            memberRepository.save(kakaoMember);
        }
        //토큰 생성
        TokenDto token=tokenProvider.generateTokenDtoForSocial(uid.toString());
        return new LoginResponseDto(uid,nickName,kakaoEmail,token);
    }
}
