package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetGoodsListResponse;
import com.ciba.wholefinancial.response.GetLoginListResponse;
import com.google.gson.Gson;

public class GetGoodsListProtocol extends BaseProtocol<GetGoodsListResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetGoodsListProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetGoodsListResponse parseJson(String json) {
		GetGoodsListResponse getGoodsListResponse = gson.fromJson(json, GetGoodsListResponse.class);
		return getGoodsListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/business/getGoodsList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
