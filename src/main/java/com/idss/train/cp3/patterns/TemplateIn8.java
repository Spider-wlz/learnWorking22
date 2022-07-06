package com.idss.train.cp3.patterns;

import java.util.function.Consumer;
import java.util.function.Function;
import org.springframework.util.Assert;

/**
 * @author lucifer.chan
 * @create 2022-06-27 5:01 PM
 **/
public class TemplateIn8 {



    static class Util {
        static <T, R> R execute(T data, Function<T, R> handler, Consumer<Exception> onFailed, Consumer<T> onComplete){
            try {
                return handler.apply(data);
            } catch (Exception e) {
                onFailed.accept(e);
                throw e;
            } finally {
                onComplete.accept(data);
            }
        }
    }

    public static void main(String[] args) {

        Function<Integer, Integer> handler = i -> {
            Assert.isTrue(i < 10, "结果不能大于10");
            //....
            return i +1;
        };

        Util.execute(100
                , handler
                , e -> System.out.println(e.getMessage())
                , (i) -> System.out.println("执行完毕")
        );
    }
}
