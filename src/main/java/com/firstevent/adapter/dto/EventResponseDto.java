package com.firstevent.adapter.dto;

import com.firstevent.domain.event.Event;
import com.firstevent.domain.event.EventStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record EventResponseDto(
    @Schema(description = "이벤트 아이디", example = "1")
    Long id,
    @Schema(description = "이벤트 제목", example = "선착순 당첨자 100명 커피쿠폰")
    String title,
    @Schema(description = "이벤트 상태", example = "PENDING: 대기중, STARTED: 진행중, FINISHED: 종료")
    EventStatus status
) {
    public static EventResponseDto fromEntity(Event e) {
        return new EventResponseDto(e.getId(), e.getTitle(), e.getStatus());
    }
}
