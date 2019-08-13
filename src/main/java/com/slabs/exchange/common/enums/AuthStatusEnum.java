package com.slabs.exchange.common.enums;

/**
 * 状态枚举
 *
 * @author joey
 * @date 2018/2/21
 */
public enum AuthStatusEnum {
    //待认证
    INIT("U"),
    //成功
    SUCCESS("S"),
    //失败
    FAIL("F"),
    //处理中
    AUTHING("P"),
    //冻结
    FREEZE("H"),
    //关闭
    CLOSE("C");
    private String key;
    private AuthStatusEnum (String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static AuthStatusEnum getByKey(String key){
        for(AuthStatusEnum athStatusEnum : values()){
            if (athStatusEnum.getKey().equals(key) ) {
                return athStatusEnum;
            }
        }
        return null;
    }

}
