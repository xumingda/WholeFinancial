package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetBalanceListResponse;
import com.ciba.wholefinancial.response.GetGoodsListResponse;
import com.google.gson.Gson;

public class GetBalanceListProtocol extends BaseProtocol<GetBalanceListResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetBalanceListProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetBalanceListResponse parseJson(String json) {
		GetBalanceListResponse getGoodsListResponse = gson.fromJson(json, GetBalanceListResponse.class);
		return getGoodsListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/business/getBusinessUserBalanceList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
