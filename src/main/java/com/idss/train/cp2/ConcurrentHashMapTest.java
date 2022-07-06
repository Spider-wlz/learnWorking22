package com.idss.train.cp2;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 有一个含900个元素的Map，现在再补充100个元素进去，这个补充操作由10个线程并发进行。
 * 在每一个线程的代码逻辑中先通过size方法拿到当前元素数量，
 * 计算 ConcurrentHashMap目前还需要补充多少元素，并在日志
 * 中输出了这个值，然后通过putAll方法把缺少的元素添加进去。
 * @author lucifer.chan
 * @create 2022-06-16 5:45 PM
 **/
@Slf4j
@RestController
@RequestMapping("cp2/concurrent")
public class ConcurrentHashMapTest {

    //线程个数
    private static int THREAD_COUNT = 10;
    //总元素数量
    private static int ITEM_COUNT = 1000;

    /**
     * 错误示范
     * @return
     * @throws InterruptedException
     */
    @GetMapping("wrong")
    public String wrong() throws InterruptedException {
        ConcurrentHashMap<String, Long> concurrentHashMap = buildMap(ITEM_COUNT - 100);
        //初始900个元素
        log.info("init size:{}", concurrentHashMap.size());

        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        //使用线程池并发处理逻辑
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10).parallel().forEach(i -> {
            //查询还需要补充多少个元素
            int gap = ITEM_COUNT - concurrentHashMap.size();
            log.info("gap size:{}", gap);
            //补充元素
            concurrentHashMap.putAll(buildMap(gap));
        }));
        //等待所有任务完成
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        //最后元素个数会是1000吗？
        log.info("finish size:{}", concurrentHashMap.size());
        return "OK";
    }


    @GetMapping("right")
    public String right() throws InterruptedException {
        ConcurrentHashMap<String, Long> concurrentHashMap = buildMap(ITEM_COUNT - 100);
        log.info("init size:{}", concurrentHashMap.size());


        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10).parallel().forEach(i -> {
            //下面的这段复合逻辑需要锁一下这个ConcurrentHashMap
            synchronized (concurrentHashMap) {
                int gap = ITEM_COUNT - concurrentHashMap.size();
                log.info("gap size:{}", gap);
                concurrentHashMap.putAll(buildMap(gap));
            }
        }));
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);


        log.info("finish size:{}", concurrentHashMap.size());
        return "OK";
    }


    /**
     * 帮助方法，用来获得一个指定元素数量模拟数据的ConcurrentHashMap
     */
    private static ConcurrentHashMap<String, Long> buildMap(int count) {
        return LongStream.rangeClosed(1, count)
                .boxed()
                .collect(Collectors.toConcurrentMap(i -> UUID.randomUUID().toString(), Function.identity(),
                        (o1, o2) -> o1, ConcurrentHashMap::new));
    }

    public static void main(String[] args) {
        Map<String, Long> map = buildMap(10);
        map.forEach((k,v) -> System.out.println(k + ":" + v));

        IntStream.rangeClosed(1, 100).forEach(System.out::println);
    }

}
