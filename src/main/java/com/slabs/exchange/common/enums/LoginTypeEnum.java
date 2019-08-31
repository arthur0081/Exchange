package com.slabs.exchange.common.enums;

public enum LoginTypeEnum {

    BACK("back", "后台登陆"),
    FORE("fore", "前台登陆");

    private String key;
    private String value;

    LoginTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static LoginTypeEnum getByKey(String key){
        for(LoginTypeEnum loginTypeEnum : values()){
            if (loginTypeEnum.getKey().equals(key)) {
                return loginTypeEnum;
            }
        }
        return null;
    }
}
