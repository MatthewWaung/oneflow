package com.oneflow.prm.core.handler;

import com.oneflow.prm.core.common.R;
import com.oneflow.prm.core.enums.ExceptionEnums;
import com.oneflow.prm.core.exception.BaseException;
import com.oneflow.prm.core.exception.CustomException;
import com.oneflow.prm.core.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * 全局异常处理器
 *
 * */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 基础异常
     */
    @ExceptionHandler(BaseException.class)
    public R baseException(BaseException e) {
        return R.fail(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(CustomException.class)
    public R businessException(CustomException e) {
        if (StringUtil.isNull(e.getCode())) {
            return R.fail(e.getDefaultMessage());
        }
        String module = e.getModule();
        String message = e.getDefaultMessage();
        if (Objects.nonNull(module)) {
            message = module + ":" + message;
        }
        return R.fail(e.getArgs(), Integer.parseInt(e.getCode()), message);
    }

    /**
     * 参数范围错误校验
     *
     * @param e 异常
     * @return ResponseEntity
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public R<String> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuffer stringBuffer = new StringBuffer();
        BindingResult bindingResult = e.getBindingResult();
        //展示具体的值和错误，如[1234567890987654321]:长度不能超过10
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        fieldErrors.forEach(fieldError -> stringBuffer.append("[").append(fieldError.getRejectedValue()).append("]").append(":").append(fieldError.getDefaultMessage()).append(" "));

        //仅仅展示错误提示
//        List<ObjectError> allErrors = bindingResult.getAllErrors();
//        allErrors.forEach(error -> stringBuffer.append(error.getDefaultMessage()).append(" "));
        log.error("参数范围错误校验 ,message {}", e.getMessage());
        return R.fail(Integer.parseInt(ExceptionEnums.PARAMETERS_EMPTY_EXCEPTION.getCode()), stringBuffer + "");
    }

    /**
     * 参数不合法
     *
     * @param e 异常
     * @return ResponseEntity
     */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public R<String> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        String opeContent = ExceptionUtils.getStackTrace(e);
        log.error("参数不合法 ,message {}", opeContent);
        Throwable rootCause = ExceptionUtils.getRootCause(e);
        String message = null;
        if (Objects.nonNull(rootCause)) {
            message = rootCause.getMessage();
        }
        return R.fail(Integer.parseInt(ExceptionEnums.ILLEGAL_ARGUMENT.getCode()), message);
    }

    /**
     * 请求参数错误异常的捕获
     *
     * @param e 异常
     * @return ResponseEntity
     */
    @ExceptionHandler(value = {BindException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public R<String> badRequest(BindException e) {
        log.error("请求参数错误异常的捕获 ,message {}", e.getMessage());
        StringBuffer stringBuffer = new StringBuffer();
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getAllErrors().forEach(error -> stringBuffer.append(error.getDefaultMessage()).append(" "));
        return R.fail(Integer.parseInt(ExceptionEnums.PARAMETERS_EMPTY_EXCEPTION.getCode()), stringBuffer + "");
    }

    /**
     * 请求方法不匹配异常
     *
     * @param e 异常
     * @return ResponseEntity
     */
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public R<String> methodRequestNotFound(HttpRequestMethodNotSupportedException e) {
        String opeContent = ExceptionUtils.getStackTrace(e);
        log.error("请求方法不匹配 ,message {}", opeContent);
        return R.fail(Integer.parseInt(ExceptionEnums.REQUEST_METHOD_NOT_MATCH.getCode()), ExceptionEnums.REQUEST_METHOD_NOT_MATCH.getMessage());
    }

    /**
     * 404错误异常的捕获
     *
     * @param e 异常
     * @return ResponseEntity
     */
    @ExceptionHandler(value = {NoHandlerFoundException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public R<String> badRequestNotFound(NoHandlerFoundException e) {
        String opeContent = ExceptionUtils.getStackTrace(e);
        log.error("找不到请求路径 ,message {}", opeContent);
        return R.fail(Integer.parseInt(ExceptionEnums.PATH_NOT_FOUND.getCode()), ExceptionEnums.PATH_NOT_FOUND.getMessage());
    }

    /**
     * 数据库操作出现异常
     *
     * @param e 异常
     * @return ResponseEntity
     */
    @ExceptionHandler(value = {SQLException.class, DataAccessException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public R<String> systemError(Exception e) {
        String opeContent = ExceptionUtils.getStackTrace(e);
        log.error("数据库操作异常 ,message {}", opeContent);
        if (e instanceof MyBatisSystemException) {
            return R.fail(Integer.parseInt(ExceptionEnums.DATABASE_OPERATION_EXCEPTION.getCode()), ((MyBatisSystemException) e).getRootCause().getMessage());
        } else {
            return R.fail(Integer.parseInt(ExceptionEnums.DATABASE_OPERATION_EXCEPTION.getCode()), ExceptionEnums.DATABASE_OPERATION_EXCEPTION.getMessage());
        }
    }

    /**
     * 网络连接失败！
     *
     * @param e 异常
     * @return ResponseEntity
     */
    @ExceptionHandler(value = {ConnectException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public R<String> connect(Exception e) {
        String opeContent = ExceptionUtils.getStackTrace(e);
        log.error("网络连接失败 ,message {}", opeContent);
        return R.fail(Integer.parseInt(ExceptionEnums.CONNECTION_ERROR.getCode()), ExceptionEnums.CONNECTION_ERROR.getMessage());
    }


    /**
     * 内部异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public R<String> runTimeError(Exception e) {
        String opeContent = ExceptionUtils.getStackTrace(e);
        log.error("内部异常 ,message {}", opeContent);
        String errorMsg = ExceptionUtils.getRootCauseMessage(e);
        String[] split = opeContent.split("\\n");
        if (Objects.nonNull(split) && split.length >= 2) {
            errorMsg = split[0] + " " + split[1];
            errorMsg = errorMsg.replaceAll("\t", " ").replaceAll("\r", " ");
        }
        return R.fail(Integer.parseInt(ExceptionEnums.RUNTIME_ERROR.getCode()), errorMsg);
    }

    /**
     * 其它异常：如ServletException会报500（http状态码）异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public R<String> exceptions(Exception e) {
        String opeContent = ExceptionUtils.getStackTrace(e);
        log.error("内部异常 ,message {}", opeContent);
        String errorMsg = ExceptionUtils.getRootCauseMessage(e);
        return R.fail(Integer.parseInt(ExceptionEnums.INTERNAL_ERROR.getCode()), ExceptionEnums.INTERNAL_ERROR.getMessage());
    }
}
