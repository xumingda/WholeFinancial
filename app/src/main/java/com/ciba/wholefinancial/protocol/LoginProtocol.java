package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.LoginResponse;
import com.google.gson.Gson;

public class LoginProtocol extends BaseProtocol<LoginResponse> {
	private Gson gson;

	public LoginProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected LoginResponse parseJson(String json) {
		LoginResponse loginresponse = gson.fromJson(json, LoginResponse.class);
		return loginresponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/common/appLogin";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
