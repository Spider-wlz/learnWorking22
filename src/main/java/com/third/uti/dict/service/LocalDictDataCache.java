package com.third.uti.dict.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地缓存数据字典
 *
 * @author lucifer.chan
 * @create 2022-07-04 2:56 PM
 **/
public class LocalDictDataCache implements DictDataCache {

    private final Map<String, String> cache = new ConcurrentHashMap<>();

    @Override
    public void refresh(Map<String, String> dictDataMap) {
        dictDataMap.forEach((key, value)-> {
            synchronized (cache) {
                cache.putAll(dictDataMap);
            }
        });
    }

    @Override
    public String getOrDefault(String key, String defaultValue) {
        return cache.getOrDefault(key, defaultValue);
    }
}
