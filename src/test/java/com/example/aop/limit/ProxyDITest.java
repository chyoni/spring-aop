package com.example.aop.limit;

import com.example.aop.limit.code.ProxyDIAspect;
import com.example.aop.member.MemberService;
import com.example.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


/**
 * 프록시 기술의 한계 중 하나인 의존 관계 주입 시 문제를 알아보자.
 * 스프링한테 프록시 만드는 기본값을 JDK Dynamic Proxy로 설정해주면 인터페이스를 구현한 구체클래스가 있을 때 구체클래스를 주입받지 못한다.
 * 그러니까 더 자세히 말하면, 인터페이스가 있고 그 인터페이스를 구현한 구체클래스가 있고 그리고 이 인터페이스가 프록시로 만들어져 스프링 컨테이너에 올라가면
 * 구체 클래스는 주입받지 못한다. 그 이유는 뭐냐면, 프록시는 그 인터페이스를 구현한 객체가 된다고 했다. 그럼 프록시 객체를 가져올 때 구체 클래스 정보를 프록시가 알까?
 * 아예 알지 못한다. 프록시의 부모도 인터페이스고 인터페이스의 부모는 없으니까. 즉, 구체 클래스에 대한 정보가 아예 없다. 그러니까 스프링 컨테이너에서 구체 클래스를 주입받을 수 없는거다.
 *
 * 물론, 위 상황은 프록시로 만들어졌다는 상황이 전제가 되고 나서.
 * */
@Slf4j
@SpringBootTest
@Import(ProxyDIAspect.class)
public class ProxyDITest {

    @Autowired
    MemberService memberService;

    /**
     * 주입 불가 -> 에러 발생
     * */
    @Autowired
    MemberServiceImpl memberServiceImpl;

    @Test
    void go() {
        log.info("memberService class = {}", memberService.getClass());

        log.info("memberServiceImpl class = {}", memberServiceImpl.getClass());

        memberServiceImpl.hello("helloA");
    }
}
