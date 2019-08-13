package com.slabs.exchange.common.enums;

public enum AreaEnum {
    BREAK_EVEN("1", "保本区"),// 币对的分母是  USDT
    CREATION("2", "创意区");  // 币对的分母是  HOS

    private String key;
    private String value;

    AreaEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static AreaEnum getByKey(String key){
        for(AreaEnum areaEnum : values()){
            if (areaEnum.getKey().equals(key)) {
                return areaEnum;
            }
        }
        return null;
    }
}
