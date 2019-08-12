package com.ciba.wholefinancial.response;


import com.ciba.wholefinancial.bean.ClassBean;
import com.ciba.wholefinancial.bean.OrderBean;
import com.ciba.wholefinancial.bean.OrderGoodsBean;

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
public class GetOrderInfoResponse {
	
	/** 服务器响应码 */
	public int code;


	/** 服务器返回消息 */
	public String msg;

	public OrderBean order;
	public List<OrderGoodsBean>  orderGoodsList;

	@Override
	public String toString() {
		return "GetOrderInfoResponse{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", order=" + order +
				", orderGoodsList=" + orderGoodsList +
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

	public OrderBean getOrder() {
		return order;
	}

	public void setOrder(OrderBean order) {
		this.order = order;
	}

	public List<OrderGoodsBean> getOrderGoodsList() {
		return orderGoodsList;
	}

	public void setOrderGoodsList(List<OrderGoodsBean> orderGoodsList) {
		this.orderGoodsList = orderGoodsList;
	}
}
