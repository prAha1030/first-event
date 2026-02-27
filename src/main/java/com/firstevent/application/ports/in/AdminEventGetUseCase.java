package com.firstevent.application.ports.in;

import com.firstevent.domain.event.Event;
import com.firstevent.domain.event.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminEventGetUseCase {
    Page<Event> getAll(Pageable pageable);

    Event get(Long id);

//    Page<Participant> getParticipants(Long eventId, Pageable pageable);

    List<Participant> getWinners(Long eventId);
}
