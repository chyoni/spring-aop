package com.example.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV4Pointcut {

    @Around("com.example.aop.order.aop.Pointcuts.allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[doLog] {}", joinPoint.getSignature()); // JoinPoint Signature
        return joinPoint.proceed(); // 실제 Target 호출
    }

    /**
     * com.example.aop.order 의 모든 하위 패키지 이면서 타입(클래스, 인터페이스) 이름 패턴이 *Service
     * '&&'라서 두 조건 모두를 만족해야한다.
     * */
    @Around("com.example.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.info("[doTransaction] Start {}", joinPoint.getSignature());
            Object proceed = joinPoint.proceed();
            log.info("[doTransaction] Commit {}", joinPoint.getSignature());

            return proceed;
        } catch (Exception e) {
            log.info("[doTransaction] Rollback {}", joinPoint.getSignature());
            throw e;
        } finally {
            log.info("[doTransaction] Release {}", joinPoint.getSignature());
        }
    }
}
