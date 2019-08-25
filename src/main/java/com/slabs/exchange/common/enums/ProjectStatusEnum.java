package com.slabs.exchange.common.enums;

public enum ProjectStatusEnum {
    SAVE("save", "可编辑"),//普通管理员在创建项目，点击保存
    SUBMIT("submit", "待审核"),//普通管理员在创建项目时，点击提交
    REJECT("reject", "拒绝上线"),//超级管理员  审核不通过
    PASS("pass","审核通过"),//超级管理员 审核通过
    PRE_SALE("pre_sale", "预售中"),//超级管理员   审核通过  当前时间小于项目开始时间
    ON_SALE("on_sale", "认购中"),//超级管理员   审核通过  当前时间大于项目开始时间
    END_SALE("end_sale","认购结束"),//在有效的时间内，募集到总额
    INTEREST("interest","计息中"),//认购结束了，但是还没有到清结算的日子（清结算日是最后认购时间）
    INVALID("invalid","失败"),//在有效的时间内，未募集到总额度
    PROJECT_END("project_end","项目结束");//这个是最终状态

    private String key;
    private String value;

    ProjectStatusEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static ProjectStatusEnum getByKey(String key){
        for(ProjectStatusEnum pse : values()){
            if (pse.getKey().equals(key)) {
                return pse;
            }
        }
        return null;
    }
}
