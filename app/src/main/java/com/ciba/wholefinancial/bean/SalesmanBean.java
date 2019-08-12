package com.ciba.wholefinancial.bean;

import java.io.Serializable;

public class SalesmanBean  implements Serializable {
    public int id;//	业务员id(salesmanId)，之后一些接口会用到这个值
    public String name;//	姓名
    public String account;//	账号（电话号码）
    public String password;//	密码
    public String phoneNumber	;//电话号码
    public int businessCount;//	发展商家数
    public int agentId;//	所属代理商id
    public String commissionRate;//	佣金利率/笔
    public String commission;//	总佣金(收入)
    public String unSettlementCommission;//	未结算佣金
    public String frozenCommission;//	冻结佣金
    public String settlementCommission	;//已结算佣金
    public int status;//	状态（是否可用）,0否，1是
    public String longitude	;//工作地点经度
    public String dimension;//	工作地点纬度
    public String createTime;//	创建账号时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getBusinessCount() {
        return businessCount;
    }

    public void setBusinessCount(int businessCount) {
        this.businessCount = businessCount;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(String commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getUnSettlementCommission() {
        return unSettlementCommission;
    }

    public void setUnSettlementCommission(String unSettlementCommission) {
        this.unSettlementCommission = unSettlementCommission;
    }

    public String getFrozenCommission() {
        return frozenCommission;
    }

    public void setFrozenCommission(String frozenCommission) {
        this.frozenCommission = frozenCommission;
    }

    public String getSettlementCommission() {
        return settlementCommission;
    }

    public void setSettlementCommission(String settlementCommission) {
        this.settlementCommission = settlementCommission;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SalesmanBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", businessCount=" + businessCount +
                ", agentId=" + agentId +
                ", commissionRate='" + commissionRate + '\'' +
                ", commission='" + commission + '\'' +
                ", unSettlementCommission='" + unSettlementCommission + '\'' +
                ", frozenCommission='" + frozenCommission + '\'' +
                ", settlementCommission='" + settlementCommission + '\'' +
                ", status=" + status +
                ", longitude='" + longitude + '\'' +
                ", dimension='" + dimension + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
