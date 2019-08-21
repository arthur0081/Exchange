package com.slabs.exchange.common.enums;

public enum WithdrawStatusEnum {
    DEFAULT("0", "默认状态"),
    SUCCEED("1", "挂单成功"),//当项目募集资金成功的时候
    WITHDRAW_NEED("2","需要撤回"),
    WITHDRAW_SUCCEED("3", "成功撤回");

    private String key;
    private String value;

    WithdrawStatusEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static WithdrawStatusEnum getByKey(String key){
        for(WithdrawStatusEnum withdrawStatusEnum : values()){
            if (withdrawStatusEnum.getKey().equals(key)) {
                return withdrawStatusEnum;
            }
        }
        return null;
    }
}
