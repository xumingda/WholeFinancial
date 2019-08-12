package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.GetCodeResponse;
import com.ciba.wholefinancial.response.SubmitReportResponse;
import com.google.gson.Gson;

public class SubmitReportProtocol extends BaseProtocol<SubmitReportResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public SubmitReportProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected SubmitReportResponse parseJson(String json) {
		SubmitReportResponse submitReportResponse = gson.fromJson(json, SubmitReportResponse.class);
		return submitReportResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/common/submitReport";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
