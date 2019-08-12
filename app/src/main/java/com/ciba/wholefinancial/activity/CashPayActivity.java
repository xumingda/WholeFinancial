package com.ciba.wholefinancial.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.callback.OnStatisticsCallBack;
import com.ciba.wholefinancial.datepicker.CustomDatePicker;
import com.ciba.wholefinancial.protocol.GetBusinessShopListProtocol;
import com.ciba.wholefinancial.response.GetBusinessShopListResponse;
import com.ciba.wholefinancial.tabpager.StatisticsMerchantPager;
import com.ciba.wholefinancial.util.DateUtils;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.NoScrollViewPager;
import com.ciba.wholefinancial.weiget.SelectShopPopuwindow;
import com.ciba.wholefinancial.weiget.TabSlidingIndicator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//现金收款
public class CashPayActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;
    private EditText et_cash,et_remark;
    private Button btn_pay;
    //输入金额
    private String cash;
    private String remark;


    //当前选择项
    private int item;
    private String businessShopId;
    private View view_back;
    private Dialog loadingDialog;
    private GetBusinessShopListResponse getBusinessShopListResponse;
    private MerchantBean merchantBean;
    private String url;
    private Gson gson;
    private TextView tv_title;
    private LinearLayout ll_main;
    private SelectShopPopuwindow selectshopPopuwindow;
    private List<String> nameList;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_cash_pay, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        merchantBean = SPUtils.getBeanFromSp(this, "businessObject", "MerchantBean");
        nameList=new ArrayList<>();
        loadingDialog = DialogUtils.createLoadDialog(this, true);
        ll_main = (LinearLayout) findViewById(R.id.ll_main);
        tv_title= (TextView) findViewById(R.id.tv_title);
        view_back = (View) findViewById(R.id.view_back);
        et_cash= (EditText) findViewById(R.id.et_cash);
        et_remark= (EditText) findViewById(R.id.et_remark);
        btn_pay= (Button) findViewById(R.id.btn_pay);

        btn_pay.setOnClickListener(this);
        view_back.setOnClickListener(this);
        tv_title.setOnClickListener(this);
    }









    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pay:{
                cash=et_cash.getText().toString();
                remark=et_remark.getText().toString();
                if(!TextUtils.isEmpty(cash)&&!TextUtils.isEmpty(businessShopId)){
                    Intent intent = new Intent(this, KeepAccountsActivity.class);
                    intent.putExtra("businessShopId",businessShopId);
                    intent.putExtra("cash",cash);
                    intent.putExtra("remark",remark);
                    UIUtils.startActivityNextAnim(intent);
                    finish();
                }else{
                    if(TextUtils.isEmpty(businessShopId)){
                        DialogUtils.showAlertDialog(CashPayActivity.this, "请先选择门店!");
                    }else {
                        DialogUtils.showAlertDialog(CashPayActivity.this, "请先输入收款金额!");
                    }
                }

                break;
            }
            case R.id.tv_title:{
                if(nameList.size()<=0){
                    getShopList();
                }else{
                    selectshopPopuwindow.showPopupWindow(ll_main);
                }
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


    public void getShopList() {
        loadingDialog.show();
        GetBusinessShopListProtocol getBusinessShopListProtocol=new GetBusinessShopListProtocol();
        url = getBusinessShopListProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("businessId", String.valueOf(merchantBean.getId()));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getBusinessShopListResponse = gson.fromJson(json, GetBusinessShopListResponse.class);
                LogUtils.e("getBusinessShopListResponse:" + getBusinessShopListResponse.toString());
                if (getBusinessShopListResponse.getCode() == 0) {
                    if (getBusinessShopListResponse.getDataList().size() > 0) {
                        for(int i=0;i<getBusinessShopListResponse.getDataList().size();i++){
                            nameList.add(getBusinessShopListResponse.getDataList().get(i).shopName);
                        }
                        selectshopPopuwindow = new SelectShopPopuwindow(UIUtils.getActivity(),"所有店铺",nameList,itemsOnClick);
                        selectshopPopuwindow.showPopupWindow(ll_main);
                    }

                } else {
                    DialogUtils.showAlertDialog(CashPayActivity.this, getBusinessShopListResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(CashPayActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    private AdapterView.OnItemClickListener itemsOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            businessShopId=String.valueOf(getBusinessShopListResponse.getDataList().get(position).getId());
            String value = nameList.get(position);
            tv_title.setText(value);
            selectshopPopuwindow.dismissPopupWindow();
        }
    };
}
