package com.idss.train.cp3.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lucifer.chan
 * @create 2022-06-23 11:10 AM
 **/
@Getter @Setter
public class Person {

    //person -> child -> cat -> toy -> name;

    private String name;

    private String gender;

    private Integer age;

    private Child child;


    @Getter @Setter
    public static class Child {
        private Cat cat;
    }

    @Getter @Setter
    public static class Cat {
        private String category;
        private Toy toy;
    }

    @Getter @Setter
    public static class Toy {
        private String name;
    }
}
