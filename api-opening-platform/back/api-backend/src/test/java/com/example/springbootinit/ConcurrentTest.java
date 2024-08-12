package com.example.springbootinit;

import com.example.springbootinit.service.UserInterfaceInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ConcurrentTest {

    // 具体的interfaceInfoId和userId替换为测试用的值
    static final long interfaceInfoId = 1;
    static final long userId = 1;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Test
    void invokeCount() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 600000; i++) {
            executorService.submit(() -> {
                userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
