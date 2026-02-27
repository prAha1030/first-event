package com.firstevent.adapter.api;

import com.firstevent.adapter.api.docs.EventApi;
import com.firstevent.adapter.dto.EventResponseDto;
import com.firstevent.adapter.dto.ParticipantResponseDto;
import com.firstevent.application.ports.in.EventGetUseCase;
import com.firstevent.application.ports.in.ParticipantManageUseCase;
import com.firstevent.domain.event.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController implements EventApi {
    private final ParticipantManageUseCase participantManageUseCase;
    private final EventGetUseCase eventGetUseCase;

    @PostMapping("/{eventId}/participate/{memberId}")
    public ParticipantResponseDto participate(Long eventId, Long memberId) {
        return ParticipantResponseDto.from(participantManageUseCase.apply(eventId, memberId));
    }

    @GetMapping
    public Page<EventResponseDto> getAll(Pageable pageable) {
        Page<Event> events = eventGetUseCase.getAll(pageable);

        return events.map(e -> EventResponseDto.fromEntity(e));
    }

    @GetMapping("/{id}")
    public EventResponseDto getEvent(@PathVariable Long id) {
        return EventResponseDto.fromEntity(eventGetUseCase.get(id));
    }
}
