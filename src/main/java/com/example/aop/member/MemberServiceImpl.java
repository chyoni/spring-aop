package com.example.aop.member;

import com.example.aop.member.annotation.ClassAop;
import com.example.aop.member.annotation.MethodAop;
import org.springframework.stereotype.Component;

@ClassAop
@Component
public class MemberServiceImpl implements MemberService {

    @Override
    @MethodAop(value = "test value")
    public String hello(String param) {
        return "ok";
    }

    public String internal(String param) {
        return "ok";
    }

    public String twoParams(String param1, String param2) {
        return "ok";
    }
}
