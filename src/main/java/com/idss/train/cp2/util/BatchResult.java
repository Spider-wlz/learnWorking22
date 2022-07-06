package com.idss.train.cp2.util;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lucifer.chan
 * @create 2022-06-20 10:48 AM
 **/
@Getter @Setter
public class BatchResult<T> {

    private boolean success;

    private T result;

    private String errorMsg;

    /**
     * 成功
     * @param t
     * @param <T>
     * @return
     */
    public static <T> BatchResult<T> success(T t){
        BatchResult<T> batchResult = new BatchResult<>();
        batchResult.setResult(t);
        batchResult.setSuccess(true);
        return batchResult;
    }

    /**
     * 失败
     * @param errorMsg
     * @param <T>
     * @return
     */
    public static <T> BatchResult<T> failed(String errorMsg){
        BatchResult<T> batchResult = new BatchResult<>();
        batchResult.setErrorMsg(errorMsg);
        batchResult.setSuccess(false);
        return batchResult;
    }

}
