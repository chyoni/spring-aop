package com.example.aop.order;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    public void save(String itemId) {

        if (itemId.equals("ex")) {
            throw new IllegalStateException("예외 발생!");
        }

        sleep(1000);
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
