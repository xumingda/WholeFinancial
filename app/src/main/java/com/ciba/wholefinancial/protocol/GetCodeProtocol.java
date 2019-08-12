package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetCodeResponse;
import com.google.gson.Gson;

public class GetCodeProtocol extends BaseProtocol<GetCodeResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetCodeProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetCodeResponse parseJson(String json) {
		GetCodeResponse getcoderesponse = gson.fromJson(json, GetCodeResponse.class);
		return getcoderesponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/common/sendSms";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
