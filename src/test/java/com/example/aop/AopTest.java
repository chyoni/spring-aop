package com.example.aop;

import com.example.aop.order.OrderRepository;
import com.example.aop.order.OrderService;
import com.example.aop.order.aop.AspectV1;
import com.example.aop.order.aop.AspectV2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.*;

// @Import(AspectV1.class) //@Import 만으로도 빈으로 등록하는것과 동일하다. 주로 @Configuration 에서 추가할 때 자주 사용됐지만, @Import 로도 그 안에 클래스들을 빈으로 등록한다.
@Import(AspectV2.class)
@Slf4j
@SpringBootTest
public class AopTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void aopInfo() {
        log.info("isAopProxy, orderService = {}", AopUtils.isAopProxy(orderService));
        log.info("isAopProxy, orderRepository = {}", AopUtils.isAopProxy(orderRepository));
    }

    @Test
    void success() {
        orderService.orderItem("itemA");
    }

    @Test
    void exception() {
        assertThatThrownBy(() -> orderService.orderItem("ex"))
                .isInstanceOf(IllegalStateException.class);
    }
}
