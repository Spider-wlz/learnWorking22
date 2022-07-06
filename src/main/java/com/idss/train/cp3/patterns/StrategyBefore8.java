package com.idss.train.cp3.patterns;

/**
 * @author lucifer.chan
 * @create 2022-06-27 4:56 PM
 **/
public class StrategyBefore8 {

    interface Strategy {
        int operate(int num1, int num2);
    }

    /**
     * 加法
     */
    class Add implements Strategy {
        @Override
        public int operate(int num1, int num2) {
            return num1 + num2;
        }
    }

    /**
     * 减法
     */
    class Subtract implements Strategy {
        @Override
        public int operate(int num1, int num2) {
            return num1 - num2;
        }
    }

    /**
     * 乘法
     */
    class Multiply implements Strategy {
        @Override
        public int operate(int num1, int num2) {
            return num1 * num2;
        }
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

    void test(){
        Context context = new Context(new Add());
        System.out.println(context.execute(5, 10));

        context = new Context(new Subtract());
        System.out.println(context.execute(5, 10));

        context = new Context(new Multiply());
        System.out.println(context.execute(5, 10));
    }

    public static void main(String[] args) {
        StrategyBefore8 demo = new StrategyBefore8();
        demo.test();
    }
}
