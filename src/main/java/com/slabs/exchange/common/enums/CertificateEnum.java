package com.slabs.exchange.common.enums;

public enum CertificateEnum {

    IDCARD_FRONT("idcard_front", "身份证正面"),
    IDCARD_BACK("idcard_back", "身份证反面"),
    PASSPORT("passport", "护照");

    private String key;
    private String value;

    CertificateEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static CertificateEnum getByKey(String key){
        for(CertificateEnum certificateEnum : values()){
            if (certificateEnum.getKey().equals(key)) {
                return certificateEnum;
            }
        }
        return null;
    }

}
