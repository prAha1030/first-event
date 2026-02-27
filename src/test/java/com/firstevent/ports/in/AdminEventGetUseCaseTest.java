package com.firstevent.ports.in;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import com.firstevent.adapter.dto.EventRequestDto;
import com.firstevent.adapter.dto.MemberRequestDto;
import com.firstevent.application.ports.out.EventRepository;
import com.firstevent.application.ports.out.MemberRepository;
import com.firstevent.application.ports.out.ParticipantRepository;
import com.firstevent.domain.event.Event;
import com.firstevent.domain.event.EventFixture;
import com.firstevent.domain.event.Participant;
import com.firstevent.domain.member.Member;
import com.firstevent.domain.member.MemberFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AdminEventGetUseCaseTest {

    @Autowired
    AdminEventGetUseCase adminEventGetUseCase;

    @Autowired
    EventManageUseCase eventManageUseCase;

    @Autowired
    AdminEventManageUseCase adminEventManageUseCase;

    @Autowired
    MemberManageUseCase memberManageUseCase;

    @Autowired
    ParticipantManageUseCase participantManageUseCase;

    @Autowired
    ParticipantGetUseCase participantGetUseCase;

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void page() {

        // given
        adminEventManageUseCase.regist(EventFixture.createEventRequestDto("title 1"));
        adminEventManageUseCase.regist(EventFixture.createEventRequestDto("title 2"));
        adminEventManageUseCase.regist(EventFixture.createEventRequestDto("title 3"));

        entityManager.flush();
        entityManager.clear();


        // when
        Pageable page = PageRequest.of(0, 2, Sort.by("id").descending());
        Page<Event> pagedEvents = adminEventGetUseCase.getAll(page);


        // then
        assertThat(pagedEvents).hasSize(2);
        assertThat(pagedEvents.getTotalElements()).isEqualTo(3);
        assertThat(pagedEvents.getContent().get(0).getTitle()).isEqualTo("title 3");
    }
    
    @Test
    void getParticipants() {

        // given
        Member savedMember = memberRepository.save(MemberFixture.registMemberWithoutId());
        Event savedEvent = eventRepository.save(EventFixture.registEvent());
        savedEvent.start();

        entityManager.flush();
        entityManager.clear();

        // when
        participantManageUseCase.apply(savedEvent.getId(), savedMember.getId());

        assertThat(participantRepository.countByEventId(savedEvent.getId())).isEqualTo(1);

        entityManager.flush();
        entityManager.clear();

        Pageable page = PageRequest.of(0, 2, Sort.by("id").descending());

        // then
        Page<Participant> participants = participantGetUseCase.getAll(savedEvent.getId(), page);

        assertThat(participants.getTotalElements()).isEqualTo(1L);
    }

}