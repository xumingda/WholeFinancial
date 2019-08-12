package com.ciba.wholefinancial.bean;

import java.io.Serializable;

//商家会员实体
public class TypeBean implements Serializable {
    public String consumeFullMoney	;//消费满多少金额0.00,type=1必填
    public String subMoney;//	减多少金额0.00,type=1必填
    public String levelDiscount	;//等级折扣0.00,type=2必填
    public String consumeFullCount;//	消费满多少次0,type=3必填
    public String countDiscount;//	消费折扣0.00,type=3必填
    public String validDay;
    public String businessLevelId;

    public String getValidDay() {
        return validDay;
    }

    public void setValidDay(String validDay) {
        this.validDay = validDay;
    }

    public String getBusinessLevelId() {
        return businessLevelId;
    }

    public void setBusinessLevelId(String businessLevelId) {
        this.businessLevelId = businessLevelId;
    }

    public String getConsumeFullMoney() {
        return consumeFullMoney;
    }

    public void setConsumeFullMoney(String consumeFullMoney) {
        this.consumeFullMoney = consumeFullMoney;
    }

    public String getSubMoney() {
        return subMoney;
    }

    public void setSubMoney(String subMoney) {
        this.subMoney = subMoney;
    }

    public String getLevelDiscount() {
        return levelDiscount;
    }

    public void setLevelDiscount(String levelDiscount) {
        this.levelDiscount = levelDiscount;
    }

    public String getConsumeFullCount() {
        return consumeFullCount;
    }

    public void setConsumeFullCount(String consumeFullCount) {
        this.consumeFullCount = consumeFullCount;
    }

    public String getCountDiscount() {
        return countDiscount;
    }

    public void setCountDiscount(String countDiscount) {
        this.countDiscount = countDiscount;
    }

}
