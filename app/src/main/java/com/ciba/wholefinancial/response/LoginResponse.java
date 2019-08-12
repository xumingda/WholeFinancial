package com.ciba.wholefinancial.response;


import com.ciba.wholefinancial.bean.BusinessLoginObjectBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.SalesmanBean;

/**
 * @作者: 许明达
 * @创建时间: 2016-3-23下午15:43:20
 * @版权: 特速版权所有
 * @描述: 封装服务器返回列表的参数
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class LoginResponse {
	/** 服务器响应码 */
	public String code;
	/** 服务器返回消息 */
	public String msg;
	public String authorization;
	public MerchantBean businessObject;
	public SalesmanBean salesmanObject;
	public BusinessLoginObjectBean businessLoginObject;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public MerchantBean getBusinessObject() {
		return businessObject;
	}

	public void setBusinessObject(MerchantBean businessObject) {
		this.businessObject = businessObject;
	}

	public SalesmanBean getSalesmanObject() {
		return salesmanObject;
	}

	public void setSalesmanObject(SalesmanBean salesmanObject) {
		this.salesmanObject = salesmanObject;
	}

	public BusinessLoginObjectBean getBusinessLoginObject() {
		return businessLoginObject;
	}

	public void setBusinessLoginObject(BusinessLoginObjectBean businessLoginObject) {
		this.businessLoginObject = businessLoginObject;
	}

	@Override
	public String toString() {
		return "LoginResponse{" +
				"code='" + code + '\'' +
				", msg='" + msg + '\'' +
				", authorization='" + authorization + '\'' +
				", businessObject=" + businessObject +
				", salesmanObject=" + salesmanObject +
				", businessLoginObject=" + businessLoginObject +
				'}';
	}
}
