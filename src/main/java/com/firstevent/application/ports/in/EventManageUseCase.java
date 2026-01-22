package com.firstevent.application.ports.in;

import com.firstevent.adapter.dto.EventRequestDto;
import com.firstevent.domain.event.Event;

public interface EventManageUseCase {
    Event regist(EventRequestDto dto);

    Event update(Long id, EventRequestDto eventRequestDto);
}
