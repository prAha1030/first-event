package com.firstevent.application.ports.in;

import com.firstevent.adapter.dto.MemberRequestDto;
import com.firstevent.domain.member.Member;

public interface MemberManageUseCase {
    Member regist(MemberRequestDto requestDto);

    Member withdraw(Long id);
}
