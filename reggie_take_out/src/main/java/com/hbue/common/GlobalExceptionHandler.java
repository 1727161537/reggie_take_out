package com.hbue.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理类
 */
//指定注解选择,当有这两个注解的类发生异常时交由此类处理
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 异常处理方法
     * 对指定类型的异常进行处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        //打印错误
        log.error(exception.getMessage());
        //判断异常中是否包含主键错误的内容
        if (exception.getMessage().contains("Duplicate entry")) {
            //获取重复的用户名
            String[] strings = exception.getMessage().split(" ");
            String msg = strings[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }


}
