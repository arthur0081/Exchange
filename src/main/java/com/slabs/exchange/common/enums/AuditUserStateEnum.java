package com.slabs.exchange.common.enums;

public enum AuditUserStateEnum {

    NO_AUDIT("0", "未审核（默认状态）"),
    AUDIT_PASS("1", "审核通过"),
    AUDIT_REJECT("2", "审核拒绝");

    private String key;
    private String value;

    AuditUserStateEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static AuditUserStateEnum getByKey(String key){
        for(AuditUserStateEnum auditUserStateEnum : values()){
            if (auditUserStateEnum.getKey().equals(key)) {
                return auditUserStateEnum;
            }
        }
        return null;
    }
}
