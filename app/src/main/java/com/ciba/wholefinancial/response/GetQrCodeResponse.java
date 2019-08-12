package com.ciba.wholefinancial.response;


/**
 * @作者: 许明达
 * @创建时间: 2016-4-6上午09:43:20
 * @版权: 特速版权所有
 * @描述: 封装服务器返回列表的参数
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class GetQrCodeResponse {
	
	/** 服务器响应码 */
	public int code;
	/**验证码id*/
	public CodeData data;
//	public class RegisterData{
//		/** 用户token */
//		public String sid;
//	}
	/** 服务器返回消息 */
	public String msg;

	public class CodeData{
		public String qrUrl;

		@Override
		public String toString() {
			return "CodeData{" +
					"qrUrl='" + qrUrl + '\'' +
					'}';
		}
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public CodeData getData() {
		return data;
	}

	public void setData(CodeData data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "GetQrCodeResponse{" +
				"code=" + code +
				", data=" + data +
				", msg='" + msg + '\'' +
				'}';
	}
}
