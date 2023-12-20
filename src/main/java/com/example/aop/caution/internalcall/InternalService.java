package com.example.aop.caution.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InternalService {

    public void internal() {
        log.info("call internal");
    }
}
