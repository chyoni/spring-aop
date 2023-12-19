package com.example.aop.pointcut;

import com.example.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class); // method 이름이 hello, 파라미터의 타입이 String
    }

    @Test
    void printMethod() {
        log.info("helloMethod = {}", helloMethod);
    }

    @Test
    void exactMatch() {
        pointcut.setExpression("execution(public String com.example.aop.member.MemberServiceImpl.hello(String))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void allMatch() {
        pointcut.setExpression("execution(* *(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatch() {
        pointcut.setExpression("execution(* hello(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameWildcardMatch() {
        pointcut.setExpression("execution(* hel*(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameWildcardMatch2() {
        pointcut.setExpression("execution(* *el*(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void notNameMatch() {
        pointcut.setExpression("execution(* notMatched(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageExactMatch() {
        pointcut.setExpression("execution(* com.example.aop.member.MemberServiceImpl.hello(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactMatch2() {
        pointcut.setExpression("execution(* com.example.aop.member.*.*(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageNotMatch() {
        // 패키지에서 (.)은 정확히 그 위치. 즉, 아래같은 경우 com.example.aop 딱 그 위치를 말한다.
        pointcut.setExpression("execution(* com.example.aop.*.*(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void subPackageMatch() {
        // 패키지에서 하위 패키지까지 몽땅 포함하려면 (..)이어야 한다.
        pointcut.setExpression("execution(* com.example.aop..*.*(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void subPackageMatch2() {
        pointcut.setExpression("execution(* com.example.aop.member..*.*(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
