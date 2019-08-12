package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.ClockCountResponse;
import com.ciba.wholefinancial.response.ClockResponse;
import com.google.gson.Gson;

public class ClockCountProtocol extends BaseProtocol<ClockCountResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public ClockCountProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected ClockCountResponse parseJson(String json) {
		ClockCountResponse clockCountResponse = gson.fromJson(json, ClockCountResponse.class);
		return clockCountResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/salesman/clockCount";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
