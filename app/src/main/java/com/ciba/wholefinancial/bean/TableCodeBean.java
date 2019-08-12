package com.ciba.wholefinancial.bean;

import java.io.Serializable;

public class TableCodeBean implements Serializable {

    public int businessShopCodeId;//	店铺餐桌码id
    public String createTime;//	增加店铺餐桌码时间
    public String code;	//餐桌码
    public int businessId;//	商家id
    public String shopName;//	店铺名称

    public int businessShopId;//	店铺餐id
    public String qrcodeUrl;//	二维码图片url，下载要用到

    @Override
    public String toString() {
        return "TableCodeBean{" +
                "businessShopCodeId=" + businessShopCodeId +
                ", createTime='" + createTime + '\'' +
                ", code=" + code +
                ", businessId=" + businessId +
                ", shopName='" + shopName + '\'' +
                ", businessShopId=" + businessShopId +
                ", qrcodeUrl='" + qrcodeUrl + '\'' +
                '}';
    }

    public int getBusinessShopCodeId() {
        return businessShopCodeId;
    }

    public void setBusinessShopCodeId(int businessShopCodeId) {
        this.businessShopCodeId = businessShopCodeId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getBusinessShopId() {
        return businessShopId;
    }

    public void setBusinessShopId(int businessShopId) {
        this.businessShopId = businessShopId;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }
}
