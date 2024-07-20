package com.lenyan.lendada;

import io.reactivex.Flowable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RxJavaTest {

    @Test
    public void test() throws InterruptedException {
        // 创建数据流
        Flowable<Long> flowable = Flowable.interval(1, TimeUnit.SECONDS)
                .map(i -> i + 1);


        // 订阅 Flowable 流，并且打印出每个接受到的数字
        flowable
                .doOnNext(item -> System.out.println(item.toString()))
                .subscribe();

        // 主线程睡眠，以便观察到结果
        Thread.sleep(50000L);
    }
}
