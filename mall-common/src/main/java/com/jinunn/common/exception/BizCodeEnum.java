package com.jinunn.common.exception;

/**
 * 统一异常提示
 * @author jinDun
 */

public enum BizCodeEnum {
    //系统位置异常
    UNKNOWN_EXCEPTION(10000,"系统未知异常"),
    //参数校验异常
    VALID_EXCEPTION(10001,"参数格式校验异常");

    private final int code;
    private final String msg;

    BizCodeEnum(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
