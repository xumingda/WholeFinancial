package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetCodeResponse;
import com.ciba.wholefinancial.response.GetMerchantListResponse;
import com.google.gson.Gson;

public class GetMerchantProtocol extends BaseProtocol<GetMerchantListResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetMerchantProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetMerchantListResponse parseJson(String json) {
		GetMerchantListResponse getMerchantListResponse = gson.fromJson(json, GetMerchantListResponse.class);
		return getMerchantListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/salesman/getBusinessList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
