package com.swyg.findingahomesafely.domain.member;

import com.swyg.findingahomesafely.common.codeconst.Authority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MEMBER")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="EMAIL")
    private String email;

    @Column(name="PASSWORD")
    private String password;

    @Column(name="NAME")
    private String name;

    @Column(name="DATE_OF_BIRTH")
    private String dateOfBirth;

    @Column(name="TEL_NO")
    private String telNo;

    @Enumerated(EnumType.STRING)
    @Column(name="AUTHORITY")
    private Authority authority;

    public void changePassword(String newPassword){
        this.password = newPassword;
    }

    public void changeDetail(String password, String name, String dateOfBirth, String telNo){
        this.password = password;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.telNo = telNo;
    }

}
