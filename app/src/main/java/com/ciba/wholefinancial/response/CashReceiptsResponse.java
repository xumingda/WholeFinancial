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
public class CashReceiptsResponse {

	/**
	 * 服务器响应码
	 */
	public int code;


	/**
	 * 服务器返回消息
	 */
	public String msg;

	public CashData data;

	public class CashData {
		public String createTime;//	收款时间
		public String money;//	收款金额
		public String orderNo;//	订单号
		public String remark;//	备注
		public String shopName;//	店铺名称

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getMoney() {
			return money;
		}

		public void setMoney(String money) {
			this.money = money;
		}

		public String getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getShopName() {
			return shopName;
		}

		public void setShopName(String shopName) {
			this.shopName = shopName;
		}

		@Override
		public String toString() {
			return "CashData{" +
					"createTime='" + createTime + '\'' +
					", money='" + money + '\'' +
					", orderNo='" + orderNo + '\'' +
					", remark='" + remark + '\'' +
					", shopName='" + shopName + '\'' +
					'}';
		}
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public CashData getData() {
		return data;
	}

	public void setData(CashData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "CashReceiptsResponse{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", data=" + data +
				'}';
	}
}
