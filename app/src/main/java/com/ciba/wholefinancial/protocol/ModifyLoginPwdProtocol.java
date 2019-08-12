package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.ModifyLoginPwdResponse;
import com.google.gson.Gson;


public class ModifyLoginPwdProtocol extends BaseProtocol<ModifyLoginPwdResponse> {
	private Gson gson;

	public ModifyLoginPwdProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected ModifyLoginPwdResponse parseJson(String json) {
		ModifyLoginPwdResponse modifyLoginPwdResponse = gson.fromJson(json, ModifyLoginPwdResponse.class);
		return modifyLoginPwdResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/common/updatePassword";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
