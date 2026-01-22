package com.firstevent.domain.event;

import com.firstevent.adapter.dto.EventRequestDto;
import com.firstevent.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 600)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT") // clob, blob
    private String description;

    @Column(nullable = false)
    private Integer capacity;

    @Embedded
    private EventPeriod period;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Participant> participants = new ArrayList<>();

    public static Event regist(EventRequestDto requestDto) {

        Objects.requireNonNull(requestDto.getTitle());
        Objects.requireNonNull(requestDto.getDescription());
        Objects.requireNonNull(requestDto.getCapacity());

        Event event = new Event();
        event.title = requestDto.getTitle();
        event.description = requestDto.getDescription();
        event.capacity = requestDto.getCapacity();

        event.period = EventPeriod.of(requestDto.getStartAt(), requestDto.getEndAt());

        event.status = EventStatus.PENDING;

        return event;
    }

    public void start() {
        this.status = EventStatus.STARTED;
        this.period.start();
    }

    public void finish() {
        this.status = EventStatus.FINISHED;
    }

    public void update(EventRequestDto requestDto) {
        validToUpdate();

        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.capacity = requestDto.getCapacity();
        this.period.update(requestDto.getStartAt(), requestDto.getEndAt());
    }

    private void validToUpdate() {
        if (this.status == EventStatus.STARTED) {
            throw new IllegalStateException("시작된 이벤트는 수정할 수 없습니다.");
        }
    }

    public void participate(Member member, Determination determination) {
        validToParticipate(member);
        participants.add(Participant.regist(member, this, determination));
    }

    private void validToParticipate(Member member) {

        if (this.status != EventStatus.STARTED) {
            throw new IllegalStateException("시작된 이벤트가 아니면 참여할 수 없습니다.");
        }

        int winnerCount = 0;
        for (Participant participant : participants) {
            if(participant.getMemberId().equals(member.getId())) {
                throw new IllegalArgumentException("이벤트에는 중복 참여할 수 없습니다.");
            }

            winnerCount += participant.isWinner() ? 1 : 0;
        }

        if (winnerCount >= capacity) {
            this.finish();
            throw new IllegalStateException("당첨자가 초과하여 이벤트가 종료되었습니다.");
        }
    }
}