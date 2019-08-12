package com.ciba.wholefinancial.bean;

import java.io.Serializable;

//商户实体
public class MerchantBean implements Serializable {
    public int id;//	商家id（businessId）, 之后一些接口估计要用到这个值
    public int agentId;//	代理商id
    public int salesmanId;//	业务员id
    public String idCardName;//	身份证姓名
    public String idCardCopy	;//身份证人像面照片
    public String idCardNational	;//身份证国徽面照片

    public String getIdCardValidStartTime() {
        return idCardValidStartTime;
    }

    public void setIdCardValidStartTime(String idCardValidStartTime) {
        this.idCardValidStartTime = idCardValidStartTime;
    }

    public String getIdCardValidEndTime() {
        return idCardValidEndTime;
    }

    public void setIdCardValidEndTime(String idCardValidEndTime) {
        this.idCardValidEndTime = idCardValidEndTime;
    }

    public String  idCardValidStartTime;
    public String         idCardValidEndTime;
    public String idCardNumber;//身份证号
    public String  accountName;//	开户名称,必须与身份证姓名一致
    public String  accountBank;//	开户银行
    public String  bankName;//	开户银行全称（含支行）

    public String accountNumber	;//银行账号
    public String bankAddressCode;//	开户银行省市编码 110000
    public String storeName;//	门店名称
    public String storeStreet;//	店铺详细地址，具体区/县及街道门牌号或大厦楼层，最长500个中文字符（无需填写省市信息）
    public String  storeEntrancePic	;//门店门口照片
    public String storeAddressCode;//	门店省市编码  110000
    public String indoorPic;//	店内环境照片
    public String merchantShortname;//	商户简称
    public String addressCertification;//	经营场地证明照片，只有一张
    public String licensePic;//	营业执照
    public String servicePhone	;//客服电话
    public String productDesc;//	售卖商品/提供服务描述
    public String  bankPic1;	//银行卡正面照片
    public String  bankPic2	;//银行卡反面照片

    public String weixinPayRate;//	费率

    public String       alipayRate;//
    public String xqRate;//

    public String contact;//	联系人姓名,和身份证姓名一致
    public String contactPhone;//	手机号码
    public String remark;//	备注
    public int status;//	状态0未审核，1审核通过,正常，有效，2审核失败，失效，3申请入驻中，4入驻失败
    public String createTime;//	创建账号时间
    public String weixinNumber;//	微信账号
    public String alipayNumber;//	支付宝账号

    public String creditCode;
    public String businessScope;
    public String specialPic;

    public String getBankPic1() {
        return bankPic1;
    }

    public void setBankPic1(String bankPic1) {
        this.bankPic1 = bankPic1;
    }

    public String getBankPic2() {
        return bankPic2;
    }

    public void setBankPic2(String bankPic2) {
        this.bankPic2 = bankPic2;
    }

    public String getSpecialPic() {
        return specialPic;
    }

    public void setSpecialPic(String specialPic) {
        this.specialPic = specialPic;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(int salesmanId) {
        this.salesmanId = salesmanId;
    }

    public String getIdCardName() {
        return idCardName;
    }

    public void setIdCardName(String idCardName) {
        this.idCardName = idCardName;
    }

    public String getIdCardCopy() {
        return idCardCopy;
    }

    public void setIdCardCopy(String idCardCopy) {
        this.idCardCopy = idCardCopy;
    }

    public String getIdCardNational() {
        return idCardNational;
    }

    public void setIdCardNational(String idCardNational) {
        this.idCardNational = idCardNational;
    }



    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankAddressCode() {
        return bankAddressCode;
    }

    public void setBankAddressCode(String bankAddressCode) {
        this.bankAddressCode = bankAddressCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreStreet() {
        return storeStreet;
    }

    public void setStoreStreet(String storeStreet) {
        this.storeStreet = storeStreet;
    }

    public String getStoreEntrancePic() {
        return storeEntrancePic;
    }

    public void setStoreEntrancePic(String storeEntrancePic) {
        this.storeEntrancePic = storeEntrancePic;
    }

    public String getStoreAddressCode() {
        return storeAddressCode;
    }

    public void setStoreAddressCode(String storeAddressCode) {
        this.storeAddressCode = storeAddressCode;
    }

    public String getIndoorPic() {
        return indoorPic;
    }

    public void setIndoorPic(String indoorPic) {
        this.indoorPic = indoorPic;
    }

    public String getMerchantShortname() {
        return merchantShortname;
    }

    public void setMerchantShortname(String merchantShortname) {
        this.merchantShortname = merchantShortname;
    }

    public String getAddressCertification() {
        return addressCertification;
    }

    public void setAddressCertification(String addressCertification) {
        this.addressCertification = addressCertification;
    }

    public String getLicensePic() {
        return licensePic;
    }

    public void setLicensePic(String licensePic) {
        this.licensePic = licensePic;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getWeixinNumber() {
        return weixinNumber;
    }

    public void setWeixinNumber(String weixinNumber) {
        this.weixinNumber = weixinNumber;
    }

    public String getAlipayNumber() {
        return alipayNumber;
    }

    public void setAlipayNumber(String alipayNumber) {
        this.alipayNumber = alipayNumber;
    }

    public String getWeixinPayRate() {
        return weixinPayRate;
    }

    public void setWeixinPayRate(String weixinPayRate) {
        this.weixinPayRate = weixinPayRate;
    }

    public String getAlipayRate() {
        return alipayRate;
    }

    public void setAlipayRate(String alipayRate) {
        this.alipayRate = alipayRate;
    }

    public String getXqRate() {
        return xqRate;
    }

    public void setXqRate(String xqRate) {
        this.xqRate = xqRate;
    }

    @Override
    public String toString() {
        return "MerchantBean{" +
                "id=" + id +
                ", agentId=" + agentId +
                ", salesmanId=" + salesmanId +
                ", idCardName='" + idCardName + '\'' +
                ", idCardCopy='" + idCardCopy + '\'' +
                ", idCardNational='" + idCardNational + '\'' +
                ", idCardValidStartTime='" + idCardValidStartTime + '\'' +
                ", idCardValidEndTime='" + idCardValidEndTime + '\'' +
                ", idCardNumber='" + idCardNumber + '\'' +
                ", accountName='" + accountName + '\'' +
                ", accountBank='" + accountBank + '\'' +
                ", bankName='" + bankName + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", bankAddressCode='" + bankAddressCode + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storeStreet='" + storeStreet + '\'' +
                ", storeEntrancePic='" + storeEntrancePic + '\'' +
                ", storeAddressCode='" + storeAddressCode + '\'' +
                ", indoorPic='" + indoorPic + '\'' +
                ", merchantShortname='" + merchantShortname + '\'' +
                ", addressCertification='" + addressCertification + '\'' +
                ", licensePic='" + licensePic + '\'' +
                ", servicePhone='" + servicePhone + '\'' +
                ", productDesc='" + productDesc + '\'' +
                ", bankPic1='" + bankPic1 + '\'' +
                ", bankPic2='" + bankPic2 + '\'' +
                ", weixinPayRate='" + weixinPayRate + '\'' +
                ", alipayRate='" + alipayRate + '\'' +
                ", xqRate='" + xqRate + '\'' +
                ", contact='" + contact + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", remark='" + remark + '\'' +
                ", status=" + status +
                ", createTime='" + createTime + '\'' +
                ", weixinNumber='" + weixinNumber + '\'' +
                ", alipayNumber='" + alipayNumber + '\'' +
                ", creditCode='" + creditCode + '\'' +
                ", businessScope='" + businessScope + '\'' +
                ", specialPic='" + specialPic + '\'' +
                '}';
    }
}
