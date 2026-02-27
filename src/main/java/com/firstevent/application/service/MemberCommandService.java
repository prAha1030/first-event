package com.firstevent.application.service;

import com.firstevent.adapter.dto.MemberRequestDto;
import com.firstevent.application.ports.in.MemberGetUseCase;
import com.firstevent.application.ports.in.MemberManageUseCase;
import com.firstevent.application.ports.out.MemberRepository;
import com.firstevent.domain.member.Member;
import com.firstevent.domain.member.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService implements MemberManageUseCase {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberGetUseCase memberGetUseCase;

    @Override
    public Member regist(MemberRequestDto requestDto) {
        return memberRepository.save(Member.regist(requestDto, passwordEncoder));
    }

    @Override
    public Member withdraw(Long id) {
        Member member = memberGetUseCase.get(id);
        member.withdraw();
        return member;
    }
}
