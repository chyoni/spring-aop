package com.example.aop.pointcut;

import com.example.aop.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * 솔직히 어려운 것에 비해 this, target 둘 다 자주 사용되진 않기 때문에 그냥 아 이런게 있구나 하고 넘어가자.
 *
 * This, Target 모두 부모 타입을 허용한다. 무슨 말이냐면 예를 들어 This 같은 경우 AOP Proxy 객체를 보고 판단한다고 했는데,
 * 세팅하기를 스프링한테 JDK 동적 프록시를 기본으로 프록시를 만들어줘 ! 라고 설정하면
 * MemberServiceProxy 같은 경우 이 프록시가 상속받는게 MemberService다. 왜냐하면 JDK 동적 프록시는 Interface를 기반으로 프록시를 만드니까.
 * 이 때 this(com.example.aop.member.MemberService) 라고 하면 this 는 Proxy 객체를 본다고 했는데 Proxy 객체의 부모가 MemberService다.
 * 그래서 this(com.example.aop.member.MemberService)는 AOP 적용 대상이 된다.
 * */
@Slf4j
@Import(ThisTargetTest.ThisTargetAspect.class)
@SpringBootTest
public class ThisTargetTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy = {}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Aspect
    static class ThisTargetAspect {

        @Around("this(com.example.aop.member.MemberService)")
        public Object doThisInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        @Around("target(com.example.aop.member.MemberService)")
        public Object doTargetInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        @Around("this(com.example.aop.member.MemberServiceImpl)")
        public Object doThisConcrete(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-impl] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        @Around("target(com.example.aop.member.MemberServiceImpl)")
        public Object doTargetConcrete(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-impl] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }
}
