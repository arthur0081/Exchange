package com.slabs.exchange.common.enums;

public enum  CodeEnum {
    OK("200", "成功"),
    Error("99999", "失败"),
    ARG_EXCEPTION("77777", "参数异常");

    private String key;
    private String value;

    CodeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static CodeEnum getByKey(String key){
        for(CodeEnum codeEnum : values()){
            if (codeEnum.getKey().equals(key)) {
                return codeEnum;
            }
        }
        return null;
    }

}
