package com.swyg.findingahomesafely.service.auth;

import com.swyg.findingahomesafely.common.exception.SwygException;
import com.swyg.findingahomesafely.domain.member.Member;
import com.swyg.findingahomesafely.domain.member.RefreshToken;
import com.swyg.findingahomesafely.dto.loginDto.LoginDto;
import com.swyg.findingahomesafely.dto.loginDto.TokenDto;
import com.swyg.findingahomesafely.dto.loginDto.TokenRequestDto;
import com.swyg.findingahomesafely.dto.memberDto.MemberRequestDto;
import com.swyg.findingahomesafely.dto.memberDto.MemberResponseDto;
import com.swyg.findingahomesafely.dto.passwordFindDto.PasswordFindDto;
import com.swyg.findingahomesafely.jwt.TokenProvider;
import com.swyg.findingahomesafely.repository.MemberRepository;
import com.swyg.findingahomesafely.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            throw new SwygException("SU0001","이미 가입되어 있는 유저(이메일)입니다");
        }

        Member member = memberRequestDto.toMember(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }

    @Transactional
    public TokenDto login(LoginDto loginDto) {
        try{
            Member member = memberRepository.findByEmail(loginDto.getEmail()).get();
            if(member.isDelete() == true){
                throw new SwygException("DM0001","탈퇴한 회원입니다.");
            }

            // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
            UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();

            // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
            //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

            // 4. RefreshToken 저장
            RefreshToken refreshToken = RefreshToken.builder()
                    .key(authentication.getName())
                    .value(tokenDto.getRefreshToken())
                    .build();

            refreshTokenRepository.save(refreshToken);

            // 5. 토큰 발급
            return tokenDto;
        }catch (BadCredentialsException e){
            throw new SwygException("FP0001","비밀번호가 일치하지 않습니다.");
        }catch (InternalAuthenticationServiceException e){
            throw new SwygException("NU0001","데이터베이스에 멤버가 존재하지 않습니다");
        }

    }

    public boolean userValidCheck(PasswordFindDto passwordFindDto){
        Member findMember = memberRepository.findByEmail(passwordFindDto.getEmail()).get();
        if(findMember!=null && findMember.getName().equals(passwordFindDto.getName()) &&
        findMember.getDateOfBirth().equals(passwordFindDto.getDateOfBirth()) &&
        findMember.getTelNo().equals(passwordFindDto.getTelNo())){
            return true;
        }
        else return false;
    }

    public String createTempPassword(){
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            builder.append("@");
            builder.append("@");
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
    }

    @Transactional
    public void changeTempPassword(String email, String tempPassword){
        Member member = memberRepository.findByEmail(email).get();
        member.changePassword(passwordEncoder.encode(tempPassword));
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new SwygException("RT0001","Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new SwygException("RT0002","로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new SwygException("RT0003","토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }
}

