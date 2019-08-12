package com.ciba.wholefinancial.bean;

public class DateStateBean {
    public int item;
    //1出勤，2外勤，3异常
    private int state=1;

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
