package com.firstevent.adapter.api;

import com.firstevent.adapter.dto.MemberRequestDto;
import com.firstevent.adapter.dto.MemberResponseDto;
import com.firstevent.application.ports.in.MemberGetUseCase;
import com.firstevent.application.ports.in.MemberManageUseCase;
import com.firstevent.domain.member.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberGetUseCase memberGetUseCase;
    private final MemberManageUseCase memberManageUseCase;

    @PostMapping
    public MemberResponseDto regist(@Valid @RequestBody MemberRequestDto requestDto) {
        Member member = memberManageUseCase.regist(requestDto);

        return MemberResponseDto.from(member);
    }

    @DeleteMapping("/{id}")
    public void withdraw(@PathVariable Long id) {
        memberManageUseCase.withdraw(id);
    }
}
