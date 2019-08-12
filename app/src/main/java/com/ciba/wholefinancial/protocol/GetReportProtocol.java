package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetCodeResponse;
import com.ciba.wholefinancial.response.GetReportListResponse;
import com.google.gson.Gson;

public class GetReportProtocol extends BaseProtocol<GetReportListResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public GetReportProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetReportListResponse parseJson(String json) {
		GetReportListResponse getReportListResponse = gson.fromJson(json, GetReportListResponse.class);
		return getReportListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/common/reportList";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
