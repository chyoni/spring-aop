package com.example.aop.pointcut;

import com.example.aop.member.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

/**
 * Within은 타입(클래스, 인터페이스)을 지정하면 그 안에 메서드는 모두 매치가 되게 하는 방법
 * */
public class WithinTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class); // method 이름이 hello, 파라미터의 타입이 String
    }

    @Test
    void withinExactly() {
        pointcut.setExpression("within(com.example.aop.member.MemberServiceImpl)");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void withinWildCard() {
        pointcut.setExpression("within(com.example.aop.member.*Service*)");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void withinSubPackage() {
        pointcut.setExpression("within(com.example.aop..*)");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("타겟의 정확하게 타입에만 직접 적용해야 한다")
    void withinSuperTypeFalse() {
        // 상위 타입으로 설정하면 within 은 안된다. 정확히 그 타입으로 지정해야 한다. execution 은 이게 가능했는데 within 은 아니다.
        pointcut.setExpression("within(com.example.aop.member.MemberService)");

        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }
}
