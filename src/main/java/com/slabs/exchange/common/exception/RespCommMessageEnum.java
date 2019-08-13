package com.slabs.exchange.common.exception;

/**
 * 通讯错误
 */
public enum RespCommMessageEnum implements RespMessage {

    TIMEOUT("100001", "超时"),
    CONNECTION_ERROR("100002", "连接错误"),
    PORT_HAS_BEEN_USED("100003", "端口已被占用"),
    AUTH_SESSION_ERROR("100004", "获取登录鉴权信息失败"),
    UNLAWFUL_ERROR("100005", "非法请求"),
    INSUFFICIENT_PRIVILEGE("100006", "权限不够"),
    NO_EXIST("100007", "用户名不存在"),
    PHONE_REGISTERED("100008", "手机号已注册"),
    INCORRECT_CREDENTIALS("100009", "账号或密码不正确"),
    LOCKED_ACCOUNT("100010", "账号已被锁定,请联系管理员"),
    ADMIN_CANNOT("100011", "管理员账号不可修改"),
    SMS_NOT_NULL("100012", "短信码不能为空"),
    SMS_INVALID("100013", "无有效短信码,请重新发送短信"),
    SMS_ERROR("100014", "短信码错误"),
    SMS_EXPIRE("100015", "短信码过期"),
    NOT_SUFFICIENT_FUNDS("100016", "余额不足"),
    NO_TRANSACTION("100017", "交易信息为空"),
    PHONE_ERROR("100018","请填入正确的手机号"),
    CAPTCHA_ERROR("100020","图形验证码错误"),
    IDCARD_ERROR("100021","请填入正确的身份证号");

    private String errorCode;
    private String errorMsg;

    /**
     * 构造方法
     *
     * @param errorCode
     * @param errorMsg
     */
    RespCommMessageEnum(String errorCode, String errorMsg) {
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
