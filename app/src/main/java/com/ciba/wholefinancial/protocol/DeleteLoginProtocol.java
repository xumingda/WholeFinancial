package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.AddOrUpdateLoginResponse;
import com.ciba.wholefinancial.response.DeleteLoginResponse;
import com.google.gson.Gson;

public class DeleteLoginProtocol extends BaseProtocol<DeleteLoginResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public DeleteLoginProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected DeleteLoginResponse parseJson(String json) {
		DeleteLoginResponse clockCountResponse = gson.fromJson(json, DeleteLoginResponse.class);
		return clockCountResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/business/deleteBusinessLogin";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
