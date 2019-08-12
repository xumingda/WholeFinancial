package com.ciba.wholefinancial.bean;

import java.io.Serializable;

//商户实体
public class MarketingBean implements Serializable {
    public int activityId;//	活动id,4.1.5接口要用到
    public int type	;//活动类型设置（1消费金额满减，2等级优惠，3递增优惠）
    public String title	;//活动标题
    public String consumeFullMoney;//	消费满多少（type=1有值）
    public String subMoney;//	减多少（type=1有值）
    public String consumeFullCount;//	消费多少次（type=3有值）
    public String countDiscount;//	消费折扣（type=3有值）
    public int businessLevelId;//	商家设置的用户等级id（type=2有值）
    public String levelDiscount;//	等级折扣（type=2有值）
    public int userLevelId;//	参与对象等级
    public String levelName	;//参与对象等级名称
    public String startTime	;//活动开始时间
    public String endTime;//	活动结束时间
    public String remark;//	备注
    public String createTime;//	活动创建时间

    @Override
    public String toString() {
        return "MarketingBean{" +
                "activityId=" + activityId +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", consumeFullMoney='" + consumeFullMoney + '\'' +
                ", subMoney='" + subMoney + '\'' +
                ", consumeFullCount='" + consumeFullCount + '\'' +
                ", countDiscount='" + countDiscount + '\'' +
                ", businessLevelId=" + businessLevelId +
                ", levelDiscount='" + levelDiscount + '\'' +
                ", userLevelId=" + userLevelId +
                ", levelName='" + levelName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getBusinessLevelId() {
        return businessLevelId;
    }

    public void setBusinessLevelId(int businessLevelId) {
        this.businessLevelId = businessLevelId;
    }

    public String getLevelDiscount() {
        return levelDiscount;
    }

    public void setLevelDiscount(String levelDiscount) {
        this.levelDiscount = levelDiscount;
    }

    public int getUserLevelId() {
        return userLevelId;
    }

    public void setUserLevelId(int userLevelId) {
        this.userLevelId = userLevelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
