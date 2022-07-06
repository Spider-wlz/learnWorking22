package com.idss.train.cp2;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lucifer.chan
 * @create 2022-06-17 11:51 AM
 **/
@Slf4j
public class Interesting {

    volatile int a = 1;
    volatile int b = 1;

//    synchronized
    void add() {
        log.info("add start");
        for (int i = 0; i < 100000; i++) {
            a++;
            b++;
        }
        log.info("add done");
    }

//    synchronized
    void compare() {
        log.info("compare start");
        for (int i = 0; i < 100000; i++) {
            //a始终等于b吗？
            if(a < b) {
                //注意看日志，在判断 a<b 成立的情况下还输出了 a>b 也成立
                log.info("a:{},b:{},{}", a, b, (a > b));
            }
        }
        log.info("compare done");
    }


    public static void main(String[] args) {
        Interesting interesting = new Interesting();
        new Thread(interesting::add).start();
        new Thread(interesting::compare).start();
    }
}
