package com.example.PET.service;


import com.example.PET.domain.Member;
import com.example.PET.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Test
    public void check(){
        Member member = new Member();
        member.setId(1L);
        member.setUserid("sss6974");
        member.setUserpw("1234");
        member.setUsername("송승수");
        member.setPhoneNum("01028456739");

        memberRepository.save(member);

        Member login = memberRepository.login(member.getUserid(), member.getUserpw());
        Assertions.assertThat(login.getUsername()).isEqualTo("송승수");
    }
}