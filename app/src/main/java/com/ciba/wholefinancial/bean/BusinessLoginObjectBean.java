package com.ciba.wholefinancial.bean;

import java.io.Serializable;

//商家会员实体
public class BusinessLoginObjectBean implements Serializable {
    public int master;//	是否为主账号,0否，1是，注意这个地方，如果不是主账号，就不给看会员列表信息
    public String name;//	姓名
    public String account;//登录账号

    public int getMaster() {
        return master;
    }

    public void setMaster(int master) {
        this.master = master;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "BusinessLoginObjectBean{" +
                "master='" + master + '\'' +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                '}';
    }
}
