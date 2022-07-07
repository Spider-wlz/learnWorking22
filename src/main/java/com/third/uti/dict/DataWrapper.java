package com.third.uti.dict;

import com.third.uti.dict.annotation.Dict;
import com.third.uti.dict.service.DictDataCache;
import com.third.uti.tool.Reflect;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * @author lucifer.chan
 * @create 2022-07-04 3:03 PM
 **/
@Slf4j
public class DataWrapper {

    private static DictDataCache DATA_CACHE;

    public DataWrapper(DictDataCache dataCache){
        DATA_CACHE = dataCache;
    }

    /**
     * list
     * @param list
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> List<T> wrapperList(List<T> list) {
        if(null == list) {
            return null;
        }

        return list.stream().map(DataWrapper::wrapperObject).collect(toList());
    }

    /**
     *
     * @param t
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> T wrapperObject(T t) {
        if(null == t) {
            return null;
        }

        if(null == DATA_CACHE){
            return t;
        }

        Field[] allFields = t.getClass().getDeclaredFields();

        Set<String> fieldNames = Stream.of(allFields).map(Field::getName).collect(toSet());

        Reflect objReflect = Reflect.on(t);

        Stream.of(allFields)
                //1 - 过滤出有@Dict的属性
                .filter(it -> it.isAnnotationPresent(Dict.class))
                //2 - 过滤出dicText 和 dicDataSource 都有值的属性
                .filter(it -> {
                    Dict dict = it.getDeclaredAnnotation(Dict.class);
                    return StringUtils.hasText(dict.dictKey()) && StringUtils.hasText(dict.dicField());
                })
                //3 - 每个field赋值
                .forEach(withDict -> {
                    Dict dict = withDict.getDeclaredAnnotation(Dict.class);
                    String targetFieldName = dict.dicField();
                    String selfFieldName = withDict.getName();
                    //dictText所指的属性合法 即: 非自身 && 对象中包含
                    if(!targetFieldName.equals(selfFieldName) && fieldNames.contains(targetFieldName)){
                        //1 - 先获取属性值
                        Object fieldValue = objReflect.get(withDict.getName());
                        String fieldStrValue = String.valueOf(fieldValue);
                        //2 - 组合成cacheKey ${dataSource}_${codeValue}
                        String cacheKey = dict.dictKey() + "_" + fieldStrValue;
                        //3 - 从缓存里拿值
                        String value = DATA_CACHE.getOrDefault(cacheKey, fieldStrValue);
                        //4 - 赋值到目标属性
                        try {
                            objReflect.set(targetFieldName, value);
                        } catch (Exception e) {
                            log.error("赋值{}.{} 为 {} 是失败,错误信息:{}", t.getClass().getName(), targetFieldName, value, e.getMessage());
                        }
                    }

                });
        return t;
    }
}
