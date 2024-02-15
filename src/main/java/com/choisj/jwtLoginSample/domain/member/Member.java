package com.choisj.jwtLoginSample.domain.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Getter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String loginId;             //아이디
    private String password;            //비밀번호
    private String name;                //닉네임

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String loginId, String password, String name, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.role = role;
    }
}
