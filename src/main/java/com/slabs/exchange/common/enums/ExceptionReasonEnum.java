package com.slabs.exchange.common.enums;

public enum ExceptionReasonEnum {

    //比如远程服务挂机，网络延迟
    NETWORK("network", "网络问题"),
    // 比如用户余额不足
    BUSINESS("business", "业务问题");

    private String key;
    private String value;

    ExceptionReasonEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static ExceptionReasonEnum getByKey(String key){
        for(ExceptionReasonEnum exceptionReasonEnum : values()){
            if (exceptionReasonEnum.getKey().equals(key)) {
                return exceptionReasonEnum;
            }
        }
        return null;
    }

}
