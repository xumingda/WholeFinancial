package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetMerchantInfoResponse;
import com.ciba.wholefinancial.response.GetMerchantRevenueResponse;
import com.google.gson.Gson;

public class GetMerchantRevenueProtocol extends BaseProtocol<GetMerchantRevenueResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetMerchantRevenueProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetMerchantRevenueResponse parseJson(String json) {
		GetMerchantRevenueResponse getMerchantRevenueResponse = gson.fromJson(json, GetMerchantRevenueResponse.class);
		return getMerchantRevenueResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/salesman/businessRevenue";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
