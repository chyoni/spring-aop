package com.example.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 포인트컷과 어드바이스를 분리하는 법.
 * */
@Slf4j
@Aspect
public class AspectV2 {

    /**
     * 반환 타입은 'void' 여야 한다.
     * 다른 곳에서 이 포인트컷을 사용하려면 public 이어야 하고 이 내부 안에서 사용하는 건 private 이어도 된다.
     * */
    @Pointcut("execution(* com.example.aop.order..*(..))")
    private void allOrder() {

    } // Pointcut Signature

    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[doLog] {}", joinPoint.getSignature()); // JoinPoint Signature
        return joinPoint.proceed(); // 실제 Target 호출
    }
}
