package com.slabs.exchange.common.exception;

import lombok.Getter;


@Getter
public class ExchangeException extends AbstractExchangeException {
    public ExchangeException(String errorMsg) {
        super(errorMsg);
    }

    public ExchangeException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }

    public ExchangeException(String errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public ExchangeException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode, errorMsg, cause);
    }

    public ExchangeException(RespMessage rspMsg) {
        super(rspMsg);
    }


}
