package com.slabs.exchange.common.enums;

public enum BuyAndSellEnum {

    SELL("sell", "挂卖单"),
    BUY("buy", "挂买单");

    private String key;
    private String value;

    BuyAndSellEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static BuyAndSellEnum getByKey(String key){
        for(BuyAndSellEnum buyAndSellEnum : values()){
            if (buyAndSellEnum.getKey().equals(key)) {
                return buyAndSellEnum;
            }
        }
        return null;
    }
}
