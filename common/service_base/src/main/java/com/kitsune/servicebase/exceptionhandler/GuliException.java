package com.kitsune.servicebase.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor    //生成有采纳数构造方法
@NoArgsConstructor     //生成无参数构造方法
@Data
public class GuliException extends RuntimeException{

    private Integer code;       //状态码
    private String msg;         //异常信息

}
