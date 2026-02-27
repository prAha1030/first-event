package com.firstevent.application.service;

import com.firstevent.application.ports.in.AdminEventGetUseCase;
import com.firstevent.application.ports.out.EventRepository;
import com.firstevent.domain.event.Event;
import com.firstevent.domain.event.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminEventQueryService implements AdminEventGetUseCase {
    private final EventRepository eventRepository;

    @Override
    public Page<Event> getAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    @Override
    public Event get(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id에 해당하는 이벤트가 없습니다."));
    }

    @Override
    public List<Participant> getWinners(Long eventId) {
        return List.of();
    }
}
