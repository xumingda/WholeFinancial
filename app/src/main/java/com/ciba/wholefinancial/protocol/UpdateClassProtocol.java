package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.AddOrUpdateLoginResponse;
import com.google.gson.Gson;

public class UpdateClassProtocol extends BaseProtocol<AddOrUpdateLoginResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public UpdateClassProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected AddOrUpdateLoginResponse parseJson(String json) {
		AddOrUpdateLoginResponse clockCountResponse = gson.fromJson(json, AddOrUpdateLoginResponse.class);
		return clockCountResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/business/updateClass";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
