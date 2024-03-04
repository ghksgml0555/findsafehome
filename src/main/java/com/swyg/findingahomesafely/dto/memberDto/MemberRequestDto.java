package com.swyg.findingahomesafely.dto.memberDto;

import com.swyg.findingahomesafely.common.codeconst.Authority;
import com.swyg.findingahomesafely.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자의 숫자, 영문, 특수문자가 포함되어야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "생년월일은 필수 입력 값입니다.")
    private String dateOfBirth;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    private String telNo;

    private boolean isSignup;

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .dateOfBirth(dateOfBirth)
                .telNo(telNo)
                .isSignup(isSignup)
                .isDelete(false)
                .authority(Authority.ROLE_USER)
                .build();
    }


}
