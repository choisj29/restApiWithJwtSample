package com.choisj.jwtLoginSample.web.dto.member;

import com.choisj.jwtLoginSample.domain.member.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDto {
    private Long memberId;
    private String loginId;


    public MemberDto(Member member) {
        this.memberId = member.getId();
        this.loginId = member.getLoginId();
    }


}
