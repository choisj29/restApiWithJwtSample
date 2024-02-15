package com.choisj.jwtLoginSample.service.member;


import com.choisj.jwtLoginSample.domain.member.Role;
import com.choisj.jwtLoginSample.domain.member.Member;
import com.choisj.jwtLoginSample.repository.member.MemberRepository;
import com.choisj.jwtLoginSample.web.dto.member.AuthDto;
import com.choisj.jwtLoginSample.web.dto.member.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    @Transactional
    public MemberDto join(AuthDto.SaveMemberDto dto) throws IOException {

        Member member = Member.builder()
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .role(Role.USER)
                .build();
        if(checkLoginIdDuplicate(member.getLoginId())){  //id 중복검사
            if (!checkNameDuplicate(member.getName())) { //name 중복검사
                return new MemberDto(memberRepository.save(member));
            }
        }
        return new MemberDto(member); //join failed
    }

    public boolean checkLoginIdDuplicate(String loginId) {
        Optional<Member> findMember = memberRepository.findByLoginId(loginId);
        log.info("isPresent {}", findMember.isPresent());
        if (findMember.isPresent()) {
            log.info("there is duplicate loginId");
            return false;
        } else {
            log.info("unique loginId");
            return true;
        }
    }

    //닉네임 중복검사
    public boolean checkNameDuplicate(String name) {
        return memberRepository.existsByName(name);
    }

    public Optional<Member> findByLoginId(String loginId){
        log.info("findByLoginId {}",loginId);
        return memberRepository.findByLoginId(loginId);
    }


}