package com.ciba.wholefinancial.bean;

import java.io.Serializable;

//商家会员实体
public class GoodsBean implements Serializable {
    public int goodsId;//	商品id
    public int classId;//	分类id
    public String goodsName;//	商品名称
    public String goodsPic;//	商品图片
    public String goodsPrice;//	商品价格
    public String goodsUnit;//		商品单位
    public String goodsDesc;//	商品详情
    public String createTime;//		增加商品时间


    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "GoodsBean{" +
                "goodsId=" + goodsId +
                ", classId=" + classId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPic='" + goodsPic + '\'' +
                ", goodsPrice='" + goodsPrice + '\'' +
                ", goodsUnit='" + goodsUnit + '\'' +
                ", goodsDesc='" + goodsDesc + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
