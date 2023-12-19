package com.example.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * 스프링 AOP 에서 @Around를 조각으로 분리할 수가 있다.
 * 그러니까 어드바이스 로직 흐름에 따라 @Around를 @Before, @AfterReturning, @AfterThrowing, @After로 분리할 수 있다.
 * {@code @Before}는 실제 타겟을 호출하기 전까지의 과정만을 작성하고 @AfterReturning은 실제 타겟을 호출한 후 과정을,
 * {@code @AfterThrowing}은 예외가 발생한 후 과정을, @After는 모든 로직이 다 끝난 마지막 부분을 담당한다.
 * 솔직히 @Around만 알면 나머지는 굳이 알 필요가 없다고 생각하는데 그럼에도 한가지는 알아야 한다. @Around는 파라미터로 ProceedingJoinPoint를 받는다.
 * 그러나 나머지는 그것을 받을수가 없다. JoinPoint로 받아야 한다. 왜냐하면 ProceedingJoinPoint는 JoinPoint를 상속받는데 둘 차이는 ProceedingJoinPoint는
 * 실제 타겟을 호출할 수가 있다. 'proceed()'가 존재한다. 근데 나머지 @Before, @AfterReturning, @AfterThrowing, @After가 담당하는 부분은
 * 실제 타켓을 호출하는 부분을 담당하지 않으니 당연히 받지 않는 것이다. 이 정도 차이만? 알고 있으면 좋을 것 같다.
 * 그리고 @Around를 사용할 때 발생할 수 있는 예외 상황(proceed()를 호출하지 않는 경우)를 방지할 수 있는 장점이 있다.
 *
 * 그리고 저 말은 @Around는 반드시 proceed()를 호출해야만 정상 흐름으로 진행할 수 있다. 아니면 다음 타겟으로 진행되지가 않는다.
 * */
@Slf4j
@Aspect
public class AspectV6Advice {

    /*@Around("com.example.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // @Before
            log.info("[doTransaction] Start {}", joinPoint.getSignature());

            Object proceed = joinPoint.proceed();

            // @AfterReturning
            log.info("[doTransaction] Commit {}", joinPoint.getSignature());

            return proceed;
        } catch (Exception e) {
            // @AfterThrowing
            log.info("[doTransaction] Rollback {}", joinPoint.getSignature());

            throw e;
        } finally {
            // @After
            log.info("[doTransaction] Release {}", joinPoint.getSignature());
        }
    }*/

    /**
     * {@code @Before}는 한가지 더 알아야 할 게 이 @Before가 호출된 후 자동으로 실제 타겟을 호출한다.
     * */
    @Before("com.example.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[doBefore] {}", joinPoint.getSignature());
    }

    /**
     * {@code @AfterReturning}도 하나 알아야 할 것이 있다. 뭐냐면 result 의 타입이다.
     * 실제 타겟이 반환하는 타입이 일치하거나 그보다 상위 타입으로 받아야 정상적으로 호출할 수 있다.
     * 즉, 만약 서비스가 반환 하는 타입이 String 이면 이 @AfterReturning도 String 또는 그 상위인 Object여야 정상 호출이 된다.
     * */
    @AfterReturning(value = "com.example.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturning(JoinPoint joinPoint, Object result) {
        log.info("[doReturning] {}, return = {}", joinPoint.getSignature(), result);
    }

    /**
     * {@code @AfterThrowing}은 위 @AfterReturning과 마찬가지로 예외의 타입이 실제 타겟이 던지는 예외 타입과 일치하거나 그 상위 예외여야 한다.
     * */
    @AfterThrowing(value = "com.example.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[doThrowing] {}, message = {}", joinPoint.getSignature(), ex.getMessage());
    }

    @After(value = "com.example.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[doAfter] {}", joinPoint.getSignature());
    }
}
