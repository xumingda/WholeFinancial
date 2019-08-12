package com.ciba.wholefinancial.protocol;

import com.ciba.wholefinancial.base.BaseProtocol;
import com.ciba.wholefinancial.response.SubmitBusinessInfoResponse;
import com.google.gson.Gson;

public class AddGoodsProtocol extends BaseProtocol<SubmitBusinessInfoResponse> {
	protected static final String TAG = "DiscoveryByProtocol";
	private Gson gson;

	public AddGoodsProtocol() {
		gson = new Gson();
	}

	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected SubmitBusinessInfoResponse parseJson(String json) {
		SubmitBusinessInfoResponse submitBusinessInfoResponse = gson.fromJson(json, SubmitBusinessInfoResponse.class);
		return submitBusinessInfoResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "/business/addGoods";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
