package com.example.aop.exam.aop;

import com.example.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class RetryAspect {

    // 아래는 포인트컷 지시자 @annotation 을 사용해서 @Retry를 사용하는 메서드를 가진 클래스를 프록시로 만들어 이 부가 기능을 더해준다.
    // doRetry(ProceedingJoinPoint joinPoint, Retry retry) 여기서 파라미터 Retry가 @annotation 의 retry 로 들어간다.
    @Around("@annotation(retry)")
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {

        log.info("[retry] {} retry = {}", joinPoint.getSignature(), retry);

        int maxRetry = retry.value();
        Exception exceptionHolder = null;

        for (int i = 1; i <= maxRetry; i++) {
            try {
                log.info("[retry] try count = {}/{}", i, maxRetry);
                return joinPoint.proceed();
            } catch (Exception e) {
                exceptionHolder = e;
            }
        }

        assert exceptionHolder != null;
        throw exceptionHolder;
    }
}
