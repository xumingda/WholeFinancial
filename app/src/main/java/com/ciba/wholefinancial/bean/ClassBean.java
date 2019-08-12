package com.ciba.wholefinancial.bean;

import java.io.Serializable;

//商家会员实体
public class ClassBean implements Serializable {
    public int classId;//	分类id
    public String className;//	分类名称

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "ClassBean{" +
                "classId=" + classId +
                ", className='" + className + '\'' +
                '}';
    }
}
