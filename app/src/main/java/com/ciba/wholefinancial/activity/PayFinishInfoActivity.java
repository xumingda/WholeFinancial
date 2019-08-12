package com.ciba.wholefinancial.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.protocol.GetBusinessShopListProtocol;
import com.ciba.wholefinancial.response.GetBusinessShopListResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.SelectShopPopuwindow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//收款结果
public class PayFinishInfoActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;
    private Button btn_finish;
    private String createTime;
    private String money;
    private String orderNo;
    private String remark;
    private String shopName;

    private View view_back;
    private TextView tv_omney,pay_time,pay_no,pay_remark,tv_shop_name;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_pay_finish, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        Intent intent=getIntent();
        createTime=intent.getStringExtra("createTime");
        money=intent.getStringExtra("money");
        orderNo=intent.getStringExtra("orderNo");
        shopName=intent.getStringExtra("shopName");
        remark=intent.getStringExtra("remark");

        tv_omney = (TextView) findViewById(R.id.tv_omney);
        pay_no= (TextView) findViewById(R.id.pay_no);
        pay_time= (TextView) findViewById(R.id.pay_time);
        pay_remark= (TextView) findViewById(R.id.pay_remark);
        tv_shop_name= (TextView) findViewById(R.id.tv_shop_name);
        view_back = (View) findViewById(R.id.view_back);

        btn_finish= (Button) findViewById(R.id.btn_finish);

        btn_finish.setOnClickListener(this);
        view_back.setOnClickListener(this);
        tv_omney.setText("￥"+money);
        tv_shop_name.setText(shopName);
        pay_time.setText(createTime);
        pay_no.setText(orderNo);
        pay_remark.setText(remark);
    }









    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finish:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }


            case R.id.view_back: {

                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }

        }
    }

    @Override

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);

        }

        return super.onKeyDown(keyCode, event);

    }



}
