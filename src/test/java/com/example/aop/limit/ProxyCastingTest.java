package com.example.aop.limit;

import com.example.aop.member.MemberService;
import com.example.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

@Slf4j
public class ProxyCastingTest {

    @Test
    void jdkProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        proxyFactory.setProxyTargetClass(false); // Not 'CGLIB' But 'JDK Dynamic Proxy'

        MemberService proxy = (MemberService) proxyFactory.getProxy();

        // JDK 동적 프록시는 무조건 인터페이스를 가지고 프록시를 만들기 때문에 프록시를 가져와서 구체 클래스로 캐스팅을 못한다. 왜냐하면 프록시는 구체클래스에 대한 정보가 아예 없기 때문에.
        Assertions.assertThatThrownBy(() -> {
            MemberServiceImpl casting = (MemberServiceImpl) proxy;
        }).isInstanceOf(ClassCastException.class);
    }

    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        proxyFactory.setProxyTargetClass(true); // CGLIB

        // CGLIB 는 캐스팅이 가능하다. 왜냐하면 CGLIB 는 인터페이스가 있어도 구체 클래스로 프록시를 만든다. 그럼 프록시의 부모는 구체 클래스고 구체 클래스의 부모는 인터페이스이기 때문에
        // 캐스팅은 당연히 가능하다.
        MemberService proxy = (MemberService) proxyFactory.getProxy();
        log.info("proxy = {}", proxy.getClass());

        // 이건 더할 나위 없이 가능하다. 그냥 프록시 자체가 구체 클래스로 만들었기 때문에 프록시의 부모는 구체 클래스
        MemberServiceImpl casting = (MemberServiceImpl) proxy;
        log.info("casting = {}", casting.getClass());
    }
}
