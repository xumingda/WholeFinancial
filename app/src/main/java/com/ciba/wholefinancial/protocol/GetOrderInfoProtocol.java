package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetOrderInfoResponse;
import com.ciba.wholefinancial.response.GetOrderListResponse;
import com.google.gson.Gson;

public class GetOrderInfoProtocol extends BaseProtocol<GetOrderInfoResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetOrderInfoProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetOrderInfoResponse parseJson(String json) {
		GetOrderInfoResponse getOrderListResponse = gson.fromJson(json, GetOrderInfoResponse.class);
		return getOrderListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/business/getOrderInfo";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
