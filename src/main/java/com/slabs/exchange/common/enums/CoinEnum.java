package com.slabs.exchange.common.enums;

public enum  CoinEnum {
    USDT("usdt", "稳定币"),
    HOS("hos", "平台币");

    private String key;
    private String value;

    CoinEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static CoinEnum getByKey(String key){
        for(CoinEnum coinEnum : values()){
            if (coinEnum.getKey().equals(key)) {
                return coinEnum;
            }
        }
        return null;
    }
}
