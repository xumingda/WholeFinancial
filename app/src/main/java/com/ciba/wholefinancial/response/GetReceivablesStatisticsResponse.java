package com.ciba.wholefinancial.response;


import java.util.List;

/**
 * @作者: 许明达
 * @创建时间: 2016-4-6上午09:43:20
 * @版权: 特速版权所有
 * @描述: 封装服务器返回列表的参数
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class GetReceivablesStatisticsResponse {
	
	/** 服务器响应码 */
	public int code;


	/** 服务器返回消息 */
	public String msg;

	public Data data;
	public class Data{
		public String totalMoney;//	收款总额
		public String startDate;//	搜索开始时间
		public String endDate;//	搜索结束时间
		public String totalCount;//	总多少笔收款

		public String getTotalCount() {
			return totalCount;
		}

		public void setTotalCount(String totalCount) {
			this.totalCount = totalCount;
		}

		public String getTotalMoney() {
			return totalMoney;
		}

		public void setTotalMoney(String totalMoney) {
			this.totalMoney = totalMoney;
		}

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		@Override
		public String toString() {
			return "Data{" +
					"totalMoney='" + totalMoney + '\'' +
					", startDate='" + startDate + '\'' +
					", endDate='" + endDate + '\'' +
					", totalCount='" + totalCount + '\'' +
					'}';
		}
	}

	public List<ReceivablesStatisticsBean>  dataList;

	public class ReceivablesStatisticsBean{
		public String count;//	多少笔收款
		public String money;//	收款金额
		public int payType;//	0现金收款，1余额收款，2微信收款，3支付宝收款，4线上收款


		public String getCount() {
			return count;
		}

		public void setCount(String count) {
			this.count = count;
		}

		public String getMoney() {
			return money;
		}

		public void setMoney(String money) {
			this.money = money;
		}

		public int getPayType() {
			return payType;
		}

		public void setPayType(int payType) {
			this.payType = payType;
		}

		@Override
		public String toString() {
			return "ReceivablesStatisticsBean{" +
					"count='" + count + '\'' +
					", money='" + money + '\'' +
					", payType=" + payType +
					'}';
		}
	}

	@Override
	public String toString() {
		return "GetReceivablesStatisticsDescResponse{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				", data=" + data +
				", dataList=" + dataList +
				'}';
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

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public List<ReceivablesStatisticsBean> getDataList() {
		return dataList;
	}

	public void setDataList(List<ReceivablesStatisticsBean> dataList) {
		this.dataList = dataList;
	}
}
