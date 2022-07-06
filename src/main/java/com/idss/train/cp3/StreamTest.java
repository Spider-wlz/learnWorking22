package com.idss.train.cp3;

import com.idss.train.cp3.dto.Person;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @author lucifer.chan
 * @create 2022-06-23 11:08 AM
 **/
public class StreamTest {

    /**
     * 案例1
     * 功能描述：元素做平方
     * before java8
     * @param integerList   int列表
     * @return
     */
    public List<Integer> case1(List<Integer> integerList) {
        List<Integer> result = new ArrayList<>();
        //外部迭代
        for (int i : integerList) {
            result.add(i * i);
        }

        return result;
    }

    /**
     * 案例1
     * 功能描述：元素做平方
     * before java8
     * @param integerList   int列表
     * @return
     */
    public List<Integer> case1InJava8(List<Integer> integerList) {
        //内部迭代
        return integerList
                .stream()
//                .parallelStream().map(i -> i * i)
                .map(i -> i * i)
                .collect(toList());//注意：在collect|forEach之前，并没有真正执行"操作流水"
    }

    /**
     * 案例1 : calcJava8(integerList, i -> i*i);
     * 将具体算法作为参数
     * @param integerList
     * @param mapper
     * @return
     */
    public List<Integer> case1InJava8(List<Integer> integerList, UnaryOperator<Integer> mapper) {
        return integerList.stream()
                .map(mapper)
//                .filter(i -> i > 10)
//                .sorted(Comparator.reverseOrder())
                .collect(toList());
    }

    /**
     * 案例2
     * 按性别分组
     * @param personList
     * @return
     */
    public Map<String, List<Person>> group(List<Person> personList) {
        Map<String, List<Person>> result = new HashMap<>();


        for (Person person : personList) {
            //
        }


        //TODO
        return null;
    }

    /**
     * 案例2
     * 按性别分组
     * @param personList
     * @return
     */
    public Map<String, List<Person>> groupInJava8(List<Person> personList) {
        return  personList.stream()
                .collect(groupingBy(Person::getGender));
    }

    /**
     * key:gender, value:name
     * @param personList
     * @return
     */
    public Map<String, String> f1(List<Person> personList) {
        return personList.stream()
                .collect(toMap(Person::getGender, Person::getName, (v1, v2) -> v2));
    }

    /**
     * allMatch、anyMatch、noneMatch
     * @param personList
     */
    public void f2(List<Person> personList) {
        boolean result = personList.stream()
                .allMatch(person -> person.getAge() > 18);

    }


    /**
     * findFirst、findAny
     * @param personList
     */
    public Person f3(List<Person> personList) {
        //TODO
        return null;
    }

    public void streamBuilder(){
        IntStream.rangeClosed(0, 10).forEach(System.out::println);
        System.out.println("****************************");

        Stream.iterate(0, n -> n + 2).limit(10).forEach(System.out::println);
        System.out.println("****************************");

        Stream.generate(Math::random).limit(5).forEach(System.out::println);
    }

    public static void main(String[] args) {
        INTEGER_LIST.stream()
                .map(i -> {
                    System.out.println(i);
                    return  i +1;
                })
                .forEach(System.out::println);




    }








    private static List<Integer> INTEGER_LIST = IntStream.rangeClosed(0, 10).boxed().collect(toList());


    private static final List<Person> PERSON_LIST;

    static {
        PERSON_LIST = new ArrayList<>();

        Person person1 = new Person();
        person1.setName("Lily");
        person1.setAge(13);
        person1.setGender("F");

        Person person2 = new Person();
        person2.setName("Lucy");
        person2.setAge(14);
        person2.setGender("F");

        Person person3 = new Person();
        person3.setName("Lilei");
        person3.setAge(15);
        person3.setGender("M");

        PERSON_LIST.add(person1);
        PERSON_LIST.add(person2);
        PERSON_LIST.add(person3);
    }






}
