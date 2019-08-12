package com.ciba.wholefinancial.bean;

import java.io.Serializable;

//商家会员实体
public class MemberLevelBean implements Serializable {
    public int id;//	等级id, businessLevelId
    public int businessId;//	商家id
    public int higherLevelId;//	高于等级id, 0表示无
    public int lowerLevelId;//	低于等级id, 0表示无
    public String createTime;//	增加时间

    public String levelName	;//等级名称
    public int count;//	消费达多少次
    public String money	;//消费满多少金额
    public String rate;//	等级折扣

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public int getHigherLevelId() {
        return higherLevelId;
    }

    public void setHigherLevelId(int higherLevelId) {
        this.higherLevelId = higherLevelId;
    }

    public int getLowerLevelId() {
        return lowerLevelId;
    }

    public void setLowerLevelId(int lowerLevelId) {
        this.lowerLevelId = lowerLevelId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "MemberLevelBean{" +
                "id=" + id +
                ", businessId=" + businessId +
                ", higherLevelId=" + higherLevelId +
                ", lowerLevelId=" + lowerLevelId +
                ", createTime='" + createTime + '\'' +
                ", levelName='" + levelName + '\'' +
                ", count=" + count +
                ", money='" + money + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
}
