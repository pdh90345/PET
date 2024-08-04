package com.example.PET.service;

import com.example.PET.domain.Member;
import com.example.PET.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member join(Member membership) {
        return memberRepository.save(membership);
    }

    public Member check(String user_id,String user_pw) {
        return memberRepository.login(user_id,user_pw);
    }

    public int idcheck(String user_id) {
        return memberRepository.idcheck(user_id);
    }


    public boolean login_checkout(String user_id) {
        return memberRepository.login_checkout(user_id);
    }
}
