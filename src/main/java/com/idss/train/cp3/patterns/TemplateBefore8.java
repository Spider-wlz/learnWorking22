package com.idss.train.cp3.patterns;

import org.springframework.util.Assert;

/**
 * 模版方法模式
 *
 * @author lucifer.chan
 * @create 2022-06-24 6:14 PM
 **/
public class TemplateBefore8 {


    abstract class Util<T, R> {
        /**
         * 处理逻辑
         * @param data
         * @return
         */
        R execute(T data){
            try {
                return handle(data);
            } catch (Exception e) {
                onFailed(e);
                throw e;
            } finally {
                onComplete(data);
            }
        }

        /**
         * 处理一个事情
         * @param data
         * @return
         */
        abstract R handle(T data);

        /**
         * 处理异常
         * @param e
         */
        abstract void onFailed(Exception e);

        /**
         * 完成操作
         * @param data
         * @return
         */
        abstract void onComplete(T data);
    }

    /**
     * 模版方法模式的使用
     */
    void test(){
        Util<Integer, Integer> util = new Util<Integer, Integer>() {
            @Override
            Integer handle(Integer data) {
                int result = data + 1;
                Assert.isTrue(result < 10, "结果不能大于10");
                System.out.println("result is " + result);
                return result;
            }

            @Override
            void onFailed(Exception e) {
                System.out.println(e.getMessage());
            }

            @Override
            void onComplete(Integer data) {
                System.out.println(data + "完成操作");
            }
        };

       util.execute(100);
    }

    public static void main(String[] args) {
        TemplateBefore8 before8 = new TemplateBefore8();
        before8.test();
    }
}
