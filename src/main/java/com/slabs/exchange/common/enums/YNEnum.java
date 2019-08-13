package com.slabs.exchange.common.enums;

public enum YNEnum {
    Y("Y", "肯定"),
    N("N", "否定");

    private String key;
    private String value;

    YNEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static YNEnum getByKey(String key){
        for(YNEnum ynEnum : values()){
            if (ynEnum.getKey().equals(key)) {
                return ynEnum;
            }
        }
        return null;
    }
}
