package com.firstevent.adapter.infra;

import com.firstevent.domain.event.Determinator;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomDeterminator implements Determinator {
    private static final double probability = 0.1; // 10% 확률로 당첨

    @Override
    public boolean determinate() {
        return ThreadLocalRandom.current().nextDouble() < probability;
    }
}
