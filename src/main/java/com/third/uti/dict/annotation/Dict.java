package com.third.uti.dict.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lucifer.chan
 * @create 2022-07-04 2:35 PM
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {

    /**
     * 数据字典的key
     * @return
     */
    String dictKey();

    /**
     * 对象的对应字段名
     * @return
     */
    String dicField() default "";
}