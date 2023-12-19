package com.example.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

/**
 * 프록시에 어드바이저 두 개가 있을 때 어드바이스의 순서를 변경 또는 지정할 수 있는데 그 방법은 이렇게
 * 클래스로 어드바이저 순서를 지정해줘야 한다. 근데 순서가 다르다고 어드바이저 별로 클래스를 만들기는 귀찮으니까
 * 이렇게 이너 클래스로 만들어서 정리하면 된다. 그리고 그 이너 클래스가 빈으로 등록되면 된다.
 * */
@Slf4j
public class AspectV5Order {

    @Aspect
    @Order(2)
    public static class LogAspect {
        @Around("com.example.aop.order.aop.Pointcuts.allOrder()")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[doLog] {}", joinPoint.getSignature()); // JoinPoint Signature
            return joinPoint.proceed(); // 실제 Target 호출
        }
    }

    @Aspect
    @Order(1)
    public static class TxAspect {

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
}
