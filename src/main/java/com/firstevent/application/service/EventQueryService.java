package com.firstevent.application.service;

import com.firstevent.application.ports.in.EventGetUseCase;
import com.firstevent.application.ports.out.EventRepository;
import com.firstevent.domain.event.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventQueryService implements EventGetUseCase {
    private final EventRepository eventRepository;

    @Override
    public List<Event> getAll() {
        return eventRepository.findAll();
    }
}
