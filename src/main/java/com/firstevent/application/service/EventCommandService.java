package com.firstevent.application.service;

import com.firstevent.adapter.dto.EventRequestDto;
import com.firstevent.application.ports.in.EventManageUseCase;
import com.firstevent.application.ports.out.EventRepository;
import com.firstevent.domain.event.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventCommandService implements EventManageUseCase {
    private final EventRepository eventRepository;

    @Override
    public Event regist(EventRequestDto requestDto) {
        return eventRepository.save(Event.regist(requestDto));
    }

    @Override
    public Event update(Long id, EventRequestDto eventRequestDto) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id에 해당하는 이벤트가 없습니다."));
        event.update(eventRequestDto);
        return event;
    }
}
