package com.ciba.wholefinancial.bean;

import android.graphics.Color;

import com.ciba.wholefinancial.R;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BusinessShopBean implements Serializable {

    public int id;//	店铺id，businessShopId
    public String createTime;//	增加店铺时间
    public int codeCount;	//餐桌码数量
    public String businessId;//	商家id
    public String shopName;//	店铺名称
    public String qrUrl;//地址

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getCodeCount() {
        return codeCount;
    }

    public void setCodeCount(int codeCount) {
        this.codeCount = codeCount;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Override
    public String toString() {
        return "BusinessShopBean{" +
                "id=" + id +
                ", createTime='" + createTime + '\'' +
                ", codeCount=" + codeCount +
                ", businessId=" + businessId +
                ", shopName='" + shopName + '\'' +
                ", qrUrl='" + qrUrl + '\'' +
                '}';
    }
}
