package com.swyg.findingahomesafely.service.member;


import com.swyg.findingahomesafely.domain.member.Member;
import com.swyg.findingahomesafely.dto.memberModifyDto.MemberModifyDto;
import com.swyg.findingahomesafely.repository.MemberRepository;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public void modifyMember(MemberModifyDto memberModifyDto){
        Member member = memberRepository.findByEmail(memberModifyDto.getEmail()).get();
        String password = memberModifyDto.getPassword();
        String name = memberModifyDto.getName();
        String dateOfBirth = memberModifyDto.getDateOfBirth();
        String telNo = memberModifyDto.getTelNo();
        member.changeDetail(passwordEncoder.encode(password), name, dateOfBirth, telNo);
    }

    public void deleteMember(@RequestParam("email")
                             @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$")String email){
        Member member = memberRepository.findByEmail(email).get();
        member.deleteTrue();
    }
}
