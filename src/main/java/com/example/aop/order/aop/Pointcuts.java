package com.example.aop.order.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* com.example.aop.order..*(..))")
    public void allOrder() {} // Pointcut Signature

    @Pointcut("execution(* *..*Service.*(..))")
    public void allService() {}

    /**
     * Pointcut에 '&&' 로 되어 있으면 둘 다 만족해야한다.
     * 그러니까 이 예제에서 이 포인트컷은 OrderRepository에는 대상이 아닌 것.
     * */
    @Pointcut("allOrder() && allService()")
    public void orderAndService() {}
}
