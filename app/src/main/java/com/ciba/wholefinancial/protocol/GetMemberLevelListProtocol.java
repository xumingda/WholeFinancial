package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetLevelListResponse;
import com.ciba.wholefinancial.response.GetMemberLevelListResponse;
import com.google.gson.Gson;

public class GetMemberLevelListProtocol extends BaseProtocol<GetMemberLevelListResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetMemberLevelListProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetMemberLevelListResponse parseJson(String json) {
		GetMemberLevelListResponse getUserListResponse = gson.fromJson(json, GetMemberLevelListResponse.class);
		return getUserListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/business/getBusinessLevelList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
