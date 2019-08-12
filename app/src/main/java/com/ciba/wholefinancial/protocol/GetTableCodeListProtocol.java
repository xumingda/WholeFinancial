package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetOrderListResponse;
import com.ciba.wholefinancial.response.GetTableCodeResponse;
import com.google.gson.Gson;

public class GetTableCodeListProtocol extends BaseProtocol<GetTableCodeResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetTableCodeListProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetTableCodeResponse parseJson(String json) {
		GetTableCodeResponse getOrderListResponse = gson.fromJson(json, GetTableCodeResponse.class);
		return getOrderListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/business/getBusinessShopCodeList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
