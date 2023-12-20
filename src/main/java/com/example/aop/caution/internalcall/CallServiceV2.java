package com.example.aop.caution.internalcall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

/**
 * 프록시 내부 호출 문제를 해결하는 방법 2. 구조 분리(변경)
 *
 * 가장 우아한 방법. 즉, 내부 호출을 하지 않으면 된다. 내부 호출 코드를 다른 클래스로 빼버리자.
 * */
@Slf4j
@Service
@RequiredArgsConstructor
public class CallServiceV2 {

    private final InternalService internalService;

    public void external() {
        log.info("call external");

        internalService.internal();
    }
}
