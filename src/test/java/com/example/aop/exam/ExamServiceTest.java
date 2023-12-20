package com.example.aop.exam;

import com.example.aop.exam.annotation.LogTrace;
import com.example.aop.exam.aop.LogTraceAspect;
import com.example.aop.exam.aop.RetryAspect;
import com.example.aop.exam.aop.TraceAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


@Slf4j
@Import({TraceAspect.class, RetryAspect.class})
@SpringBootTest
class ExamServiceTest {

    @Autowired ExamService examService;

    @Test
    void test() {
        for (int i = 0; i < 5; i++) {
            log.info("client request i = {}", i);
            examService.request("data" + i);
        }
    }

    @Test
    void classAnnotationTest() {
        examService.request("testItem");
    }

}