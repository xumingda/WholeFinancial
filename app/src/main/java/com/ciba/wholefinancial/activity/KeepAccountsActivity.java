package com.ciba.wholefinancial.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
import com.ciba.wholefinancial.protocol.CashReceiptsProtocol;
import com.ciba.wholefinancial.protocol.GetBusinessShopListProtocol;
import com.ciba.wholefinancial.response.CashReceiptsResponse;
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

//记账
public class KeepAccountsActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;
    private Button btn_pay;
    //输入金额
    private String cash;
    private String remark;


    //当前选择项
    private String businessShopId;
    private View view_back;
    private Dialog loadingDialog;
    private CashReceiptsResponse cashReceiptsResponse;
    private MerchantBean merchantBean;
    private String url;
    private Gson gson;
    private TextView tv_title;

    private TextView tv_money;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_keep_accounts, null);
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
        cash=intent.getStringExtra("cash");
        businessShopId=intent.getStringExtra("businessShopId");
        remark=intent.getStringExtra("remark");
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        merchantBean = SPUtils.getBeanFromSp(this, "businessObject", "MerchantBean");
        loadingDialog = DialogUtils.createLoadDialog(this, true);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_title= (TextView) findViewById(R.id.tv_title);
        view_back = (View) findViewById(R.id.view_back);

        btn_pay= (Button) findViewById(R.id.btn_pay);
        tv_money.setText("付款金额:￥"+cash+"元");
        SpannableStringBuilder style=new SpannableStringBuilder(tv_money.getText());
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.errorColor)), 6, tv_money.getText().length()-1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv_money.setText(style);
        btn_pay.setOnClickListener(this);
        view_back.setOnClickListener(this);
        tv_title.setOnClickListener(this);
    }









    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pay:{
               toCashReceipts();
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


    public void toCashReceipts() {
        loadingDialog.show();
        CashReceiptsProtocol cashReceiptsProtocol=new CashReceiptsProtocol();
        url = cashReceiptsProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("businessId", String.valueOf(merchantBean.getId()));
        params.put("businessShopId", businessShopId);
        params.put("money", cash);
        params.put("remark", remark);

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                cashReceiptsResponse = gson.fromJson(json, CashReceiptsResponse.class);
                LogUtils.e("cashReceiptsResponse:" + cashReceiptsResponse.toString());
                if (cashReceiptsResponse.getCode() == 0) {
                    Intent intent = new Intent(KeepAccountsActivity.this, PayFinishInfoActivity.class);
                    intent.putExtra("createTime",cashReceiptsResponse.getData().getCreateTime());
                    intent.putExtra("money",cashReceiptsResponse.getData().getMoney());
                    intent.putExtra("remark",cashReceiptsResponse.getData().getRemark());
                    intent.putExtra("orderNo",cashReceiptsResponse.getData().getOrderNo());
                    intent.putExtra("shopName",cashReceiptsResponse.getData().getShopName());
                    UIUtils.startActivityNextAnim(intent);
                    finish();
                } else {
                    DialogUtils.showAlertDialog(KeepAccountsActivity.this, cashReceiptsResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(KeepAccountsActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }


}
