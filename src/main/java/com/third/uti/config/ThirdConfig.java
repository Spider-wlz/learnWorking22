package com.third.uti.config;

import com.third.uti.dict.DataWrapper;
import com.third.uti.dict.service.DictDataCache;
import com.third.uti.dict.service.DictDataLoaderService;
import com.third.uti.dict.service.LocalDictDataCache;
import com.third.uti.dict.service.RedisDictDataCache;
import java.util.Collections;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author lucifer.chan
 * @create 2022-07-04 1:29 PM
 **/
@Configuration
@PropertySource("classpath:/META-INF/third.properties")
public class ThirdConfig {

    /**
     * redis缓存数据字典
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "com.third.uti.dict.type", havingValue = "redis")
    public DictDataCache redisDictDataCache(RedisTemplate redisTemplate){
        return new RedisDictDataCache(redisTemplate);
    }

    /**
     * local缓存数据字典
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = "com.third.uti.dict.type", havingValue = "local")
    public DictDataCache localDictDataCache(){
        return new LocalDictDataCache();
    }

    /**
     * 默认的 数据字典加载服务
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(DictDataLoaderService.class)
    public DictDataLoaderService dictDataLoaderService(){
        return Collections::emptyMap;
    }

    @Bean
    public DataWrapper dataWrapper(DictDataCache dataCache){
        return new DataWrapper(dataCache);
    }
}
