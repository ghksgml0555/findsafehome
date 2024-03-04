package com.swyg.findingahomesafely.service.code;

import com.swyg.findingahomesafely.common.exception.SwygException;
import com.swyg.findingahomesafely.domain.member.Member;
import com.swyg.findingahomesafely.dto.memberDto.MemberRequestDto;
import com.swyg.findingahomesafely.dto.memberDto.MemberResponseDto;
import com.swyg.findingahomesafely.repository.MemberRepository;
import com.swyg.findingahomesafely.service.mail.MailService;
import com.swyg.findingahomesafely.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CodeService { //인증번호 생성, 검증
    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    private final MemberRepository memberRepository;

    private final MailService mailService;

    private final RedisService redisService;

    private final PasswordEncoder passwordEncoder;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    public void sendCodeToEmail(String toEmail) {
        if (memberRepository.existsByEmail(toEmail)
                && memberRepository.findByEmail(toEmail).get().isSignup()) {
            throw new SwygException("SU0001","이미 가입되어 있는 유저(이메일)입니다");
        }

        this.checkDuplicatedEmail(toEmail);
        String title = "세로운집 이메일 인증 번호";
        String authCode = this.createCode();
        mailService.sendEmail(toEmail, title, authCode);
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
        redisService.setValues(AUTH_CODE_PREFIX + toEmail,
                authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    private void checkDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            log.debug("MemberService.checkDuplicatedEmail exception occur email: {}", email);
            throw new SwygException("SU0001","이미 가입되어 있는 유저(이메일)입니다");
        }
    }

    private String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new RuntimeException();
        }
    }

    public void verifiedCode(String email, String authCode) {
        this.checkDuplicatedEmail(email);
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);

        if (!authResult) {
            throw new SwygException("CF0001","인증번호가 일치하지 않습니다.");
        }
        else{
            MemberRequestDto memberRequestDto = new MemberRequestDto(email,"","","","",false);
            Member member = memberRequestDto.toMember(passwordEncoder);
            memberRepository.save(member);
        }
    }
}
