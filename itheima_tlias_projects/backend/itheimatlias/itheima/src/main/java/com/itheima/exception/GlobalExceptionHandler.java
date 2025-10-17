package com.itheima.exception;


import com.itheima.pojo.Result;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        return Result.error("系统出错啦！请稍后再试");
    }


    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleException(DuplicateKeyException e){
        String message = e.getMessage();
        int duplicateEntry = message.indexOf("Duplicate entry");
        String substring = message.substring(duplicateEntry);
        String[] split = substring.split(" ");

        return Result.error(split[2]+"已存在");
    }
}
