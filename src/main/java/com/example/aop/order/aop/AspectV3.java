package com.example.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV3 {

    /**
     * 반환 타입은 'void' 여야 한다.
     * 다른 곳에서 이 포인트컷을 사용하려면 public 이어야 하고 이 내부 안에서 사용하는 건 private 이어도 된다.
     * */
    @Pointcut("execution(* com.example.aop.order..*(..))")
    private void allOrder() {} // Pointcut Signature

    @Pointcut("execution(* *..*Service.*(..))")
    private void allService() {}

    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[doLog] {}", joinPoint.getSignature()); // JoinPoint Signature
        return joinPoint.proceed(); // 실제 Target 호출
    }

    /**
     * com.example.aop.order 의 모든 하위 패키지 이면서 타입(클래스, 인터페이스) 이름 패턴이 *Service
     * '&&'라서 두 조건 모두를 만족해야한다.
     * */
    @Around("allOrder() && allService()")
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
