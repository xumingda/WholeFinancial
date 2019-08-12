package com.ciba.wholefinancial.bean;

import java.io.Serializable;

//商户实体
public class MessageBean implements Serializable {
    public int id;//	序号
    public int type	;//类型（0业务员消息，1商家消息, 2用户消息）
    public String title	;//举报标题
    public String content;//	举报内容
    public String createTime;//	举报时间

    @Override
    public String toString() {
        return "MessageBean{" +
                "id=" + id +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
