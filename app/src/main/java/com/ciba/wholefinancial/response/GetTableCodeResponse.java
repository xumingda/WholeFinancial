package com.ciba.wholefinancial.response;


import com.ciba.wholefinancial.bean.ClockBean;
import com.ciba.wholefinancial.bean.TableCodeBean;

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
public class GetTableCodeResponse {
	
	/** 服务器响应码 */
	public int code;


	/** 服务器返回消息 */
	public String msg;


	public List<TableCodeBean>  dataList;

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

	public List<TableCodeBean> getDataList() {
		return dataList;
	}

	public void setDataList(List<TableCodeBean> dataList) {
		this.dataList = dataList;
	}

	@Override
	public String toString() {
		return "GetClocktListResponse{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", dataList=" + dataList +
				'}';
	}
}
