package com.slabs.exchange.common.interceptor;

import com.slabs.exchange.common.exception.AbstractExchangeException;
import com.slabs.exchange.common.exception.RespBizMessageEnum;
import com.slabs.exchange.common.exception.RespCommMessageEnum;
import com.slabs.exchange.common.exception.RespDataMessageEnum;
import com.slabs.exchange.model.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import springfox.documentation.annotations.ApiIgnore;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;


/**
 * Created by pengchangguo on 18/14/1.
 */
@ApiIgnore
@RestController
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * ExchangeException异常统一处理
     *
     * @param request
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = AbstractExchangeException.class)
    public ResponseDto bizErrorHandler(HttpServletRequest request, Exception ex) throws Exception {
        AbstractExchangeException ce = ((AbstractExchangeException) ex);

        log.error("**执行失败:HOST:{},URI:{},异常为:{},异常stack:{}", request.getRemoteHost(),
                request.getRequestURL(), ce.getMessage(), ExceptionUtils.getStackTrace(ce));
        return new ResponseDto(ce.getErrorCode(), ((AbstractExchangeException) ex).getMessage());

    }

    /**
     * Exception 统一异常处理
     *
     * @param request
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseDto defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        log.error("**执行失败:HOST:{},URI:{},异常stack:{}", request.getRemoteHost(), request.getRequestURL(), ExceptionUtils.getStackTrace(e));
        return new ResponseDto(RespBizMessageEnum.UNKNOW_ERROR.getErrorCode(), RespBizMessageEnum.UNKNOW_ERROR.getErrorMsg());
    }

    /**
     * MultipartException 上传文件异常处理
     *
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = MultipartException.class)
    public ResponseDto multipartExceededException(HttpServletRequest request, Exception e) throws Exception {
        log.error("**执行失败:HOST:{},URI:{},上传文件超出限制", request.getRemoteHost(), request.getRequestURL());
        return new ResponseDto(RespBizMessageEnum.FILE_ERROR.getErrorCode(), "上传文件超出限制");
    }

    /**
     * HttpMessageNotReadableException/400错误
     *
     * @return
     * @throws Exception
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseDto requestNotReadable(HttpServletRequest request, HttpMessageNotReadableException ex) {
        log.error("**执行失败:HOST:{},URI:{},异常stack:{}", request.getRemoteHost(), request.getRequestURL(), ExceptionUtils.getStackTrace(ex));
        return new ResponseDto(RespCommMessageEnum.UNLAWFUL_ERROR.getErrorCode(), RespCommMessageEnum.UNLAWFUL_ERROR.getErrorMsg());

    }

    /**
     * 捕捉AuthenticationException异常
     */
    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseDto authenticationException(HttpServletRequest request, AuthenticationException e) {

        log.error("**执行失败:HOST:{},URI:{},异常为:{},异常stack:{}", request.getRemoteHost(),
                request.getRequestURL(), e.getMessage(), ExceptionUtils.getStackTrace(e));
        return new ResponseDto(RespCommMessageEnum.AUTH_SESSION_ERROR.getErrorCode(), RespCommMessageEnum.AUTH_SESSION_ERROR.getErrorMsg());

    }

    /**
     * 捕捉AuthorizationException异常
     */
    @ExceptionHandler(value = AuthorizationException.class)
    public ResponseDto authorizationException(HttpServletRequest request, AuthorizationException e) {
        log.error("**执行失败:HOST:{},URI:{},异常为:{},异常stack:{}", request.getRemoteHost(),
                request.getRequestURL(), e.getMessage(), ExceptionUtils.getStackTrace(e));
        return new ResponseDto(RespCommMessageEnum.INSUFFICIENT_PRIVILEGE.getErrorCode(), RespCommMessageEnum.INSUFFICIENT_PRIVILEGE.getErrorMsg());

    }

    /**
     * 捕捉IncorrectCredentialsException异常
     */
    @ExceptionHandler(value = IncorrectCredentialsException.class)
    public ResponseDto incorrectCredentialsException(HttpServletRequest request, IncorrectCredentialsException e) {
        log.error("**执行失败:HOST:{},URI:{},异常为:{},异常stack:{}", request.getRemoteHost(),
                request.getRequestURL(), e.getMessage(), ExceptionUtils.getStackTrace(e));
        return new ResponseDto(RespCommMessageEnum.INCORRECT_CREDENTIALS.getErrorCode(), RespCommMessageEnum.INCORRECT_CREDENTIALS.getErrorMsg());

    }

    /**
     * 捕捉LockedAccountException异常
     */
    @ExceptionHandler(value = LockedAccountException.class)
    public ResponseDto lockedAccountException(HttpServletRequest request, LockedAccountException e) {
        log.error("**执行失败:HOST:{},URI:{},异常为:{},异常stack:{}", request.getRemoteHost(),
                request.getRequestURL(), e.getMessage(), ExceptionUtils.getStackTrace(e));
        return new ResponseDto(RespCommMessageEnum.LOCKED_ACCOUNT.getErrorCode(), RespCommMessageEnum.LOCKED_ACCOUNT.getErrorMsg());

    }

    /**
     * 捕捉UnknownAccountException异常
     */
    @ExceptionHandler(value = UnknownAccountException.class)
    public ResponseDto lockedAccountException(HttpServletRequest request, UnknownAccountException e) {
        log.error("**执行失败:HOST:{},URI:{},异常为:{},异常stack:{}", request.getRemoteHost(),
                request.getRequestURL(), e.getMessage(), ExceptionUtils.getStackTrace(e));
        return new ResponseDto(RespCommMessageEnum.NO_EXIST.getErrorCode(), RespCommMessageEnum.NO_EXIST.getErrorMsg());

    }

    /**
     * 用于处理验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto validException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        String errorMesssage = "校验失败:";

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage += fieldError.getDefaultMessage() + ", ";
        }
        return new ResponseDto(RespCommMessageEnum.UNLAWFUL_ERROR.getErrorCode(), errorMesssage);
    }

    /**
     * 用于SQL通用异常
     */
    @ExceptionHandler(SQLException.class)
    public ResponseDto sqlException(HttpServletRequest request, SQLException e) {
        log.error("**执行失败:HOST:{},URI:{},异常为:{},异常stack:{}", request.getRemoteHost(),
                request.getRequestURL(), e.getMessage(), ExceptionUtils.getStackTrace(e));
        return new ResponseDto(RespDataMessageEnum.DATABASE_QUERY_EXCEPTIONAL.getErrorCode(), RespDataMessageEnum.DATABASE_QUERY_EXCEPTIONAL.getErrorMsg());
    }

    /**
     * 用于数据库异常
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseDto dataIntegrityViolationException(HttpServletRequest request, SQLException e) {
        log.error("**执行失败:HOST:{},URI:{},异常为:{},异常stack:{}", request.getRemoteHost(),
                request.getRequestURL(), e.getMessage(), ExceptionUtils.getStackTrace(e));
        return new ResponseDto(RespDataMessageEnum.MORE_THAN_RECORD_MAXIMUM.getErrorCode(), RespDataMessageEnum.MORE_THAN_RECORD_MAXIMUM.getErrorMsg());
    }

}
