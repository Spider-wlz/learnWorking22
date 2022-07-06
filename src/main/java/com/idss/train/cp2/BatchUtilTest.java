package com.idss.train.cp2;

import com.alibaba.fastjson.JSONObject;
import com.idss.train.cp2.util.BatchResult;
import com.idss.train.cp2.util.BatchUtil;
import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.util.stream.Collectors.toList;

/**
 * @author lucifer.chan
 * @create 2022-06-20 11:27 AM
 **/
@Slf4j
@RestController
@RequestMapping("cp2/batch")
public class BatchUtilTest {


    private static List<Integer> list = IntStream.rangeClosed(1, 1000).boxed().collect(toList());

    /**
     * 单线程处理
     * @return
     */
    @GetMapping("normal")
    public JSONObject normal(){
        long start = System.currentTimeMillis();
        int sum = list.stream()
                .mapToInt(i -> {
                    try {
                        return square(i);
                    } catch (Exception e) {
                        return 0;
                    }
                })
                .sum();

        return new JSONObject()
                .fluentPut("耗时", (System.currentTimeMillis() - start) + "ms")
                .fluentPut("结果", sum);
    }

    /**
     * 多线程处理
     * @return
     */
    @GetMapping("batch")
    public JSONObject batch(@RequestParam(value = "size", defaultValue = "100") int size) {
        BatchUtil batchUtil = new BatchUtil(size);
        long start = System.currentTimeMillis();

        List<BatchResult<Integer>> results = batchUtil.execute(list, this::square);

        int sum = results.stream()
                .filter(BatchResult::isSuccess)
                .mapToInt(BatchResult::getResult)
                .sum();

        return new JSONObject()
                .fluentPut("耗时", (System.currentTimeMillis() - start) + "ms")
                .fluentPut("结果", sum);

    }


    private int square(int i) {
        sleep(10);
        int result = i * i;
        if(result > 10000) {
            throw new IllegalArgumentException("计算结果为"+ result +",太大");
        }
        return result;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored){

        }
    }

}
