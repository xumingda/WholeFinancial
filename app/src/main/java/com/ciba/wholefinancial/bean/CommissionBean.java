package com.ciba.wholefinancial.bean;

import java.io.Serializable;

public class CommissionBean implements Serializable {
    public int id;//	序号
    public String commission;//	佣金
    public String commissionRate;//	佣金利率
    public String createTime;//	收益时间
    public String  businessName	;//商家名称
    public String  shopName	;//商家店铺名称
    public String  code	;//餐桌码
    public String orderMoney;//	订单金额
    public String  needMoney;//	实付金额

    @Override
    public String toString() {
        return "CommissionBean{" +
                "id=" + id +
                ", commission='" + commission + '\'' +
                ", commissionRate='" + commissionRate + '\'' +
                ", createTime='" + createTime + '\'' +
                ", businessName='" + businessName + '\'' +
                ", shopName='" + shopName + '\'' +
                ", code='" + code + '\'' +
                ", orderMoney='" + orderMoney + '\'' +
                ", needMoney='" + needMoney + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(String commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getNeedMoney() {
        return needMoney;
    }

    public void setNeedMoney(String needMoney) {
        this.needMoney = needMoney;
    }
}
