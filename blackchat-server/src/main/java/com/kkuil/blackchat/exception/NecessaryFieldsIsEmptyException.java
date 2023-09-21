package com.kkuil.blackchat.exception;

/**
 * @Author Kkuil
 * @Date 2023/9/4 0:18
 * @Description 必填字段为空异常
 */
public class NecessaryFieldsIsEmptyException extends RuntimeException{

    public NecessaryFieldsIsEmptyException() {
    }

    public NecessaryFieldsIsEmptyException(String message) {
        super(message);
    }
}
