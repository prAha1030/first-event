package com.firstevent.adapter.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EventRequestDto {
    private String title;
    private String description;
    private Integer capacity;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public EventRequestDto(String title, String description, Integer capacity,
                           LocalDateTime startAt, LocalDateTime endAt) {
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
