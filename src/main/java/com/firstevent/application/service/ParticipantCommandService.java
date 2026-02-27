package com.firstevent.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sparta.firstevent.application.ports.in.EventGetUseCase;
import sparta.firstevent.application.ports.in.MemberGetUseCase;
import sparta.firstevent.application.ports.in.ParticipantGetUseCase;
import sparta.firstevent.application.ports.in.ParticipantManageUseCase;
import sparta.firstevent.application.ports.out.EventParticipantCountRepository;
import sparta.firstevent.application.ports.out.ParticipantRepository;
import sparta.firstevent.domain.event.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ParticipantCommandService implements ParticipantManageUseCase {

    private final EventGetUseCase eventGetUseCase;
    private final MemberGetUseCase memberGetUseCase;
    private final ParticipantGetUseCase participantGetUseCase;

    private final ParticipantRepository participantRepository;
    private final EventParticipantCountRepository eventParticipantCountRepository;

    private final Determinator determinator;


//    @Override
//    public Participant apply(Long eventId, Long memberId) {
//        validateApply(eventId, memberId);
//
//        Participant participant = participantRepository.save(Participant.regist(memberId, eventId, determinator)); // 새로운 row를 INSERT하여 충돌 없이 1000개 성공
//        EventParticipantCount participantCount = eventParticipantCountRepository.findByEventIdWithLock(eventId) // 같은 row를 동시에 READ -> 같은 값을 읽음
//            .orElse(EventParticipantCount.regist(eventId));
//
//        if (participant.isWinner()) {
//            participantCount.updateWithWinner();
//        } else {
//            participantCount.update(); // 불러온 값을 MODIFY -> Java 메모리에서 +1
//        }
//
//        eventParticipantCountRepository.save(participantCount); // 수정한 값을 WRITE -> 같은 값을 덮어씌워 손실 발생 -> 참여자 수 390으로 조회

        /* 이를 해결하기 위한 방안

            방법 1: 비관적 락 (트래픽이 적을 때 사용)

            한 트랜잭션이 끝날 때까지 다른 스레드가 대기
            But, 트래픽이 많을 경우 스레드가 줄을 서기 때문에 병목 문제 발생

            방법 2: JPQL atomic update
            JPQL을 활용하여 DB레벨에서 직접 +1 처리하여 Race Condition 없음

            방법 3: Redis INCR
            Redis INCR 명령어는 지정된 키의 정수 값을 1만큼 원자적으로 증가시키는 명령어 (읽고 더하고 저장하는 과정이 하나의 명령어로 처리된다.)
            이를 통해 DB 락 없이 메모리 레벨에서 즉시 관리 가능
            Redis를 본래 DB 앞에 배치하여 보조 저장소 추가하여 진행 -> 락을 걸지 않아 트래픽이 많아도 성능상 문제가 발생하지 않음

            사용자 응답은 즉시 업데이트된 숫자를 보여주고,
            DB 동기화는 스케줄러 또는 비동기 방식(메시지 큐 등)을 통해 나중에 천천히 반영

            방법 4: Redis SET NX
            Redis SET NX를 통한 분산 락 구현 (분산 락: 내가 지금 이 데이터를 사용 중!이라는 깃발을 꽂는 행위? TODO 좀더 탐색)
            4-1. 락 획득 시도: SET lock_key "unique_id" NX PX 3000
                4-1-1. NX: 키가 없을 때만 성공 (이미 누가 락을 잡고 있으면 실패)
                4-1-2. PX 3000: 3초 뒤에 자동으로 삭제 (프로그램이 뻗어도 무한 락에 걸리지 않게 방지)
            4-2. 비즈니스 로직 수행: 락을 획득한 클라이언트만 DB 데이터를 수정하거나 중요한 작업을 합니다
            4-3. 락 해제: 작업이 끝나면 Redis에서 해당 키를 삭제합니다

            실무 ex) 실무에서는 직접 구현하기보다 Redisson이라는 라이브러리를 가장 많이 씁니다. 내부적으로 위 메커니즘을 아주 안전하게 처리해주기 때문!

            public void updateStockWithLock(String lockKey) {
                // 1. 락 객체 가져오기
                RLock lock = redissonClient.getLock(lockKey);

                try {
                    // 2. 락 획득 시도 (최대 5초 대기, 2초간 유지)
                    boolean isLocked = lock.tryLock(5, 2, TimeUnit.SECONDS);

                    if (isLocked) {
                        // 3. 락 획득 성공 시 비즈니스 로직 (예: 재고 차감)
                        System.out.println("락 획득! 안전하게 작업 중...");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    // 4. 작업 완료 후 반드시 락 해제
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }

         */
//        return participant;
//    }

    @Override
    public Participant apply(Long eventId, Long memberId) {
        validateApply(eventId, memberId);

        Participant participant = participantRepository.save(Participant.regist(memberId, eventId, determinator));

        if (participant.isWinner()) {
            eventParticipantCountRepository.incrementWithWinner(eventId);
        } else {
            eventParticipantCountRepository.incrementParticipantCount(eventId);
        }

        return participant;
    }

    private void validateApply(Long eventId, Long memberId) {
        Event event = eventGetUseCase.get(eventId);
        memberGetUseCase.get(memberId);
        Long participantCount = participantGetUseCase.countWinner(eventId);

        if (!event.getStatus().equals(EventStatus.STARTED)) {
            throw new IllegalStateException("진행중인 이벤트가 아닙니다.");
        }

        if (participantCount >= event.getCapacity()) {
            throw new IllegalStateException("당첨자 수에 도달하여 이벤트가 종료되었습니다.");
        }

        if (participantGetUseCase.exists(eventId, memberId)) {
            throw new IllegalStateException("이벤트에 중복 참여할 수 없습니다.");
        }
    }
}
