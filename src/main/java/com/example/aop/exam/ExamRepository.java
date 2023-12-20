package com.example.aop.exam;

import com.example.aop.exam.annotation.LogTrace;
import com.example.aop.exam.annotation.Retry;
import com.example.aop.exam.annotation.Trace;
import org.springframework.stereotype.Repository;

@Repository
@LogTrace
public class ExamRepository {

    private static int seq = 0;

    @Trace
    @Retry
    public String save(String itemId) {
        seq++;
        if (seq % 5 == 0) {
            throw new IllegalStateException("Exception!");
        }
        return "ok";
    }
}
