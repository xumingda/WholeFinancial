package com.ciba.wholefinancial.bean;

import java.io.Serializable;

public class BalanceBean implements Serializable {
    public int id;//	序号
    public int userId;//	会员id
    public int businessId;//	商家id
    public int type;//	类型（0充值，1余额支付）
    public int orderId;//	订单id,type=1有值，这个可以用来查看消费清单,4.1.15接口用到
    public String money;//	涉及金额
    public String remark;//		备注
    public String createTime;//		记录时间


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

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
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

    @Override
    public String toString() {
        return "BalanceBean{" +
                "id=" + id +
                ", userId=" + userId +
                ", businessId=" + businessId +
                ", type=" + type +
                ", orderId=" + orderId +
                ", money='" + money + '\'' +
                ", remark='" + remark + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
