package com.firstevent.domain.event;

import com.firstevent.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_participant_event", columnNames = {"memberId", "eventId"})})
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Participant {
    // 참여 식별자, 멤버, 이벤트, 당첨, 참여일
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long memberId;
    @Column(nullable = false, insertable = false, updatable = false)
    private Long eventId;
    @Column(nullable = false)
    private boolean isWinner;
    @Column(nullable = false)
    private LocalDateTime participateAt;

    public static Participant regist(Member member, Event event, Determination determination) {
        Participant participant = new Participant();

        participant.memberId = member.getId();
        participant.eventId = event.getId();
        participant.isWinner = determination.determinate();
        participant.participateAt = LocalDateTime.now();

        return participant;

    }
}
