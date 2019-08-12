package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.UpdateMarketingResponse;
import com.google.gson.Gson;

public class UpdateOnlyMemberLevelProtocol extends BaseProtocol<UpdateMarketingResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public UpdateOnlyMemberLevelProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected UpdateMarketingResponse parseJson(String json) {
		UpdateMarketingResponse updateMarketingResponse = gson.fromJson(json, UpdateMarketingResponse.class);
		return updateMarketingResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/business/updateBusinessUserLevel";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
