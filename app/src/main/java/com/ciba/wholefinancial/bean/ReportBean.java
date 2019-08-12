package com.ciba.wholefinancial.bean;

import java.io.Serializable;

//商户实体
public class ReportBean implements Serializable {
    public int id;//	序号
    public int type	;//类型（0业务员举报，1商家举报）
    public int salesmanId;//	业务员id
    public int   businessId;//	商家id
    public String   name;//	举报人姓名
    public String phoneNumber;//	举报人电话号码
    public String title	;//举报标题
    public String content;//	举报内容
    public String pics;//	图片（多张，英文逗号分割）
    public String createTime;//	举报时间

    @Override
    public String toString() {
        return "ReportBean{" +
                "id=" + id +
                ", type=" + type +
                ", salesmanId=" + salesmanId +
                ", businessId=" + businessId +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", pics='" + pics + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(int salesmanId) {
        this.salesmanId = salesmanId;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
