package com.keendo.architecture.controller;


import com.keendo.architecture.exception.BizException;
import com.keendo.architecture.utils.Log;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 该类实现所有controller中所有restApi的通用逻辑，例如异常处理
 */
@ControllerAdvice
public class RestControllerAspect {


    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public RespBase apiWrap(BizException e){

        Log.e(e);
        String errorMsg = e.getBizExceptionMsg();
        RespBase resp = RespHelper.failed(errorMsg);

        return resp;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public RespBase apiWrap( Exception e){

        Log.e(e);
        RespBase resp = RespHelper.failed("系统异常");

        return resp;
    }

}
