package com.choisj.jwtLoginSample.repository.member;


import com.choisj.jwtLoginSample.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);
    boolean existsByName(String name);

}
