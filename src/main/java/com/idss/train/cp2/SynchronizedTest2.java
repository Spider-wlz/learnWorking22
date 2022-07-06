package com.idss.train.cp2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lucifer.chan
 * @create 2022-06-17 1:01 PM
 **/
@Slf4j
@RestController
@RequestMapping("cp2/sync2")
public class SynchronizedTest2 {

    private List<Integer> data = new ArrayList<>();

    //不涉及共享资源的慢方法
    private void slow() {
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
        }
    }


    /**
     * 错误的加锁方法
     * @return
     */
    @GetMapping("wrong")
    public String wrong() {
        long begin = System.currentTimeMillis();
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
            //加锁粒度太粗了
            synchronized (this) {
                slow();
                data.add(i);
            }
        });
        log.info("cost:{}", System.currentTimeMillis() - begin);
        return "耗时" + ( System.currentTimeMillis() - begin) + "ms";
    }

    /**
     * 正确的加锁方法
     * @return
     */
    @GetMapping("right")
    public String right() {
        long begin = System.currentTimeMillis();
        IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
            slow();
            //只对List加锁
            synchronized (data) {
                data.add(i);
            }
        });
        log.info("cost:{}", System.currentTimeMillis() - begin);
        return "耗时" + ( System.currentTimeMillis() - begin) + "ms";
    }
}
