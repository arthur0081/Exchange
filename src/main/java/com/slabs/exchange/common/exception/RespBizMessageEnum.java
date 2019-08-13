package com.slabs.exchange.common.exception;

/**
 *
 * 业务返回信息
 */
public enum RespBizMessageEnum implements RespMessage {

    SUCCESS               ( "000000" , "成功"),
    REPEAT_ERROR          ( "666666" , "请勿重复提交"),
    FILE_ERROR            ( "777777" , "文件操作失败"),
    //msg建议覆盖
    OPERATOR_ERROR        ( "888888" , "操作失败"),
    UNKNOW_ERROR          ( "999999" , "未知错误");

    private String errorCode;
    private String errorMsg;

    /**
     * 构造方法
     *
     * @param errorCode
     * @param errorMsg
     */
    RespBizMessageEnum(String errorCode, String errorMsg)
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
