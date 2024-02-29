package com.swyg.findingahomesafely.dto.loginDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {
    private Long id;
    private String name;
    private String email;
    private TokenDto token;

    public LoginResponseDto(Long id,String nickname, String email, TokenDto token) {
        this.id = id;
        this.name = nickname;
        this.email = email;
        this.token = token;
    }
}
