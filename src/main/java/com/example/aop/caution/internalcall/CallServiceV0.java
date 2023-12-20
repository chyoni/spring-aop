package com.example.aop.caution.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV0 {

    public void external() {
        log.info("call external");

        // 이렇게 내부 메서드를 호출하면 이 호출은 곧 this.internal()과 같은 것인데 프록시를 통해서 이 external()을 호출하면 프록시가 결국 proceed()를 호출하면서
        // 프록시가 가지고 있는 실제 객체의 external()을 호출. 그리고 실제 객체가 이 internal()을 호출하기 때문에 internal()에는 프록시가 걸리지 않는다.
        // 이 경우를 '프록시 내부 호출 문제'라고 한다.
        internal();
    }

    public void internal() {
        log.info("call internal");
    }
}
