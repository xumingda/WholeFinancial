package com.ciba.wholefinancial.request;

import java.util.HashMap;

/**
 * @作者: 许明达
 * @创建时间: 2016-4-6 上午9:44:34
 * @描述: 封装请求服务器获取注册的参数
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class GetCodeRequest {
	
	public static HashMap<String, String> map = new HashMap<String, String>();

	public GetCodeRequest() {

		/** 21509或者21507 */
		map.put("template_id","");
		/**
		 * platform
		 */
		map.put("platform","");
		/**随机数*/
		map.put("template_value","");
		/**用des算法对各个platform+一位随机数+phone_number +’-’+ template_id*/
		map.put("sign","");


	}

	
}
