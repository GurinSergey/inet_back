package com.sgurin.inetback.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimedConf {
    @Bean
    public TimedAspect timedAspect (MeterRegistry meterRegistry){
        return new TimedAspect(meterRegistry);
    }
}
