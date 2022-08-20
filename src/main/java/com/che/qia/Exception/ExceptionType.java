package com.che.qia.Exception;

import lombok.Data;

/**
 * @author xiaoluyouqu
 * #Description ExceptionType
 * #Date: 2022/8/20 12:38
 */
public enum ExceptionType {




    ;

    private final Integer code;
    private final String msg;

    ExceptionType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
