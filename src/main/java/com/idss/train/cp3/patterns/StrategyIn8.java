package com.idss.train.cp3.patterns;

/**
 * @author lucifer.chan
 * @create 2022-06-27 4:58 PM
 **/
public class StrategyIn8 {

    interface Strategy {
        int operate(int num1, int num2);
    }

    class Context {
        private Strategy strategy;

        Context(Strategy strategy) {
            this.strategy = strategy;
        }

        int execute(int num1, int num2) {
            return strategy.operate(num1, num2);
        }
    }

    /**
     * 策略模式的使用 java8
     */
    void test() {
        Context context1 = new Context((num1, num2) -> num1 + num2);
        System.out.println(context1.execute(5, 10));

        Context context2 = new Context((num1, num2) -> num1 - num2);
        System.out.println(context2.execute(5, 10));

        Context context3 = new Context((num1, num2) -> num1 * num2);
        System.out.println(context3.execute(5, 10));

        //加策略很方便
        Context context4 = new Context((num1, num2) -> num1 * num2 - 1);
        System.out.println(context4.execute(5, 10));

    }

    public static void main(String[] args) {
        StrategyIn8 demo = new StrategyIn8();
        demo.test();
    }
}
