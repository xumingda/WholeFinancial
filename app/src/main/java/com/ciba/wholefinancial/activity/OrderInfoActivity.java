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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.adapter.OrderGoodsAdapter;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.MessageBean;
import com.ciba.wholefinancial.protocol.AddClassProtocol;
import com.ciba.wholefinancial.protocol.CancelOrderProtocol;
import com.ciba.wholefinancial.protocol.GetMerchantInfoProtocol;
import com.ciba.wholefinancial.protocol.GetOrderInfoProtocol;
import com.ciba.wholefinancial.protocol.PayOrderProtocol;
import com.ciba.wholefinancial.protocol.PrintProtocol;
import com.ciba.wholefinancial.response.AddOrUpdateLoginResponse;
import com.ciba.wholefinancial.response.GetMerchantInfoResponse;
import com.ciba.wholefinancial.response.GetOrderInfoResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SharedPrefrenceUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;


public class OrderInfoActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;
    private ListView lv_order;
    private TextView tv_table_code,tv_time,tv_orderMoney,tv_people,tv_remark,tv_needMoney,tv_activeMoney,tv_userMoney;
    private ImageView iv_top_back;
    private int orderId;
    //1取消。2结账
    private int type=1;
    private TextView tv_pay,tv_cancel,tv_print,tv_type_title,tv_money_title;
    private AlertDialog alertDialog;
    private Dialog loadingDialog;
    private String url;
    private Gson gson;
    private OrderGoodsAdapter orderGoodsAdapter;
    private int eatType;
    private int status;
    private LinearLayout ll_bottom;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_order_info, null);
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
        loadingDialog = DialogUtils.createLoadDialog(this, true);
        orderId=getIntent().getIntExtra("orderId",0);
        eatType=getIntent().getIntExtra("eatType",0);
        // 0未支付，1已支付，2已取消
        status=getIntent().getIntExtra("status",0);
        iv_top_back=(ImageView)findViewById(R.id.iv_top_back);
        tv_type_title=(TextView) findViewById(R.id.tv_type_title);
        tv_money_title=(TextView) findViewById(R.id.tv_money_title);
        ll_bottom=(LinearLayout) findViewById(R.id.ll_bottom);
        tv_table_code=(TextView) findViewById(R.id.tv_table_code);
        tv_pay=(TextView) findViewById(R.id.tv_pay);
        tv_cancel=(TextView) findViewById(R.id.tv_cancel);
        tv_people=(TextView) findViewById(R.id.tv_people);
        tv_time=(TextView) findViewById(R.id.tv_time);
        tv_orderMoney=(TextView) findViewById(R.id.tv_orderMoney);
        tv_activeMoney=(TextView) findViewById(R.id.tv_activeMoney);
        tv_needMoney=(TextView) findViewById(R.id.tv_needMoney);
        tv_userMoney=(TextView) findViewById(R.id.tv_userMoney);
        tv_remark=(TextView) findViewById(R.id.tv_remark);
        tv_print=(TextView) findViewById(R.id.tv_print);
        lv_order=(ListView)findViewById(R.id.lv_order);
        iv_top_back.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_print.setOnClickListener(this);
        getOrderInfo();
        if(eatType==1){
            tv_pay.setText("已配送");
        }else{
            tv_pay.setText("结账");
            tv_pay.setOnClickListener(this);
        }
        if(status==0){
            tv_type_title.setVisibility(View.VISIBLE);
            tv_money_title.setVisibility(View.VISIBLE);
            ll_bottom.setVisibility(View.VISIBLE);

        }else{
            tv_type_title.setVisibility(View.GONE);
            tv_money_title.setVisibility(View.GONE);
            ll_bottom.setVisibility(View.GONE);
        }
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
            case R.id.tv_print:{
                printOrder();
                break;
            }
            case R.id.tv_cancel:{
                type=1;
                alertDialog=DialogUtils.showAlertDoubleBtnDialog(OrderInfoActivity.this,"确认取消订单？","确定",1,OrderInfoActivity.this);
                break;
            }
            case R.id.tv_pay:{
                type=2;
                alertDialog=DialogUtils.showAlertDoubleBtnDialog(OrderInfoActivity.this,"确认结账？","确定",1,OrderInfoActivity.this);
                break;
            }
            case R.id.tv_ensure:{
                if(type==1){
                    cancleOrder();
                }else{
                    payOrder();
                }

                break;
            }
        }
    }


    private void getOrderInfo() {
        loadingDialog.show();
        GetOrderInfoProtocol getOrderInfoProtocol=new GetOrderInfoProtocol();
        String url = getOrderInfoProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderId",  String.valueOf(orderId));
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                GetOrderInfoResponse getOrderInfoResponse = gson.fromJson(json, GetOrderInfoResponse.class);
                LogUtils.e("getOrderInfoResponse:" + getOrderInfoResponse.toString());
                if (getOrderInfoResponse.getCode() == 0) {
                    tv_table_code.setText("桌牌号："+getOrderInfoResponse.getOrder().getCode()+"号");
                    tv_time.setText(getOrderInfoResponse.getOrder().getCreateTime());
                    tv_orderMoney.setText("订单总额¥"+getOrderInfoResponse.getOrder().getOrderMoney());
                    tv_userMoney.setText("会员优惠金额：¥"+getOrderInfoResponse.getOrder().getUserMoney());
                    tv_activeMoney.setText("活动优惠金额¥"+getOrderInfoResponse.getOrder().getActiveMoney());
                    tv_needMoney.setText("实际支付金额¥"+getOrderInfoResponse.getOrder().getNeedMoney());
                    tv_people.setText("人数："+getOrderInfoResponse.getOrder().getNumber()+"人");
                    tv_remark.setText(getOrderInfoResponse.getOrder().getRemark());
                    if(orderGoodsAdapter==null){
                        orderGoodsAdapter=new OrderGoodsAdapter(OrderInfoActivity.this,getOrderInfoResponse.getOrderGoodsList());
                        lv_order.setAdapter(orderGoodsAdapter);
                    }else{
                        orderGoodsAdapter.notifyDataSetChanged();
                    }
                } else {
                    DialogUtils.showAlertDialog(OrderInfoActivity.this, getOrderInfoResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(OrderInfoActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
    private void cancleOrder() {
        loadingDialog.show();
        CancelOrderProtocol cancelOrderProtocol=new CancelOrderProtocol();
        String url = cancelOrderProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderId",  String.valueOf(orderId));
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                AddOrUpdateLoginResponse cancleOrder = gson.fromJson(json, AddOrUpdateLoginResponse.class);
                LogUtils.e("cancleOrder:" + cancleOrder.toString());
                alertDialog.cancel();
                if (cancleOrder.getCode() == 0) {
                    DialogUtils.showAlertDialog(OrderInfoActivity.this, "取消成功！");
                } else {
                    DialogUtils.showAlertDialog(OrderInfoActivity.this, cancleOrder.getMsg());
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(OrderInfoActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
    //打印
    private void printOrder() {
        loadingDialog.show();
        PrintProtocol printProtocol=new PrintProtocol();
        String url = printProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderId",  String.valueOf(orderId));
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                AddOrUpdateLoginResponse cancleOrder = gson.fromJson(json, AddOrUpdateLoginResponse.class);
                LogUtils.e("printOrder:" + cancleOrder.toString());
                if (cancleOrder.getCode() == 0) {
                    DialogUtils.showAlertDialog(OrderInfoActivity.this, "打印成功！");
                } else {
                    DialogUtils.showAlertDialog(OrderInfoActivity.this, cancleOrder.getMsg());
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(OrderInfoActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
    private void payOrder() {
        loadingDialog.show();
        PayOrderProtocol payOrderProtocol=new PayOrderProtocol();
        String url = payOrderProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("orderId",  String.valueOf(orderId));
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                AddOrUpdateLoginResponse cancleOrder = gson.fromJson(json, AddOrUpdateLoginResponse.class);
                LogUtils.e("payOrderProtocol:" + cancleOrder.toString());
                alertDialog.cancel();
                if (cancleOrder.getCode() == 0) {
                } else {
                    DialogUtils.showAlertDialog(OrderInfoActivity.this, cancleOrder.getMsg());
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(OrderInfoActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
}
