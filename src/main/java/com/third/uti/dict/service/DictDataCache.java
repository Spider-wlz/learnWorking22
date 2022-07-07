package com.third.uti.dict.service;

import java.util.Map;

/**
 * @author lucifer.chan
 * @create 2022-07-04 2:53 PM
 **/
public interface DictDataCache {

    /**
     * 刷新缓存
     * @param dictDataMap
     */
    void refresh(Map<String, String> dictDataMap);

    /**
     * 获取数据
     * @param key
     * @param defaultValue
     * @return
     */
    String getOrDefault(String key, String defaultValue);
}
