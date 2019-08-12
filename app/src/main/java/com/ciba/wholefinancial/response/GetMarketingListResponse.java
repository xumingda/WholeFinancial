package com.ciba.wholefinancial.response;


import com.ciba.wholefinancial.bean.MarketingBean;
import com.ciba.wholefinancial.bean.MerchantBean;

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
public class GetMarketingListResponse {
	
	/** 服务器响应码 */
	public int code;


	/** 服务器返回消息 */
	public String msg;

	public List<MarketingBean>  dataList;

	@Override
	public String toString() {
		return "GetMerchantListResponse{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", dataList=" + dataList +
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

	public List<MarketingBean> getDataList() {
		return dataList;
	}

	public void setDataList(List<MarketingBean> dataList) {
		this.dataList = dataList;
	}
}
