package com.ciba.wholefinancial.tabpager;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.activity.LoginActivity;
import com.ciba.wholefinancial.activity.ReportInfoActivity;
import com.ciba.wholefinancial.adapter.AddImageAdapter;
import com.ciba.wholefinancial.adapter.ReportAdapter;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.protocol.GetCodeProtocol;
import com.ciba.wholefinancial.protocol.GetReportProtocol;
import com.ciba.wholefinancial.protocol.SubmitReportProtocol;
import com.ciba.wholefinancial.request.GetCodeRequest;
import com.ciba.wholefinancial.response.GetCodeResponse;
import com.ciba.wholefinancial.response.GetReportListResponse;
import com.ciba.wholefinancial.response.SubmitReportResponse;
import com.ciba.wholefinancial.util.Constants;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.SharedPrefrenceUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * @作者: 许明达
 * @创建时间: 2016-4-27下午5:24:30
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class HistoryReportPager extends ViewTabBasePager {




    private Dialog loadingDialog;
    private SalesmanBean salesmanBean;
    private MerchantBean merchantBean;
    private ListView lv_history_report;
	private View v_empty;
	private TextView tv_empty;
	private GetReportListResponse getReportListResponse;

	public HistoryReportPager(Context context) {
		super(context);
	}






    @Override
	protected View initView() {
		View view = View.inflate(mContext,
				R.layout.history_report_pager, null);
		v_empty=(View)view.findViewById(R.id.v_empty);
        tv_empty=(TextView)view.findViewById(R.id.tv_empty);
		lv_history_report=(ListView)view.findViewById(R.id.lv_history_report);
        initData();
		return view;
	}

	@Override
	public void initData() {
        salesmanBean= SPUtils.getBeanFromSp(mContext,"salesmanObject","SalesmanBean");
		merchantBean= SPUtils.getBeanFromSp(mContext,"businessObject","MerchantBean");
        loadingDialog = DialogUtils.createLoadDialog(mContext, false);
		getReportList();
	}

    //获取数据
	public void getReportList() {
		LogUtils.e("getReportListResponse:大大大" );
		loadingDialog.show();
		GetReportProtocol getReportProtocol = new GetReportProtocol();
		HashMap<String, String> params = new HashMap<String, String>();
		if(salesmanBean!=null){
			params.put("type", "0");
			params.put("salesmanId",  String.valueOf(salesmanBean.getId()));
		}else{
			params.put("type", "1");
			params.put("businessId",  String.valueOf(merchantBean.getId()));
		}

		String url = getReportProtocol.getApiFun();

		MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
			@Override
			public void dealWithJson(String address, String json) {

				Gson gson = new Gson();
				getReportListResponse = gson.fromJson(json, GetReportListResponse.class);
				LogUtils.e("getReportListResponse:" + getReportListResponse.toString());
                loadingDialog.dismiss();
				if (getReportListResponse.code == 0) {
					if(getReportListResponse.getDataList().size()>0){
						ReportAdapter reportAdapter=new ReportAdapter(mContext,getReportListResponse.getDataList());
						lv_history_report.setAdapter(reportAdapter);
						lv_history_report.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
								Intent intent=new Intent(mContext,ReportInfoActivity.class);
								intent.putExtra("reportList",getReportListResponse.getDataList().get(i));
								UIUtils.startActivityNextAnim(intent);
							}
						});
					}else {
						v_empty.setVisibility(View.VISIBLE);
                        tv_empty.setVisibility(View.VISIBLE);
						lv_history_report.setVisibility(View.GONE);
					}

				} else {

					DialogUtils.showAlertDialog(mContext,
							getReportListResponse.msg);
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