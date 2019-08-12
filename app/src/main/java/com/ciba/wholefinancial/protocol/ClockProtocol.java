package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.ClockResponse;
import com.ciba.wholefinancial.response.GetClocktListResponse;
import com.google.gson.Gson;

public class ClockProtocol extends BaseProtocol<ClockResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public ClockProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected ClockResponse parseJson(String json) {
		ClockResponse clockResponse = gson.fromJson(json, ClockResponse.class);
		return clockResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/salesman/clock";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
