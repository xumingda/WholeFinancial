package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetLoginListResponse;
import com.ciba.wholefinancial.response.GetUserListResponse;
import com.google.gson.Gson;

public class GetLoginListProtocol extends BaseProtocol<GetLoginListResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetLoginListProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetLoginListResponse parseJson(String json) {
		GetLoginListResponse getUserListResponse = gson.fromJson(json, GetLoginListResponse.class);
		return getUserListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/business/getBusinessLoginList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
