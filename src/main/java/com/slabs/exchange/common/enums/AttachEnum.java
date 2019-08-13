package com.slabs.exchange.common.enums;

public enum AttachEnum {
    PRO_COIN("pro_coin", "项目币"),
    HOS("hos", "平台币"),
    USDT("usdt", "稳定币"),
    REAL_ESTATE("real_estate", "房地产"),

    PRO_PICTURE("pro_picture", "项目图片"),
    STRUCTURE_PICTURE("structure_picture", "项目运营结构图"),
    PRO_REF_FILE("pro_ref_picture", "项目相关图"),
    PRO_REF_LAW_FILE("pro_ref_law_file", "项目相关法律文件"),
    PRO_BUY_INVEST_CONTRACT("pro_buy_invest_contract","项目购买投资合同");

    private String key;
    private String value;

    AttachEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static AttachEnum getByKey(String key){
        for(AttachEnum attachEnum : values()){
            if (attachEnum.getKey().equals(key)) {
                return attachEnum;
            }
        }
        return null;
    }
}
