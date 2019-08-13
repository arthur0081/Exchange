package com.slabs.exchange.common.exception;


public interface RespMessage {
    /**
     * @return
     * 错误码code
     */
    String getErrorCode();

    /**
     * @return
     * 错误message
     */
    String getErrorMsg();
}
