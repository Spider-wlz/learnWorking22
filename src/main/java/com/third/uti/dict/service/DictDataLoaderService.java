package com.third.uti.dict.service;

import java.time.Duration;
import java.util.Map;

/**
 * @author lucifer.chan
 * @create 2022-07-04 2:54 PM
 **/
public interface DictDataLoaderService {

    /**
     * 从数据库读取数据
     * @return
     */
    Map<String, String> read();

    /**
     * 周期, 默认10分种刷一次
     * @return
     */
    default Duration period(){
        return Duration.ofMinutes(10L);
    }
}
