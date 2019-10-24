package com.example.pay.demo;

import com.example.pay.bean.JsonResult;
import com.example.pay.exception.MyException;
import com.example.pay.util.ErrorLogsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * @author: chenjie
 * @date: 2019/3/06 10:12
 * 统一支付平台接口 UPI
 * 全局异常处理类
 *
 *
 *         ┌─┐              ┌─┐
 *   ┌──┘  ┴───────┘  ┴──┐
 *   │                                  │
 *   │          ───                  │
 *   │     ─┬┘       └┬─          │
 *   │                                  │
 *   │           ─┴─                 │
 *   │                                  │
 *   └───┐                  ┌───┘
 *           │                  │
 *           │                  │
 *           │                  │
 *           │                  └──────────────┐
 *           │                                                │
 *           │                                                ├─┐
 *           │                                                ┌─┘
 *           │                                                │
 *           └─┐    ┐    ┌───────┬──┐    ┌──┘
 *               │  ─┤  ─┤              │  ─┤  ─┤
 *               └──┴──┘              └──┴──┘
 *                神兽保佑
 *               代码无BUG!
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String logExceptionFormat = "Capture Exception By GlobalExceptionHandler: Code: %s Detail: %s";

    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //运行时异常
  /*  @ExceptionHandler(RuntimeException.class)
    public String runtimeExceptionHandler(RuntimeException ex) {
        return exceptionFormat(1, ex);
    }
*/
    //空指针异常
    @ExceptionHandler(NullPointerException.class)
    public String nullPointerExceptionHandler(NullPointerException ex) {
        return exceptionFormat(2, ex);
    }

    //类型转换异常
    @ExceptionHandler(ClassCastException.class)
    public String classCastExceptionHandler(ClassCastException ex) {
        return exceptionFormat(3, ex);
    }

    //IO异常
    @ExceptionHandler(IOException.class)
    public String iOExceptionHandler(IOException ex) {
        return exceptionFormat(4, ex);
    }

    //未知方法异常
    @ExceptionHandler(NoSuchMethodException.class)
    public String noSuchMethodExceptionHandler(NoSuchMethodException ex) {
        return exceptionFormat(5, ex);
    }

    //数组越界异常
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public String indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        return exceptionFormat(6, ex);
    }

    //400错误
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String requestNotReadable(HttpMessageNotReadableException ex) {
        System.out.println("400..requestNotReadable");
        return exceptionFormat(7, ex);
    }

    //400错误
    @ExceptionHandler(TypeMismatchException.class)
    public String requestTypeMismatch(TypeMismatchException ex) {
        System.out.println("400..TypeMismatchException");
        return exceptionFormat(8, ex);
    }

    //400错误
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String requestMissingServletRequest(MissingServletRequestParameterException ex) {
        System.out.println("400..MissingServletRequest");
        return exceptionFormat(9, ex);
    }

    //405错误
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String request405(HttpRequestMethodNotSupportedException ex) {
        return exceptionFormat(10, ex);
    }

    //406错误
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public String request406(HttpMediaTypeNotAcceptableException ex) {
        System.out.println("406...");
        return exceptionFormat(11, ex);
    }
    //栈溢出
    @ExceptionHandler(StackOverflowError.class)
    public String requestStackOverflow(Exception ex) {
        return exceptionFormat(13, ex);
    }


    //其他错误
    @ExceptionHandler(value = Exception.class)
    public String exception(Exception ex) {
        return exceptionFormat(14, ex);
    }



    @ExceptionHandler(MyException.class)
    public Map<String,Object> exceptionHandler(MyException e) {
        Map<String,Object> map = new HashMap<>();
        map.put("code",e.getCode());
        map.put("msg",e.getMsg());
        ErrorLogsUtil.error(e); //调用工具类写入数据库
        return map;
    }


    private <T extends Throwable> String exceptionFormat(Integer code, T ex) {
        log.error(String.format(logExceptionFormat, code, ex.getMessage()));
        ErrorLogsUtil.error((Exception) ex); //调用工具类写入数据库
        return JsonResult.failed(code, ex.getMessage());
    }


}
