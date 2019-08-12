package com.ciba.wholefinancial.tabpager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.activity.ConsumptionListActivity;
import com.ciba.wholefinancial.activity.EditMarketActivity;
import com.ciba.wholefinancial.adapter.BalanceAdapter;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.BalanceBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.protocol.GetBalanceListProtocol;
import com.ciba.wholefinancial.protocol.RechargeProtocol;
import com.ciba.wholefinancial.protocol.UpdateMarketingProtocol;
import com.ciba.wholefinancial.response.GetBalanceListResponse;
import com.ciba.wholefinancial.response.UpdateMarketingResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @作者: 许明达
 * @创建时间: 2016-4-27下午5:24:30
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class MemberRechargePager extends ViewTabBasePager {




    private Dialog loadingDialog;
    private MerchantBean merchantBean;
	private GetBalanceListResponse getBalanceListResponse;
	private List<BalanceBean> balanceBeanList;
	private int pageNo = 1;
	//判断是否刷新
	private boolean isRefresh = false;
	private BalanceAdapter balanceAdapter;
	private String userId;
	private String money;
	private EditText et_money;
	private Button btn_commit;
	private RelativeLayout rl;
	public MemberRechargePager(Context context, String userId) {
		super(context);
		this.userId=userId;
	}






    @Override
	protected View initView() {
		View view = View.inflate(mContext,
				R.layout.recharge_pager, null);
		et_money=(EditText)view.findViewById(R.id.et_money);
		btn_commit=(Button)view.findViewById(R.id.btn_commit);
		rl=(RelativeLayout)view.findViewById(R.id.rl);
		return view;
	}

	@Override
	public void initData() {
		balanceBeanList=new ArrayList<>();
		merchantBean= SPUtils.getBeanFromSp(mContext,"businessObject","MerchantBean");
        loadingDialog = DialogUtils.createLoadDialog(mContext, false);
		et_money.addTextChangedListener(new TextWatcher() {
			private CharSequence temp = null;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(temp)) {
					btn_commit.setBackground(UIUtils.getDrawable(R.drawable.shape_gray));
					btn_commit.setTextColor(UIUtils.getColor(R.color.text_color_gray));
				} else {
					btn_commit.setBackground(UIUtils.getDrawable(R.drawable.shape_update_btn));
					btn_commit.setTextColor(UIUtils.getColor(R.color.tab_background));
				}

			}
		});
		btn_commit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				money=et_money.getText().toString();
				if(money.length()>0){
					recharge();
				}else{
					DialogUtils.showAlertDialog(mContext, "请输入充值金额！");
				}
			}
		});
	}
	private void setData() {


	}
	private void recharge() {
		loadingDialog.show();
		RechargeProtocol rechargeProtocol=new RechargeProtocol();
		String url = rechargeProtocol.getApiFun();
		Map<String, String> params = new HashMap<String, String>();
		params.put("businessId",  String.valueOf(merchantBean.getId()));
		params.put("userId", userId);
		params.put("money", money);
		MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

			@Override
			public void dealWithJson(String address, String json) {

				Gson gson = new Gson();
				loadingDialog.dismiss();
				UpdateMarketingResponse updateMarketingResponse = gson.fromJson(json, UpdateMarketingResponse.class);
				LogUtils.e("updateMarketingResponse:" + updateMarketingResponse.toString());
				if (updateMarketingResponse.getCode() == 0) {
					SuccessPopuwindow successPopuwindow=new SuccessPopuwindow(UIUtils.getActivity(),null,"充值成功");
					successPopuwindow.showPopupWindow(rl);
				} else {
					DialogUtils.showAlertDialog(mContext, updateMarketingResponse.getMsg());
				}

			}

			@Override
			public void dealWithError(String address, String error) {
				loadingDialog.dismiss();
				DialogUtils.showAlertDialog(mContext, error);

			}

			@Override
			public void dealTokenOverdue() {

			}
		});
	}


}