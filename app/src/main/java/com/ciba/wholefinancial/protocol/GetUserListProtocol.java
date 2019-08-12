package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetMessageListResponse;
import com.ciba.wholefinancial.response.GetUserListResponse;
import com.google.gson.Gson;

public class GetUserListProtocol extends BaseProtocol<GetUserListResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetUserListProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetUserListResponse parseJson(String json) {
		GetUserListResponse getUserListResponse = gson.fromJson(json, GetUserListResponse.class);
		return getUserListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/business/getBusinessUserList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
