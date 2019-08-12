package com.ciba.wholefinancial.bean;

import android.graphics.Color;

import com.ciba.wholefinancial.R;

import java.util.List;
import java.util.Map;

public class LevelBean {

    //序号，等级id，businessLevelId
    public  int id;
    //	等级名称
    public String levelName;

    @Override
    public String toString() {
        return "LevelBean{" +
                "id=" + id +
                ", levelName='" + levelName + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
