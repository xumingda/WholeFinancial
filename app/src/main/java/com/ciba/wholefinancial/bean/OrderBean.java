package com.ciba.wholefinancial.bean;

import java.io.Serializable;

//商户实体
public class OrderBean implements Serializable {
    public int orderId;
    public int status	;//状态，0未支付，1已支付，2已取消
    public String orderMoney	;//订单金额
    public String needMoney	;//订单金额
    public String activeMoney	;//订单金额
    public String userMoney	;//订单金额
    public String code;//		餐桌码
    public String createTime;//	下单时间
    public String phoneNumber;//	下单人
    public String number;
    public String remark;
    public int eatType;//	就餐方式 0在店铺，1外卖
    public String contact;//	外卖联系人
    public String contactPhone;//	外卖联系人电话
    public String contactAddress	;//外卖联系人地址

    public String getNeedMoney() {
        return needMoney;
    }

    public void setNeedMoney(String needMoney) {
        this.needMoney = needMoney;
    }

    public String getActiveMoney() {
        return activeMoney;
    }

    public void setActiveMoney(String activeMoney) {
        this.activeMoney = activeMoney;
    }

    public String getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(String userMoney) {
        this.userMoney = userMoney;
    }

    public int getEatType() {
        return eatType;
    }

    public void setEatType(int eatType) {
        this.eatType = eatType;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "orderId=" + orderId +
                ", status=" + status +
                ", orderMoney='" + orderMoney + '\'' +
                ", needMoney='" + needMoney + '\'' +
                ", activeMoney='" + activeMoney + '\'' +
                ", userMoney='" + userMoney + '\'' +
                ", code='" + code + '\'' +
                ", createTime='" + createTime + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", number='" + number + '\'' +
                ", remark='" + remark + '\'' +
                ", eatType=" + eatType +
                ", contact='" + contact + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", contactAddress='" + contactAddress + '\'' +
                '}';
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
