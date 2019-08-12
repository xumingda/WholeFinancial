package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetMarketingListResponse;
import com.ciba.wholefinancial.response.GetMessageListResponse;
import com.google.gson.Gson;

public class GetMarketingListProtocol extends BaseProtocol<GetMarketingListResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetMarketingListProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetMarketingListResponse parseJson(String json) {
		GetMarketingListResponse getMessageListResponse = gson.fromJson(json, GetMarketingListResponse.class);
		return getMessageListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/business/getBusinessActivityList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
