package com.firstevent.application.ports.in;

import com.firstevent.adapter.dto.EventRequestDto;
import com.firstevent.domain.event.Event;

public interface AdminEventManageUseCase {

    Event terminate(Long id);

    Event regist(EventRequestDto dto);

    Event update(Long id, EventRequestDto eventRequestDto);

    void delete(Long id);

    void start(Long id);
}
