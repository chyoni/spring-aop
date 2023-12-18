package com.example.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 스프링은 프록시 방식의 AOP 를 사용한다. 즉, 프록시를 통하는 메서드만 적용 대상이 된다.
 * 그래서 아래 @Around() 에 정규 표현식도 특정 패키지의 특정 메서드명의 특정 파라미터를 정규식으로 작성하는 것.
 *
 * 스프링 AOP 는 AspectJ의 문법을 차용하고 프록시 방식의 AOP 를 제공한다. AspectJ를 직접 사용하는 것이 아니다.
 * 스프링 AOP 를 사용할 땐 @Aspect 애노테이션을 주로 사용하는데 이 애노테이션도 AspectJ가 제공하는 애노테이션이다.
 * */
@Slf4j
@Aspect
public class AspectV1 {

    @Around("execution(* com.example.aop.order..*(..))")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[doLog] {}", joinPoint.getSignature()); // JoinPoint Signature
        return joinPoint.proceed(); // 실제 Target 호출
    }
}
