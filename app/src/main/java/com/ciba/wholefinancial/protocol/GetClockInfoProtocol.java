package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetClocktInfoResponse;
import com.ciba.wholefinancial.response.GetClocktListResponse;
import com.google.gson.Gson;

public class GetClockInfoProtocol extends BaseProtocol<GetClocktInfoResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetClockInfoProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetClocktInfoResponse parseJson(String json) {
		GetClocktInfoResponse getClocktInfoResponse = gson.fromJson(json, GetClocktInfoResponse.class);
		return getClocktInfoResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/salesman/attendanceDesc";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
