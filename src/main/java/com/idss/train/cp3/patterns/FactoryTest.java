package com.idss.train.cp3.patterns;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 工厂模式
 * @author lucifer.chan
 * @create 2022-06-27 5:09 PM
 **/
public class FactoryTest {

    interface Product {
        default void print(){
            System.out.println("I am" + this.getClass().getName());
        }
    }

    class Shoe implements Product {}

    class Tshirt implements Product {}

    class Shock implements Product {}

    /**
     * before java8
     * @param category
     * @return
     */
    Product create1 (String category) {
        switch (category) {
            case "shoe" : return new Shoe();
            case "tshirt" : return new Tshirt();
            case "shock" : return new Shock();
            default: throw new IllegalArgumentException(category + "错误");
        }
    }

    /**
     * java 8
     * @param category
     * @return
     */
    Product create2 (String category) {
        Supplier<Product> supplier = map.get(category);

        return Optional.ofNullable(supplier)
                .map(Supplier::get)
                .orElseThrow(() ->  new IllegalArgumentException(category + "错误"));
    }


    Map<String, Supplier<Product>> map = new HashMap<String, Supplier<Product>>() {
        {
            put("shoe", Shoe::new);
            put("tshirt", Tshirt::new);
            put("shock", Shock::new);
        }
    };

    public static void main(String[] args) {
        FactoryTest test = new FactoryTest();
        test.create2("tshirt").print();
        test.create2("shock").print();
        test.create2("xxx").print();
    }

}
