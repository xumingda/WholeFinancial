package com.ciba.wholefinancial.response;


import com.ciba.wholefinancial.bean.MerchantBean;

/**
 * @作者: 许明达
 * @创建时间: 2016-4-6上午09:43:20
 * @描述: 封装服务器返回列表的参数
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class GetMerchantRevenueResponse {
	
	/** 服务器响应码 */
	public int code;


	/** 服务器返回消息 */
	public String msg;

	public double  revenue;

	@Override
	public String toString() {
		return "GetMerchantRevenueResponse{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", revenue='" + revenue + '\'' +
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


	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}

	public double getRevenue() {
		return revenue;
	}
}
