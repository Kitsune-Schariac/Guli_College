package com.kitsune.servicebase.exceptionhandler;


import com.kitsune.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)      //出现设么异常执行这个方法
    @ResponseBody   //为了能够返回数据
    public R error(Exception e) {

        e.printStackTrace();
        return R.error().message("执行了全局异常处理");
    }

    //特定异常
    @ExceptionHandler(ArithmeticException.class)      //出现设么异常执行这个方法
    @ResponseBody   //为了能够返回数据
    public R error(ArithmeticException e) {

        e.printStackTrace();
        return R.error().message("执行了ArithmeticException的异常处理..");
    }

    //自定义异常
    @ExceptionHandler(GuliException.class)      //出现设么异常执行这个方法
    @ResponseBody   //为了能够返回数据
    public R error(GuliException e) {

        //把异常输出到日志
        log.error(e.getMsg());

        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }

}
