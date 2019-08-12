package com.ciba.wholefinancial.bean;

import java.io.Serializable;

//商家会员实体
public class BusinessUserBean implements Serializable {
    public int id;//	序号
    public int userId;//	会员id
    public String phoneNumber;//	账号/手机号码
    public String balance;//	余额
    public String createTime;//	注册时间

    @Override
    public String toString() {
        return "BusinessUserBean{" +
                "id=" + id +
                ", userId=" + userId +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", balance='" + balance + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
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
}
