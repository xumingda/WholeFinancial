package com.ciba.wholefinancial.response;


/**
 * @作者: 许明达
 * @创建时间: 2016-4-6上午09:43:20
 * @版权: 特速版权所有
 * @描述: 封装服务器返回列表的参数
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class ClockCountResponse {
	
	/** 服务器响应码 */
	public int code;


	/** 服务器返回消息 */
	public String msg;



	public int attendanceCount;//	出勤
	public int outsideCount;//	外勤
	public int lateCount;//	迟到
	public int leaveEarlyCount;//	早退
	public int stayAwayCount;//	缺卡

	@Override
	public String toString() {
		return "ClockCountResponse{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", attendanceCount=" + attendanceCount +
				", outsideCount=" + outsideCount +
				", lateCount=" + lateCount +
				", leaveEarlyCount=" + leaveEarlyCount +
				", stayAwayCount=" + stayAwayCount +
				'}';
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getAttendanceCount() {
		return attendanceCount;
	}

	public void setAttendanceCount(int attendanceCount) {
		this.attendanceCount = attendanceCount;
	}

	public int getOutsideCount() {
		return outsideCount;
	}

	public void setOutsideCount(int outsideCount) {
		this.outsideCount = outsideCount;
	}

	public int getLateCount() {
		return lateCount;
	}

	public void setLateCount(int lateCount) {
		this.lateCount = lateCount;
	}

	public int getLeaveEarlyCount() {
		return leaveEarlyCount;
	}

	public void setLeaveEarlyCount(int leaveEarlyCount) {
		this.leaveEarlyCount = leaveEarlyCount;
	}

	public int getStayAwayCount() {
		return stayAwayCount;
	}

	public void setStayAwayCount(int stayAwayCount) {
		this.stayAwayCount = stayAwayCount;
	}
}
