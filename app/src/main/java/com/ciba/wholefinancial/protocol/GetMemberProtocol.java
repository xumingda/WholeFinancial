package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetMemberResponse;
import com.ciba.wholefinancial.response.GetReportListResponse;
import com.google.gson.Gson;

public class GetMemberProtocol extends BaseProtocol<GetMemberResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetMemberProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetMemberResponse parseJson(String json) {
		GetMemberResponse getReportListResponse = gson.fromJson(json, GetMemberResponse.class);
		return getReportListResponse;
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
