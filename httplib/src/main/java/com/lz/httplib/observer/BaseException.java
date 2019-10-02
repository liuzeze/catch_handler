package com.lz.httplib.observer;


/**
 * -------- 日期 ---------- 维护人 ------------ 变更内容 --------
 * 2017/12/26	9:24	     刘泽			   全局异常统一处理
 */

public class BaseException extends Exception {

    private int code;
    private String message;

    public BaseException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
        this.message = throwable.getMessage();
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public BaseException setMessage(String message) {
        this.message = message;
        return this;
    }
}
