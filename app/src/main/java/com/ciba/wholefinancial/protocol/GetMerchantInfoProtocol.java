package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetMerchantInfoResponse;
import com.ciba.wholefinancial.response.GetMerchantListResponse;
import com.google.gson.Gson;

public class GetMerchantInfoProtocol extends BaseProtocol<GetMerchantInfoResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetMerchantInfoProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetMerchantInfoResponse parseJson(String json) {
		GetMerchantInfoResponse getMerchantInfoResponse = gson.fromJson(json, GetMerchantInfoResponse.class);
		return getMerchantInfoResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/salesman/businessDesc";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
