package com.idss.train.cp2.util;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;
import org.jooq.lambda.Unchecked;

import static java.util.stream.Collectors.toList;

/**
 * @author lucifer.chan
 * @create 2022-06-20 9:50 AM
 **/
@Slf4j
public class BatchUtil {

    private final ForkJoinPool FORK_JOIN_POOL;

    public BatchUtil(int nThreads) {
        FORK_JOIN_POOL = new ForkJoinPool(nThreads);

    }

    /**
     * 处理数据，错误的记录异常
     *
     * @param collection
     * @param handler
     * @param <T>
     * @param <R>
     * @return
     */
    public <T, R> List<BatchResult<R>> execute(Iterable<T> collection, Function<T, R> handler) {

        long start = System.currentTimeMillis();

        List<Future<BatchResult<R>>> futures = StreamSupport.stream(collection.spliterator(), false)
                .map(t -> singleTask(t, handler))
                .collect(toList());

        List<BatchResult<R>> result = futures.stream()
                .map(Unchecked.function(Future::get))
                .collect(toList());

        log.info("批量处理任务完成,耗时:[{}]ms", (System.currentTimeMillis() - start));

        try {
            FORK_JOIN_POOL.shutdown();
            FORK_JOIN_POOL.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException ignored) {

        }

        return result;
    }


    /**
     * 单条数据的处理
     * @param data
     * @param handler
     * @param <T>
     * @param <R>
     * @return
     */
    private <T, R> Future<BatchResult<R>> singleTask(T data, Function<T, R> handler) {
        FutureTask<BatchResult<R>> task = new FutureTask<>(() -> {
            String threadName = Thread.currentThread().getName();
            long start = System.currentTimeMillis();
            try {
                R r = handler.apply(data);
                log.info("线程[{}]执行成功,耗时{}ms", threadName, (System.currentTimeMillis() - start));
                return BatchResult.success(r);
            } catch (Exception e) {
                log.error("线程[{}]执行失败,失败原因:{}, 耗时{}ms", threadName, e.getMessage(), (System.currentTimeMillis() - start));
                return BatchResult.failed(e.getMessage());
            }
        });

        FORK_JOIN_POOL.submit(task);
        return task;
    }

}


