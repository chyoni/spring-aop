package com.example.aop.exam.aop;

import com.example.aop.exam.trace.LogTrace;
import com.example.aop.exam.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class LogTraceAspect {
    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Around("@within(com.example.aop.exam.annotation.LogTrace)")
    public Object logTraceAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;

        try {
            // Ex) "OrderController.request()"
            String message = joinPoint.getSignature().toShortString();

            status = logTrace.begin(message);

            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
