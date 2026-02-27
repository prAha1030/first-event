package com.firstevent.adapter.infra;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import com.firstevent.domain.event.Determinator;

@TestConfiguration
public class TestConfig {

    @Bean
    public Determinator determinator() {
        return new Determinator() {
            @Override
            public boolean determinate() {
                return true;
            }
        };
    }
}
