package com.example.aop.pointcut.annotation;

import com.example.aop.member.annotation.ClassAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;


/**
 * 클래스에 붙이는 애노테이션을 기반으로 포인트컷을 만들 땐 @target, @within 을 사용할 수 있다.
 * */
@Slf4j
@Import(AtTargetAtWithinTest.Config.class)
@SpringBootTest
public class AtTargetAtWithinTest {

    @Autowired Child child;

    @Test
    void success() {
        log.info("child proxy = {}", child.getClass());

        child.childMethod();
        child.parentMethod();
    }

    static class Config {

        @Bean
        public Parent parent() { return new Parent(); }

        @Bean
        public Child child() { return new Child(); }

        @Bean
        public AtTargetAtWithinAspect atTargetAtWithinAspect() { return new AtTargetAtWithinAspect(); }
    }

    static class Parent {
        public void parentMethod() {
            log.info("[parentMethod] Start");
        }
    }

    @ClassAop
    static class Child extends Parent {
        public void childMethod() {
            log.info("[childMethod] Start");
        }
    }

    @Aspect
    static class AtTargetAtWithinAspect {

        // @target: 인스턴스 기준으로 모든 메서드의 조인 포인트를 선정 = 부모 타입의 메서드도 적용
        @Around("execution(* com.example.aop..*(..)) && @target(com.example.aop.member.annotation.ClassAop)")
        public Object atTarget(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@target] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        // @within: 선택된 클래스 내부에 있는 메서드만 조인 포인트로 선정 = 부모 타입의 메서드는 적용되지 않음
        @Around("execution(* com.example.aop..*(..)) && @within(com.example.aop.member.annotation.ClassAop)")
        public Object atWithin(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@within] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        // 참고로 @target, @args, args 이런 포인트컷 지시자는 단독으로 사용하면 안된다. 위 에제에서도 execution 과 같이 사용했는데
        // 그 이유는 스프링이 이런 포인트컷 지시자가 있으면 모든 스프링 빈에 AOP 를 적용하려고 시도하는데 스프링이 내부에서 사용하는 빈 중에는 final 로
        // 지정된 빈들도 있기 때문에 오류가 발생할 수 있다.
    }
}
