package com.ciba.wholefinancial.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.protocol.GetOrderInfoProtocol;
import com.ciba.wholefinancial.response.GetOrderInfoResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SharedPrefrenceUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


public class ConsumptionListActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;

    private ImageView iv_top_back;
    private int orderId;
    private String url;

    private Dialog loadingDialog;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_consumption, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        loadingDialog = DialogUtils.createLoadDialog(ConsumptionListActivity.this, false);
        orderId=getIntent().getIntExtra("orderId",0);
        LogUtils.e("orderId"+orderId);
        iv_top_back=(ImageView)findViewById(R.id.iv_top_back);
        iv_top_back.setOnClickListener(this);
        getOrderInfo();
    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_top_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }

        }
    }

    private void getOrderInfo() {
        loadingDialog.show();
        GetOrderInfoProtocol getOrderInfoProtocol=new GetOrderInfoProtocol();
        url = getOrderInfoProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();

        params.put("orderId",  String.valueOf(orderId));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                GetOrderInfoResponse getOrderInfoResponse = gson.fromJson(json, GetOrderInfoResponse.class);
                LogUtils.e("getOrderInfoResponse:" + getOrderInfoResponse.toString());
                if (getOrderInfoResponse.getCode() == 0) {


                } else {
                    DialogUtils.showAlertDialog(ConsumptionListActivity.this, getOrderInfoResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(ConsumptionListActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

}
