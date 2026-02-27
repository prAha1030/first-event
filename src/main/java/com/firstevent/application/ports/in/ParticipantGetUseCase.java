package com.firstevent.application.ports.in;

import com.firstevent.adapter.dto.CursorPage;
import com.firstevent.domain.event.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParticipantGetUseCase {
    Long countWinner(Long eventId);
    boolean exists(Long eventId, Long memberId);
    Page<Participant> getAll(Long eventId, Pageable pageable);

    CursorPage<Participant> getAllByCursor(Long eventId, Long cursor, int size);
}
