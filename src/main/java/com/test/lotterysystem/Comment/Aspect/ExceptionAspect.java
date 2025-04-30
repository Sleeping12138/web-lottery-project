package com.test.lotterysystem.Comment.Aspect;

import com.test.lotterysystem.Comment.ErrorCode.GlobalErrorCode;
import com.test.lotterysystem.Comment.Exception.ControllerException;
import com.test.lotterysystem.Comment.Exception.ServiceException;
import com.test.lotterysystem.Comment.Pojo.CommentResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.rmi.ServerException;

@RestControllerAdvice
@Slf4j
public class ExceptionAspect {
    @ExceptionHandler(value = ControllerException.class)
    public CommentResult<?> ControllerExceptionHandler(ControllerException exception) {
        log.error("controller发生错误：", exception.getErrMsg());
        return CommentResult.fail(GlobalErrorCode.INTERNAL_SERVER_ERROR.getCode(), exception.getErrMsg());
    }

    @ExceptionHandler(value = ServiceException.class)
    public CommentResult<?> ServiceExceptionHandler(ServiceException exception) {
        log.error("service发生错误："+exception.getErrMsg());
        return CommentResult.fail(GlobalErrorCode.INTERNAL_SERVER_ERROR.getCode(), exception.getErrMsg());
    }

    @ExceptionHandler(value = Exception.class)
    public CommentResult<?> ExceptionHandler(Exception exception) {
        log.error("发生未知错误：", exception.getMessage());
        System.out.println(exception);
        return CommentResult.fail(GlobalErrorCode.INTERNAL_SERVER_ERROR.getCode(), exception.getMessage());
    }
}
