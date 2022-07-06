package com.idss.train.cp2;

import java.util.stream.IntStream;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lucifer.chan
 * @create 2022-06-17 12:08 PM
 **/
@Slf4j
@RestController
@RequestMapping("cp2/sync")
public class SynchronizedTest {


    /**
     * @param count
     * @return
     */
    @GetMapping("wrong")
    public int wrong(@RequestParam(value = "count", defaultValue = "1000000") int count) {
        Data.reset();
        //多线程循环一定次数调用Data类不同实例的wrong方法
        IntStream.rangeClosed(1, count).parallel().forEach(i -> new Data().wrong());
        return Data.getCounter();
    }

    /**
     *
     * @param count
     * @return
     */
    @GetMapping("right")
    public int right(@RequestParam(value = "count", defaultValue = "1000000") int count) {
        Data.reset();
        //多线程循环一定次数调用Data类不同实例的wrong方法
        IntStream.rangeClosed(1, count).parallel().forEach(i -> new Data().right());
        return Data.getCounter();
    }


    private static class Data {
        @Getter
        private static int counter = 0;

        private static byte[] locker = new byte[0];

        public static int reset() {
            counter = 0;
            return counter;
        }

        /**
         * 在非静态的 wrong 方法上加锁，只能确保多个线程无法执行同一个实例的 wrong 方法，却不能保证不会执行不同实例的 wrong 方法。
         * 而静态的 counter 在多个实例中共享，所以必然会出现线程安全问题。
         */
        public synchronized void wrong() {
            counter++;
        }

        /**
         * 在类中定义一个静态字段，在操作 counter 之前对这个字段加锁。
         */
        public void right() {
            synchronized (locker) {
                counter++;
            }
        }
    }
}
