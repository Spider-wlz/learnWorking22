package com.idss.train.cp2;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lucifer.chan
 * @create 2022-06-16 4:21 PM
 **/
@RestController
@RequestMapping("cp2/threadlocal")
public class ThreadLocalTest {

    private static final ThreadLocal<Integer> currentUserCache = ThreadLocal.withInitial(() -> null);

    /**
     * 错误示范
     * @param userId
     * @return
     */
    @GetMapping("wrong")
    public Map<String, String> wrong(Integer userId){
        //1 - 查
        String before = currentUser();
        //2 - 设置
        currentUserCache.set(userId);
        //3 - 再查
        String after = currentUser();

        return new HashMap<String, String>(){
            {
                put("before", before);
                put("after", after);
            }
        };
    }

    /**
     * 正确示范，手动清理
     * @param userId
     * @return
     */
    @GetMapping("right")
    public Map<String, String> right(Integer userId){

        //1 - 查
        String before = currentUser();
        //2 - 设置
        currentUserCache.set(userId);
        //3 - 再查
        String after = currentUser();
        try {
            return new HashMap<String, String>(){
                {
                    put("before", before);
                    put("after", after);
                }
            };
        } finally {
            currentUserCache.remove();
        }


    }

    private String currentUser(){
        return Thread.currentThread().getName() + ":" + currentUserCache.get();
    }
}
