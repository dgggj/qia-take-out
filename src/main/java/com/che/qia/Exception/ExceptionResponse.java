package com.che.qia.Exception;

import lombok.Data;

/**
 * @author xiaoluyouqu
 * #Description ExceptionResponse
 * #Date: 2022/8/20 12:38
 */
@Data
public class ExceptionResponse {
    private Integer code;
    private String msg;
    private String trace;
}
