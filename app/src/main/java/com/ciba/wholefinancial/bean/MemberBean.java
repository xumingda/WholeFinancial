package com.ciba.wholefinancial.bean;

import java.io.Serializable;

//商家会员实体
public class MemberBean implements Serializable {
    public int  id;//	序号
    public int  userId;//	会员id
    public String phoneNumber;//	账号/手机号码
    public String balance;//	余额
    public String createTime;//	注册时间
    public int businessLevelId;//	等级id，0表示无会员等级，这个返回去到下个界面详情有用到，比如，商家会员等级列表接口那里，是没有0返回的
    public String levelName;//	等级名称

    public int getBusinessLevelId() {
        return businessLevelId;
    }

    public void setBusinessLevelId(int businessLevelId) {
        this.businessLevelId = businessLevelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "MemberBean{" +
                "id=" + id +
                ", userId=" + userId +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", balance='" + balance + '\'' +
                ", createTime='" + createTime + '\'' +
                ", businessLevelId=" + businessLevelId +
                ", levelName='" + levelName + '\'' +
                '}';
    }
}
