package com.example.aop.exam;

import com.example.aop.exam.aop.LogTraceAspect;
import com.example.aop.exam.trace.LogTrace;
import com.example.aop.exam.trace.ThreadLocalLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }

    @Bean
    public LogTraceAspect logTraceAspect() {
        return new LogTraceAspect(logTrace());
    }
}
