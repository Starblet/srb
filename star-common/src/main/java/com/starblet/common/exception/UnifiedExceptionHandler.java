package com.starblet.common.exception;

import com.starblet.common.result.R;
import com.starblet.common.result.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;


/**
 * 异常处理类，用于捕获项目中的异常
 *
 * @author starblet
 * @create 2021-08-19 20:10
 */
@Slf4j
@Component
/* 如果使用ControllerAdvice，因为要返回JSON数据，所以在方法上要添加@ResponseBody */
@RestControllerAdvice // 在controller层添加切面。如果使用ControllerAdvice，则需要添加@ResponseBody
public class UnifiedExceptionHandler {

    @ExceptionHandler(value = Exception.class) // 当Controller层抛出Exception则捕获
    public R handleException(Exception e) {
        log.error(e.getMessage(),e);
        return R.error();
    }

    /**
     * 处理特定的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = BadSqlGrammarException.class)
    public R handleBadSqlGrammarException(BadSqlGrammarException e) {
        log.error(e.getMessage(),e);
        return R.setResult(ResponseEnum.BAD_SQL_GRAMMAR_ERROR);
    }

    /**
     * 处理自定义异常类
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public R handleBusinessException(BusinessException e) {
        log.error(e.getMessage(),e); // 此处若没有e，则不会打印原始的报错信息
        return R.error().message(e.getMessage()).code(e.getCode());
    }

    /**
     * 处理前端与后端之间发生的异常，及controller的上一级异常
     * @param e
     * @return
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MethodArgumentNotValidException.class,
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    public R handleServletException(Exception e) {
        log.error(e.getMessage(), e);
        //SERVLET_ERROR(-102, "servlet请求异常"),
        return R.error().message(ResponseEnum.SERVLET_ERROR.getMessage()).code(ResponseEnum.SERVLET_ERROR.getCode());
    }

}
