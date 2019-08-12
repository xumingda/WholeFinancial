package com.ciba.wholefinancial.bean;

import java.io.Serializable;

//商家会员实体
public class BusinessLoginBean implements Serializable {
    public int id;//	序号
    public int businessId;//	商家id
    public String account;//	账号
    public String name;//	姓名
    public String createTime;//	增加时间

    @Override
    public String toString() {
        return "BusinessLoginBean{" +
                "id=" + id +
                ", businessId=" + businessId +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
