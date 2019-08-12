package com.ciba.wholefinancial.bean;

import java.io.Serializable;

public class ClockBean implements Serializable {
    private String attendanceId;//	考勤批次id
    private String salesmanId;//	业务员id
    private int status;//	状态（0 等待考勤，1 正常考勤，2 超时考勤，3 修正考勤，4 缺卡，5 考勤不通过 )，注意，这里状态为0是打卡的意思，其他状态是在历史列表那里显示
    private String createTime;
    private String clickTime;//	打卡时间
    private String clickPlace;//	打卡位置
    private String pic;//	图片

    public String getClickTime() {
        return clickTime;
    }

    public void setClickTime(String clickTime) {
        this.clickTime = clickTime;
    }

    public String getClickPlace() {
        return clickPlace;
    }

    public void setClickPlace(String clickPlace) {
        this.clickPlace = clickPlace;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(String salesmanId) {
        this.salesmanId = salesmanId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClockBean{" +
                "attendanceId='" + attendanceId + '\'' +
                ", salesmanId='" + salesmanId + '\'' +
                ", status=" + status +
                ", createTime='" + createTime + '\'' +
                ", clickTime='" + clickTime + '\'' +
                ", clickPlace='" + clickPlace + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
