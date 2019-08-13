package com.slabs.exchange.common.exception;

import lombok.Getter;

/**
 * @author joey
 * @date 2018/3/24
 */
@Getter
public abstract class AbstractExchangeException extends RuntimeException {
    /**
     * 错误编码
     */
    private String errorCode;
    /**
     * 错误信息
     */
    private String errorMsg;

    public AbstractExchangeException(String errorMsg) {
        this(RespBizMessageEnum.OPERATOR_ERROR.getErrorCode(), errorMsg);
    }

    public AbstractExchangeException(String errorMsg, Throwable cause) {
        this(RespBizMessageEnum.OPERATOR_ERROR.getErrorCode(), errorMsg, cause);
    }

    public AbstractExchangeException(String errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public AbstractExchangeException(String errorCode, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public AbstractExchangeException(RespMessage rspMsg) {
        this(rspMsg.getErrorCode(), rspMsg.getErrorMsg());
    }
}
