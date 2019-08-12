package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetCommissionListResponse;
import com.ciba.wholefinancial.response.GetMessageListResponse;
import com.google.gson.Gson;

public class GetMessageListProtocol extends BaseProtocol<GetMessageListResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetMessageListProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetMessageListResponse parseJson(String json) {
		GetMessageListResponse getMessageListResponse = gson.fromJson(json, GetMessageListResponse.class);
		return getMessageListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/common/msgList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
