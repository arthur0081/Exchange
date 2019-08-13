package com.slabs.exchange.common.exception;

/**
 * Created by zhaolei on 2018/03/10.
 *
 * 数据错误信息
 */
public enum RespDataMessageEnum implements RespMessage {

    NO_RECORD_BE_FOUND           ( "200001" , "没有找到任何记录"),
    MORE_THAN_RECORD_MAXIMUM     ( "200002" , "超过数据最大记录"),
    DATABASE_QUERY_EXCEPTIONAL   ( "200003" , "数据库查询异常");

    private String errorCode;
    private String errorMsg;

    /**
     * 构造方法
     *
     * @param errorCode
     * @param errorMsg
     */
    RespDataMessageEnum(String errorCode, String errorMsg)
    {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}
