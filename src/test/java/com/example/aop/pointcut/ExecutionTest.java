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

    @Test
    void typeExactMatch() {
        pointcut.setExpression("execution(* com.example.aop.member.MemberServiceImpl.*(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchSuperType() {
        // 상위 타입으로 expression 을 설정
        pointcut.setExpression("execution(* com.example.aop.member.MemberService.*(..))");

        // pointcut 은 상위 타입이고 상위 타입이 가지고 있는 메서드면 자식 메서드도 역시 가능하다. 이유는 자식은 부모에 들어갈 수 있으니까.
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchInternal() throws NoSuchMethodException {
        // 상위 타입으로 expression 을 설정
        pointcut.setExpression("execution(* com.example.aop.member.MemberService.*(..))");

        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);

        // 상위 타입으로 pointcut 의 expression 을 설정한 경우, 상위 타입이 가지고 있는 메서드만 가능하다.
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
    }

    /**
     * 모든 메서드 중 파라미터가 String 타입 하나 인 것들을 매치
     * */
    @Test
    void argsMatch() {
        pointcut.setExpression("execution(* *(String))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 모든 메서드 중 파라미터가 없는 것들을 매치
     * */
    @Test
    void argsMatchNoArgs() {
        pointcut.setExpression("execution(* *())");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    /**
     * 모든 메서드 중 모든 타입을 허용하지만 딱 한 개의 파라미터만 허용
     * */
    @Test
    void argsMatchWildCard() {
        pointcut.setExpression("execution(* *(*))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 모든 메서드 중 모든 타입, 모든 개수의 파라미터를 허용
     * */
    @Test
    void argsMatchAll() {
        pointcut.setExpression("execution(* *(..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 모든 메서드 중 파라미터가 String 타입으로 시작하고 그 이후는 모든 타입, 모든 개수의 파라미터를 허용 또는 없어도 된다.
     * */
    @Test
    void argsMatchComplex() {
        pointcut.setExpression("execution(* *(String, ..))");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 모든 메서드 중 파라미터가 딱 두개이면서 둘 다 String 타입인 것
     * */
    @Test
    void argsMatchComplexExactly() throws NoSuchMethodException {
        pointcut.setExpression("execution(* *(String, String))");

        Method twoParamsMethod = MemberServiceImpl.class.getMethod("twoParams", String.class, String.class);

        assertThat(pointcut.matches(twoParamsMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 모든 메서드 중 파라미터가 딱 두개이면서 첫번째는 String, 두번째는 모든 타입
     * */
    @Test
    void argsMatchComplexExactly2() throws NoSuchMethodException {
        pointcut.setExpression("execution(* *(String, *))");

        Method twoParamsMethod = MemberServiceImpl.class.getMethod("twoParams", String.class, String.class);

        assertThat(pointcut.matches(twoParamsMethod, MemberServiceImpl.class)).isTrue();
    }
}
