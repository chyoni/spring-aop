package com.example.aop.pointcut;

import com.example.aop.member.MemberService;
import com.example.aop.member.annotation.ClassAop;
import com.example.aop.member.annotation.MethodAop;
import com.example.aop.member.annotation.MethodTwoAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ParameterTest.ParameterAspect.class)
@SpringBootTest
public class ParameterTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy = {}", memberService.getClass());
        memberService.hello("helloParameter");
    }

    @Aspect
    static class ParameterAspect {
        // 모든 반환 타입(*)의 com.example.aop.member 의 하위 모든 패키지(..)의 모든 타입(*)[Class, Interface]의 모든 메서드(*)의 모든 파라미터(..)에 적용
        @Pointcut("execution(* com.example.aop.member..*.*(..))")
        private void allMember() {}

        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object args1 = joinPoint.getArgs()[0];
            log.info("[logArgs1]{}, arg = {}", joinPoint.getSignature(), args1);
            return joinPoint.proceed();
        }

        @Around("allMember() && args(arg, ..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[logArgs2]{}, arg = {}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }

        @Before("allMember() && args(arg, ..)")
        public void logArgs3(String arg) {
            log.info("[logArgs3] arg = {}", arg);
        }

        /**
         * this 는 프록시를 받는다.
         * */
        @Before("allMember() && this(obj)")
        public void thisArgs(JoinPoint joinPoint, Object obj) {
            log.info("[this]{}, obj = {}", joinPoint.getSignature(), obj.getClass());
        }

        /**
         * target 은 실제 객체를 받는다.
         * */
        @Before("allMember() && target(obj)")
        public void targetArgs(JoinPoint joinPoint, Object obj) {
            log.info("[target]{}, obj = {}", joinPoint.getSignature(), obj.getClass());
        }

        /**
         * 클래스 대상의 애노태이션을 가져올 때 사용하는 @target
         * */
        @Before("allMember() && @target(annotation)")
        public void atTargetArgs(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@target]{}, obj = {}", joinPoint.getSignature(), annotation);
        }

        /**
         * 클래스 대상의 애노태이션을 가져올 때 사용하는 @within
         * */
        @Before("allMember() && @within(annotation)")
        public void atWithinArgs(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@within]{}, obj = {}", joinPoint.getSignature(), annotation);
        }

        @Before("allMember() && @annotation(annotation)")
        public void atAnnotationAcceptedArgs(JoinPoint joinPoint, MethodAop annotation) {
            log.info("[@annotation Accepted]{}, annotationValue = {}", joinPoint.getSignature(), annotation.value());
        }

        /**
         * 파라미터의 타입이 올바르지 않은 경우엔 어드바이스가 적용되지 않는다.
         * 올바르지 않는다는 뜻은 MethodTwoAop 애노테이션을 적용한 메서드 중 allMember() 포인트컷에 적용되는 메서드가 없다는 뜻이다.
         * */
        @Before("allMember() && @annotation(annotation)")
        public void atAnnotationNotAcceptedArgs(JoinPoint joinPoint, MethodTwoAop annotation) {
            log.info("[@annotation Not Accepted]{}, annotationValue = {}", joinPoint.getSignature(), annotation.value());
        }
    }
}
