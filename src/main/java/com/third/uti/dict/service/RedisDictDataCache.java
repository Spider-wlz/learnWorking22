package com.third.uti.dict.service;

import java.util.Map;
import java.util.Objects;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis缓存数据字典（当有redis的时候启用）
 *
 * @author lucifer.chan
 * @create 2022-07-01 7:31 PM
 **/
public class RedisDictDataCache implements DictDataCache {

    private static final String HASH_KEY = "NAME";

    private final RedisTemplate redisTemplate;

    public RedisDictDataCache(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 将map刷新到redis里
     *
     * @param dictDataMap
     */
    @Override
    @SuppressWarnings("unchecked")
    public void refresh(Map<String, String> dictDataMap) {

        RedisSerializer keySerializer = redisTemplate.getKeySerializer();
        RedisSerializer hashKeySerializer = redisTemplate.getHashKeySerializer();
        RedisSerializer hashValueSerializer = redisTemplate.getHashValueSerializer();

        //采用pipelined,减少redis的写次数
        redisTemplate.executePipelined((RedisCallback) connection -> {
            dictDataMap.forEach((key, value)
                    -> connection.hSet(keySerializer.serialize(key)
                        , hashKeySerializer.serialize(HASH_KEY)
                        , hashValueSerializer.serialize(value)
                    )
            );
            return null;
        });
    }

    @Override
    public String getOrDefault(String key, String defaultValue) {
        Object value = redisTemplate.opsForHash().get(key, HASH_KEY);

        if(Objects.nonNull(value)) {
            return value.toString();
        }
        return defaultValue;
    }

}
