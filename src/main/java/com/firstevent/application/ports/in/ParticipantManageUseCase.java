package com.firstevent.application.ports.in;

import com.firstevent.domain.event.Participant;

public interface ParticipantManageUseCase {
    Participant apply(Long eventId, Long memberId);
}
