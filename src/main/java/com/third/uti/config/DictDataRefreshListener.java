package com.third.uti.config;

import com.third.uti.dict.service.DictDataCache;
import com.third.uti.dict.service.DictDataLoaderService;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * @author lucifer.chan
 * @create 2022-07-04 1:26 PM
 **/
@Service
@Slf4j
public class DictDataRefreshListener implements ApplicationListener<ApplicationStartedEvent> {

    private static final ScheduledExecutorService DICT_DATA_EXECUTOR = new ScheduledThreadPoolExecutor(1
            , new BasicThreadFactory.Builder().namingPattern("dict-data-loader-pool-%d").daemon(true).build());

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        ApplicationContext context = event.getApplicationContext();

        //1 - 数据字典缓存服务
        DictDataCache dictDataCache = context.getBean(DictDataCache.class);

        //2 - 数据字典加载服务
        DictDataLoaderService dataLoader = context.getBean(DictDataLoaderService.class);

        //3 - 定时刷新数据字典缓存
        DICT_DATA_EXECUTOR.scheduleWithFixedDelay(() -> {
            try {

                Map<String, String> dictDataMap = dataLoader.read();
                dictDataCache.refresh(dictDataMap);
                int totalNum = Optional.ofNullable(dictDataMap).map(Map::size).orElse(0);
                log.debug("完成数据字典缓存刷新,共刷新数据{}条", totalNum);
            } catch (Exception e) {
                log.error("执行加载数据字典时出错: {}", e.getMessage());
            }
        }, 0, dataLoader.period().getSeconds(), TimeUnit.SECONDS);

    }
}
