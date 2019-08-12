package com.ciba.wholefinancial.response;


import com.ciba.wholefinancial.bean.LevelBean;

import java.util.List;

/**
 * @作者: 许明达
 * @创建时间: 2016-4-6上午09:43:20
 * @版权: 特速版权所有
 * @描述: 封装服务器返回列表的参数
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class GetHomeResponse {
	
	/** 服务器响应码 */
	public int code;


	/** 服务器返回消息 */
	public String msg;


	public String todayOrderSucCount;//	今日订单数
	public double todayTurnover	;//今日营业额
	public double qrPayTurnover	;//扫码收款

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

	public String getTodayOrderSucCount() {
		return todayOrderSucCount;
	}

	public void setTodayOrderSucCount(String todayOrderSucCount) {
		this.todayOrderSucCount = todayOrderSucCount;
	}

	public double getTodayTurnover() {
		return todayTurnover;
	}

	public void setTodayTurnover(double todayTurnover) {
		this.todayTurnover = todayTurnover;
	}

	public double getQrPayTurnover() {
		return qrPayTurnover;
	}

	public void setQrPayTurnover(double qrPayTurnover) {
		this.qrPayTurnover = qrPayTurnover;
	}

	@Override
	public String toString() {
		return "GetHomeResponse{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", todayOrderSucCount='" + todayOrderSucCount + '\'' +
				", todayTurnover='" + todayTurnover + '\'' +
				", qrPayTurnover='" + qrPayTurnover + '\'' +
				'}';
	}
}
