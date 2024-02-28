package com.swyg.findingahomesafely.dto.memberDto;

import com.swyg.findingahomesafely.common.codeconst.Authority;
import com.swyg.findingahomesafely.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    private String email;
    private String password;
    private String name;
    private String dateOfBirth;
    private String telNo;

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .dateOfBirth(dateOfBirth)
                .telNo(telNo)
                .authority(Authority.ROLE_USER)
                .build();
    }


}
