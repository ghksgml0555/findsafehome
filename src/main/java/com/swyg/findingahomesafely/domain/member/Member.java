package com.swyg.findingahomesafely.domain.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="name")
    private String name;

    @Column(name="date_of_birth")
    private String dateOfBirth;

    @Column(name="tel_no")
    private String telNo;

    @Enumerated(EnumType.STRING)
    @Column(name="authority")
    private Authority authority;

}
