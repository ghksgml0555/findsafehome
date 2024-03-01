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
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static com.swyg.findingahomesafely.common.codeconst.Authority.ROLE_USER;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final Environment environment;

    public LoginResponseDto googleLogin(String code){
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 유저리소스 받아오기
        HashMap<String, Object> userInfo = getGoogleUserInfo(accessToken);

        //3. 카카오ID로 회원가입 & 로그인 처리
        LoginResponseDto GoogleUserResponse= googleMemberLogin(userInfo);

        return GoogleUserResponse;
    }

    private String getAccessToken(String code) {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "824362037923-shonp6ke3e7glidd67u03c0m6ucuj671.apps.googleusercontent.com");
        body.add("redirect_uri","http://localhost:7000/login/oauth2/code/google");
        body.add("code", code);

        String clientsecret = environment.getProperty("spring.security.oauth2.client.registration.google.client-secret");
        body.add("client_secret",clientsecret);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> GoogleTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                GoogleTokenRequest,
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

    private HashMap<String, Object> getGoogleUserInfo(String accessToken){
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> googleUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                googleUserInfoRequest,
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

        String id = jsonNode.get("id").asText();
        String email = jsonNode.get("email").asText();
        String name;
        if(jsonNode.get("name")==null){
            name = "미공개";
        }
        else{
            name = jsonNode.get("name").asText();
        }


        HashMap<String, Object> userInfo= new HashMap<String,Object>();
        userInfo.put("id",id);
        userInfo.put("email",email);
        userInfo.put("name",name);

        return userInfo;
    }

    private LoginResponseDto googleMemberLogin(HashMap<String, Object> userInfo){

        Long uid= Long.valueOf(userInfo.get("id").toString().substring(0,9));
        String email = userInfo.get("email").toString();
        String name = userInfo.get("name").toString();

        Member googleMember = memberRepository.findByEmail(email).orElse(null);

        if (googleMember == null) {    //회원가입
            googleMember= new Member(uid,email,"",name,"","",ROLE_USER);
            memberRepository.save(googleMember);
        }
        //토큰 생성
        TokenDto token=tokenProvider.generateTokenDtoForSocial(uid.toString());
        return new LoginResponseDto(uid,name,email,token);
    }
}
