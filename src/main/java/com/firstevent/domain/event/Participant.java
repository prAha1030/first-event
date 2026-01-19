package com.firstevent.domain.event;

import com.firstevent.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Participant {
    private Member member;
    private Event event;
    private boolean isWinner;
    private LocalDateTime participateAt;

    public static Participant regist(Member member, Event event, Determination determination) {
        Participant participant = new Participant();

        participant.member = member;
        participant.event = event;
        participant.isWinner = determination.determinate();
        participant.participateAt = LocalDateTime.now();

        return participant;

    }
}
