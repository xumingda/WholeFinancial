package com.ciba.wholefinancial.bean;

import java.io.Serializable;

//商户实体
public class OrderGoodsBean implements Serializable {
    public int id;//序号
    public int goodsId	;//商品id
    public String goodsName	;//商品名称
    public String goodsPic;//		商品图片
    public String goodsPrice;//	商品价格
    public String goodsUnit;//	商品单位
    public String remark;//	备注
    public int count;//	多少份

    @Override
    public String toString() {
        return "OrderGoodsBean{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPic='" + goodsPic + '\'' +
                ", goodsPrice='" + goodsPrice + '\'' +
                ", goodsUnit='" + goodsUnit + '\'' +
                ", remark='" + remark + '\'' +
                ", count=" + count +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
