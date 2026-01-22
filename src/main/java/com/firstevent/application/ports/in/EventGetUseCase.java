package com.firstevent.application.ports.in;

import com.firstevent.domain.event.Event;

import java.util.List;

public interface EventGetUseCase {
    List<Event> getAll();
}
