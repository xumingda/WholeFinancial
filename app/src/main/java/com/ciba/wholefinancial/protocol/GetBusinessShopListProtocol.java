package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetBusinessShopListResponse;
import com.ciba.wholefinancial.response.GetLevelListResponse;
import com.google.gson.Gson;

public class GetBusinessShopListProtocol extends BaseProtocol<GetBusinessShopListResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetBusinessShopListProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetBusinessShopListResponse parseJson(String json) {
		GetBusinessShopListResponse getUserListResponse = gson.fromJson(json, GetBusinessShopListResponse.class);
		return getUserListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/business/getBusinessShopList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
