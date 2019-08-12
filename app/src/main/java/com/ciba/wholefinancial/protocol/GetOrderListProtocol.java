package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetMessageListResponse;
import com.ciba.wholefinancial.response.GetOrderListResponse;
import com.google.gson.Gson;

public class GetOrderListProtocol extends BaseProtocol<GetOrderListResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetOrderListProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetOrderListResponse parseJson(String json) {
		GetOrderListResponse getOrderListResponse = gson.fromJson(json, GetOrderListResponse.class);
		return getOrderListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/business/getOrderList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
