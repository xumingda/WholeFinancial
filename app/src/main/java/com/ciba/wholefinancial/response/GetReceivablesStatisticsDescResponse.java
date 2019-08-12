package com.ciba.wholefinancial.response;


import com.ciba.wholefinancial.bean.BalanceBean;

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
public class GetReceivablesStatisticsDescResponse {
	
	/** 服务器响应码 */
	public int code;


	/** 服务器返回消息 */
	public String msg;

	public Data data;

	public class Data{
		public String totalMoney;//	收款总额
		public String startDate;//	搜索开始时间
		public String endDate;//	搜索结束时间

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
					'}';
		}
	}

	public List<ReceivablesStatisticsDescBean>  dataList;

	public class ReceivablesStatisticsDescBean{
		public String id;//	序号
		public String commissionRate;//	佣金利率
		public String createTime;//	收益时间
		public String businessName;//	商家名称
		public String shopName;//	商家店铺名称
		public String commission;//	佣金
		public String orderMoney;//	订单金额

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getCommissionRate() {
			return commissionRate;
		}

		public void setCommissionRate(String commissionRate) {
			this.commissionRate = commissionRate;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getBusinessName() {
			return businessName;
		}

		public void setBusinessName(String businessName) {
			this.businessName = businessName;
		}

		public String getShopName() {
			return shopName;
		}

		public void setShopName(String shopName) {
			this.shopName = shopName;
		}

		public String getCommission() {
			return commission;
		}

		public void setCommission(String commission) {
			this.commission = commission;
		}

		public String getOrderMoney() {
			return orderMoney;
		}

		public void setOrderMoney(String orderMoney) {
			this.orderMoney = orderMoney;
		}

		@Override
		public String toString() {
			return "ReceivablesStatisticsDescBean{" +
					"id='" + id + '\'' +
					", commissionRate='" + commissionRate + '\'' +
					", createTime='" + createTime + '\'' +
					", businessName='" + businessName + '\'' +
					", shopName='" + shopName + '\'' +
					", commission='" + commission + '\'' +
					", orderMoney='" + orderMoney + '\'' +
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

	public List<ReceivablesStatisticsDescBean> getDataList() {
		return dataList;
	}

	public void setDataList(List<ReceivablesStatisticsDescBean> dataList) {
		this.dataList = dataList;
	}
}
