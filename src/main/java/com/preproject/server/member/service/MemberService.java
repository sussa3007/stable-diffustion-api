package com.preproject.server.member.service;

import com.preproject.server.constant.ErrorCode;
import com.preproject.server.exception.GeneralException;
import com.preproject.server.member.dto.MemberDto;
import com.preproject.server.member.entity.Member;
import com.preproject.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public Member getMember(Long memberId) {
        return verifiedMemberById(memberId);
    }

    public Member createMember(MemberDto member) {
        System.out.println(member);
        verifyMemberByEmail(member.getEmail());
        String pw = passwordEncoder
                .encode(member.getPassword());
        member.setPassword(pw);
        Member create = Member.of(member);
        return memberRepository.save(create);
    }

    public Member updateMember(MemberDto member) {
        Member findMember = verifiedMemberByEmail(member.getEmail());
        if (member.getPassword() != null) {
            String encodePassword = passwordEncoder.encode(member.getPassword());
            findMember.setPassword(encodePassword);
        }
        Optional.ofNullable(member.getNickName())
                .ifPresent(findMember::setNickName);
        Optional.ofNullable(member.getPhoneNumber())
                .ifPresent(findMember::setPhoneNumber);
        return memberRepository.save(findMember);
    }

    public Member verifiedMemberById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() ->
                        new GeneralException(ErrorCode.NOT_FOUND));

    }
    public Member verifiedMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() ->
                        new GeneralException(ErrorCode.NOT_FOUND));

    }

    public void verifyMemberByEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new GeneralException(ErrorCode.MEMBER_EXISTS);
        }
    }
    public boolean verifyMemberByEmailReturnBoolean(String email) {
        Optional<Member> byEmail = memberRepository.findByEmail(email);
        return byEmail.isEmpty();
    }
}
