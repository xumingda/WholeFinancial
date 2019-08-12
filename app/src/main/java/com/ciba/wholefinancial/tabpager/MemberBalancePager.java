package com.ciba.wholefinancial.tabpager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.activity.ConsumptionListActivity;
import com.ciba.wholefinancial.activity.MemberInfoActivity;
import com.ciba.wholefinancial.adapter.BalanceAdapter;
import com.ciba.wholefinancial.adapter.MemberAdapter;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.BalanceBean;
import com.ciba.wholefinancial.bean.MemberBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.protocol.GetBalanceListProtocol;
import com.ciba.wholefinancial.protocol.GetMemberProtocol;
import com.ciba.wholefinancial.response.GetBalanceListResponse;
import com.ciba.wholefinancial.response.GetMemberResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @作者: 许明达
 * @创建时间: 2016-4-27下午5:24:30
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class MemberBalancePager extends ViewTabBasePager implements
		PullToRefreshBase.OnRefreshListener{




    private Dialog loadingDialog;
    private MerchantBean merchantBean;
    private PullToRefreshListView lv_balance;
	private View v_empty;
	private TextView tv_empty;
	private GetBalanceListResponse getBalanceListResponse;
	private List<BalanceBean> balanceBeanList;
	private int pageNo = 1;
	//判断是否刷新
	private boolean isRefresh = false;
	private BalanceAdapter balanceAdapter;
	private String userId;
	public MemberBalancePager(Context context,String userId) {
		super(context);
		this.userId=userId;
	}






    @Override
	protected View initView() {
		View view = View.inflate(mContext,
				R.layout.balance_pager, null);
		v_empty=(View)view.findViewById(R.id.v_empty);
        tv_empty=(TextView)view.findViewById(R.id.tv_empty);
		lv_balance=(PullToRefreshListView)view.findViewById(R.id.lv_balance);
		return view;
	}

	@Override
	public void initData() {
		balanceBeanList=new ArrayList<>();
		merchantBean= SPUtils.getBeanFromSp(mContext,"businessObject","MerchantBean");
        loadingDialog = DialogUtils.createLoadDialog(mContext, false);
		lv_balance.setMode(PullToRefreshBase.Mode.BOTH);
		lv_balance.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
		lv_balance.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
		lv_balance.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以刷新");
		lv_balance.setOnRefreshListener(this);
		lv_balance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

				if(balanceBeanList.get(i-1).getType()==1){
					Intent intent=new Intent(mContext, ConsumptionListActivity.class);
					intent.putExtra("orderId",balanceBeanList.get(i-1).getOrderId());
					UIUtils.startActivityNextAnim(intent);
				}

			}
		});
		getBalanceList();
	}
	private void setData() {
		balanceBeanList.addAll(getBalanceListResponse.getDataList());
		if (balanceAdapter == null) {
			balanceAdapter = new BalanceAdapter( mContext, balanceBeanList);
			lv_balance.setAdapter(balanceAdapter);
		} else {
			balanceAdapter.setDate(balanceBeanList);
			balanceAdapter.notifyDataSetChanged();
		}

	}
    //获取数据
	public void getBalanceList() {
		loadingDialog.show();
		GetBalanceListProtocol getBalanceListProtocol = new GetBalanceListProtocol();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userId",  userId);
		params.put("businessId",  String.valueOf(merchantBean.getId()));
		params.put("pageNo", String.valueOf(pageNo));
		params.put("pageSize", "100");
		String url = getBalanceListProtocol.getApiFun();

		MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
			@Override
			public void dealWithJson(String address, String json) {

				Gson gson = new Gson();
				getBalanceListResponse = gson.fromJson(json, GetBalanceListResponse.class);
				LogUtils.e("getBalanceListResponse:" + getBalanceListResponse.toString());
                loadingDialog.dismiss();
				if (getBalanceListResponse.code == 0) {
					if(getBalanceListResponse.getDataList().size()>0){

						if (getBalanceListResponse.getDataList().size() > 0) {
							if (pageNo == 1) {
								balanceBeanList.clear();
							}
							setData();
						} else {
							DialogUtils.showAlertDialog(mContext, "没有更多数据了！");
						}
					}else {
						v_empty.setVisibility(View.VISIBLE);
                        tv_empty.setVisibility(View.VISIBLE);
						lv_balance.setVisibility(View.GONE);
					}

				} else {

					DialogUtils.showAlertDialog(mContext,
							getBalanceListResponse.msg);
				}

				if (isRefresh) {
					isRefresh = false;
					lv_balance.onRefreshComplete();
				}
			}

		@Override
		public void dealWithError(String address, String error) {
			loadingDialog.dismiss();
			DialogUtils.showAlertDialog(mContext, error);
			if (isRefresh) {
				isRefresh = false;
				lv_balance.onRefreshComplete();
			}
		}

		@Override
		public void dealTokenOverdue() {

		}
	});
}


	@Override
	public void onRefresh(PullToRefreshBase refreshView) {
		if (!isRefresh) {
			isRefresh = true;
			if (lv_balance.isHeaderShown()) {
				pageNo = 1;
				balanceBeanList.clear();
				getBalanceList();
			} else if (lv_balance.isFooterShown()) {
				pageNo++;
				getBalanceList();
			}
		}
	}
}