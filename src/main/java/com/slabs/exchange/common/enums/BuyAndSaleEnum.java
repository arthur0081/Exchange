package com.slabs.exchange.common.enums;

public enum BuyAndSaleEnum {

    SELL("sell", "挂卖单"),
    BUY("buy", "挂买单");

    private String key;
    private String value;

    BuyAndSaleEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static BuyAndSaleEnum getByKey(String key){
        for(BuyAndSaleEnum buyAndSaleEnum : values()){
            if (buyAndSaleEnum.getKey().equals(key)) {
                return buyAndSaleEnum;
            }
        }
        return null;
    }
}
