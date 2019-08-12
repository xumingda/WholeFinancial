package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetClocktListResponse;
import com.ciba.wholefinancial.response.GetUserHomeResponse;
import com.google.gson.Gson;

public class GetUserHomeProtocol extends BaseProtocol<GetUserHomeResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetUserHomeProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetUserHomeResponse parseJson(String json) {
		GetUserHomeResponse getClocktListResponse = gson.fromJson(json, GetUserHomeResponse.class);
		return getClocktListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/business/businessUserHome";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
