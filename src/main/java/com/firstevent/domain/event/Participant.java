package com.firstevent.domain.event;

import com.firstevent.domain.member.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Participant {
    private Member member;
    private Event event;
    private LocalDateTime participateAt;

    public Participant(Member member, Event event) {
        this.member = member;
        this.event = event;
        participateAt = LocalDateTime.now();
    }
}
