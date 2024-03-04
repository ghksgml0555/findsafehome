package com.swyg.findingahomesafely.service.member;


import com.swyg.findingahomesafely.domain.member.Member;
import com.swyg.findingahomesafely.dto.memberModifyDto.MemberModifyDto;
import com.swyg.findingahomesafely.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
