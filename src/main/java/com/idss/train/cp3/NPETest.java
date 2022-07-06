package com.idss.train.cp3;

import com.alibaba.fastjson.JSONObject;
import com.idss.train.cp3.dto.Person;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author lucifer.chan
 * @create 2022-06-24 4:16 PM
 **/
public class NPETest {

    /**
     * 案例3: NPE
     * @param person
     * @return
     */
    public String toyName1(Person person) {
        return person.getChild().getCat().getToy().getName();
    }

    /**
     * 案例3: java8之前
     * person.child.cat.toy.name
     * @param person
     * @return
     */
    public String toyName2(Person person) {
        if(null == person) {
            return null;
        }

        Person.Child child = person.getChild();
        if(null == child) {
            return null;
        }

        Person.Cat cat = child.getCat();
        if(null == cat) {
            return null;
        }
        Person.Toy toy = cat.getToy();

        if(null == toy) {
            return null;
        }

        return  toy.getName();
    }

    /**
     * Optional
     * @param person
     * @return
     */
    @Nullable
    public String toyNameInJava8(@NonNull Person person) {

        return Optional.ofNullable(person)
                .map(Person::getChild)
                .map(Person.Child::getCat)
                .map(Person.Cat::getToy)
                .map(Person.Toy::getName)
                .orElse("");


    }



    public static void main(String[] args) {
        NPETest test = new NPETest();

        Person person = JSONObject.parseObject(person1Json, Person.class);

        System.out.println(test.toyNameInJava8(person));
//        System.out.println(test.toyNameInJava8(null));
    }







    private static String person1Json = "{\"name\" : \"张三\", \"gender\" : \"M\", \"age\": \"30\", \"child\" : {\"cat\" : {\"category\" : \"三花\", \"toy\" : {\"name\" : \"小鱼枕头\"}}}}";

    private static String person2Json = "{\"name\" : \"张三\", \"gender\" : \"M\", \"age\": \"30\", \"child\" : {\"cat\" : {\"category\" : \"三花\"}}}";
}
