package com.firstevent.application.ports.in;

import com.firstevent.domain.event.Event;
import com.firstevent.domain.event.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventGetUseCase {
    Page<Event> getAll(Pageable pageable);

    Event get(Long id);

    Event getWithStatus(Long id, EventStatus status);
}
