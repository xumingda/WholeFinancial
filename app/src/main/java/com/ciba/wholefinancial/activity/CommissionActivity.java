package com.ciba.wholefinancial.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.adapter.CommissionAdapter;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.CommissionBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.callback.OnSetViewCallBack;
import com.ciba.wholefinancial.protocol.GetCommissionListProtocol;
import com.ciba.wholefinancial.protocol.GetMerchantProtocol;
import com.ciba.wholefinancial.response.GetCommissionListResponse;
import com.ciba.wholefinancial.response.GetMerchantListResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.ClockOutPopuwindow;
import com.ciba.wholefinancial.weiget.ClockPopuwindow;
import com.ciba.wholefinancial.weiget.SpinnerPopuwindow;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//收益明细
public class CommissionActivity extends BaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener  {

    private LayoutInflater mInflater;
    private View rootView;
    private String url;

    private Dialog loadingDialog;
    private List<CommissionBean> commissionBeanList;
    private int pageNo = 1;
    //判断是否刷新
    private boolean isRefresh = false;
    private SalesmanBean salesmanBean;
    private GetCommissionListResponse getCommissionListResponse;
    private CommissionAdapter commissionAdapter;
    private PullToRefreshListView lv_commission;
    private View v_empty,view_back;
    private TextView tv_empty;
    private TextView tv_commission;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_commission, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        commissionBeanList=new ArrayList<>();
        tv_empty=(TextView)rootView.findViewById(R.id.tv_empty);
        tv_commission=(TextView)rootView.findViewById(R.id.tv_commission);
        v_empty=(View)rootView.findViewById(R.id.v_empty);
        view_back=(View)rootView.findViewById(R.id.view_back);
        lv_commission=(PullToRefreshListView)rootView.findViewById(R.id.lv_commission);
        loadingDialog = DialogUtils.createLoadDialog(CommissionActivity.this, false);
        salesmanBean= SPUtils.getBeanFromSp(CommissionActivity.this,"salesmanObject","SalesmanBean");
        view_back.setOnClickListener(this);
        getCommissionList();
    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.rl_business:{
                Intent intent=new Intent(this,MyMerchantActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
        }
    }


    private void setData() {
        commissionBeanList.addAll(getCommissionListResponse.getDataList());
        if (commissionAdapter == null) {
            commissionAdapter = new CommissionAdapter( this, commissionBeanList);
            lv_commission.setAdapter(commissionAdapter);
        } else {
            commissionAdapter.notifyDataSetChanged();
        }
    }

    private void getCommissionList() {
        loadingDialog.show();
        GetCommissionListProtocol getCommissionListProtocol=new GetCommissionListProtocol();
        url = getCommissionListProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
//        params.put("status", "1");
        params.put("salesmanId",  String.valueOf(salesmanBean.getId()));
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", "100");
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                getCommissionListResponse = gson.fromJson(json, GetCommissionListResponse.class);
                LogUtils.e("getCommissionListResponse:" + getCommissionListResponse.toString());
                if (getCommissionListResponse.getCode() == 0) {
                    DecimalFormat df = new java.text.DecimalFormat("#.00");
                    if(getCommissionListResponse.getCommission()>0){
                        tv_commission.setText("￥"+df.format(getCommissionListResponse.getCommission()));
                    }
                    else{
                        tv_commission.setText("￥0.00");
                    }
                    if (getCommissionListResponse.getDataList().size() > 0) {
                        if (pageNo == 1) {
                            commissionBeanList.clear();
//                            SharedPrefrenceUtils.setString(mContext, url + "3", json);
                        }
                        setData();
                    } else {
                        if(pageNo==1){
                            v_empty.setVisibility(View.VISIBLE);
                            tv_empty.setVisibility(View.VISIBLE);
                            lv_commission.setVisibility(View.GONE);
                        }else{
                            DialogUtils.showAlertDialog(CommissionActivity.this, "没有更多数据了！");
                        }

                    }

                } else {
                    DialogUtils.showAlertDialog(CommissionActivity.this, getCommissionListResponse.getMsg());
                }
                if (isRefresh) {
                    LogUtils.e("结束");
                    isRefresh = false;
                    lv_commission.onRefreshComplete();
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(CommissionActivity.this, error);
                if (isRefresh) {
                    isRefresh = false;
                    lv_commission.onRefreshComplete();
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
            if (lv_commission.isHeaderShown()) {
                pageNo = 1;
                commissionBeanList.clear();
                getCommissionList();
            } else if (lv_commission.isFooterShown()) {
                pageNo++;
                getCommissionList();
            }
        }
    }
}
