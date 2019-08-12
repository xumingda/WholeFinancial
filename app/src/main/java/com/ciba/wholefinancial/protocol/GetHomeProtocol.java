package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetGoodsListResponse;
import com.ciba.wholefinancial.response.GetHomeResponse;
import com.google.gson.Gson;

public class GetHomeProtocol extends BaseProtocol<GetHomeResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetHomeProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetHomeResponse parseJson(String json) {
		GetHomeResponse getGoodsListResponse = gson.fromJson(json, GetHomeResponse.class);
		return getGoodsListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/business/home";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
