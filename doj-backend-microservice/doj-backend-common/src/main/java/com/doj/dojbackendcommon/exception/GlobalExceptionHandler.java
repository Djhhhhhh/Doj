package com.doj.dojbackendcommon.exception;

import com.doj.dojbackendcommon.common.BaseResponse;
import com.doj.dojbackendcommon.common.ErrorCode;
import com.doj.dojbackendcommon.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e){
        log.error("BusinessException",e);
        return R.error(e.getCode(),e.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e){
        log.error("RuntimeException",e);
        return R.error(ErrorCode.SYSTEM_ERROR,"系统错误");
    }
}
