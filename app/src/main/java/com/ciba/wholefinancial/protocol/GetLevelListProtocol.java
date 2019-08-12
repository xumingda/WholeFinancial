package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetLevelListResponse;
import com.ciba.wholefinancial.response.GetLoginListResponse;
import com.google.gson.Gson;

public class GetLevelListProtocol extends BaseProtocol<GetLevelListResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetLevelListProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetLevelListResponse parseJson(String json) {
		GetLevelListResponse getUserListResponse = gson.fromJson(json, GetLevelListResponse.class);
		return getUserListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/business/getUserLevelList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
