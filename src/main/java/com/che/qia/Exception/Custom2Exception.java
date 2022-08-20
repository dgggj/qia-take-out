package com.che.qia.Exception;

import lombok.Data;

/**
 * @author xiaoluyouqu
 * #Description CustomException
 * #Date: 2022/8/20 12:36
 */
@Data
public class Custom2Exception extends RuntimeException{
    private Integer code;
    public Custom2Exception(ExceptionType exceptionType){
        super((exceptionType.getMsg()));
        this.code=exceptionType.getCode();
    }
}
