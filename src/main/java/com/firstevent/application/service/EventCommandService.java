package com.firstevent.application.service;

import com.firstevent.application.ports.in.EventGetUseCase;
import com.firstevent.application.ports.in.EventManageUseCase;
import com.firstevent.application.ports.in.MemberGetUseCase;
import com.firstevent.domain.event.Determinator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EventCommandService implements EventManageUseCase {
    private final MemberGetUseCase memberGetUseCase;
    private final EventGetUseCase eventGetUseCase;

    private final Determinator determinator;

}
