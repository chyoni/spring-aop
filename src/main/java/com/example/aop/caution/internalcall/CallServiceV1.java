package com.example.aop.caution.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 프록시 내부 호출 문제를 해결하는 방법 1. 지연 조회
 *
 * 말이 좀 거창한데 그냥 단순하게 스프링 컨테이너에서 이 프록시를 다시 꺼내는 것.
 * 방법은 두갠데 하나는 진짜 스프링 컨테이너 (ApplicationContext)를 가져와서 bean 을 꺼내는 방법이 있고
 * 위 방법은 너무 무거우니까 ObjectProvider 를 사용해서 꺼내는 방법이 있다.
 * */
@Slf4j
@Service
public class CallServiceV1 {

    /*private ApplicationContext applicationContext;

    public CallServiceV1(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }*/
    private final ObjectProvider<CallServiceV1> callServiceV1Provider;

    public CallServiceV1(ObjectProvider<CallServiceV1> callServiceV1Provider) {
        this.callServiceV1Provider = callServiceV1Provider;
    }

    public void external() {
        log.info("call external");

        // CallServiceV1 callServiceV1 = applicationContext.getBean(CallServiceV1.class);
        CallServiceV1 callServiceV1 = callServiceV1Provider.getObject();
        callServiceV1.internal();
    }

    public void internal() {
        log.info("call internal");
    }
}
