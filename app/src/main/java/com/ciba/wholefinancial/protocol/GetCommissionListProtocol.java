package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetCommissionListResponse;
import com.ciba.wholefinancial.response.GetMerchantListResponse;
import com.google.gson.Gson;

public class GetCommissionListProtocol extends BaseProtocol<GetCommissionListResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetCommissionListProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetCommissionListResponse parseJson(String json) {
		GetCommissionListResponse getCommissionListResponse = gson.fromJson(json, GetCommissionListResponse.class);
		return getCommissionListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/salesman/getSalesmanCommissionList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
