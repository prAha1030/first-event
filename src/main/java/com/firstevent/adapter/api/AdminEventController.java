package com.firstevent.adapter.api;

import com.firstevent.adapter.dto.CursorPage;
import com.firstevent.adapter.dto.EventRequestDto;
import com.firstevent.adapter.dto.EventResponseDto;
import com.firstevent.adapter.dto.ParticipantResponseDto;
import com.firstevent.application.ports.in.AdminEventGetUseCase;
import com.firstevent.application.ports.in.AdminEventManageUseCase;
import com.firstevent.application.ports.in.ParticipantGetUseCase;
import com.firstevent.domain.event.Event;
import com.firstevent.domain.event.Participant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/admin/events")
@RestController
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventManageUseCase adminEventManageUseCase;
    private final AdminEventGetUseCase adminEventGetUseCase;
    private final ParticipantGetUseCase participantGetUseCase;

    @PostMapping
    public EventResponseDto registEvent(@Valid @RequestBody EventRequestDto requestDto) {
        Event event = adminEventManageUseCase.regist(requestDto);
        return new EventResponseDto(event.getId(), event.getTitle(), event.getStatus());
    }

    @PatchMapping("/{eventId}")
    public EventResponseDto modifyEvent(@PathVariable Long eventId, @Valid @RequestBody EventRequestDto requestDto) {
        Event event = adminEventManageUseCase.update(eventId, requestDto);
        return new EventResponseDto(event.getId(), event.getTitle(), event.getStatus());
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        adminEventManageUseCase.delete(eventId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{eventId}/participants")
    public Page<ParticipantResponseDto> getParticipants(@PathVariable Long eventId, @PageableDefault Pageable pageable) {
        Page<Participant> participants = participantGetUseCase.getAll(eventId, pageable);

        return participants.map(ParticipantResponseDto::from);
    }

    @GetMapping("/{eventId}/participants/cursor")
    public CursorPage<ParticipantResponseDto> getParticipantsWithCursor(
        @PathVariable Long eventId,
        @RequestParam(required = false) Long cursor,
        @RequestParam int size) {

        CursorPage<Participant> participants = participantGetUseCase.getAllByCursor(eventId, cursor, size);

        return participants.map(ParticipantResponseDto::from);
    }

}
