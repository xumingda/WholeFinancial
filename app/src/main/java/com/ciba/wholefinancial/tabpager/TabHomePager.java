package com.ciba.wholefinancial.tabpager;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.activity.CashPayActivity;
import com.ciba.wholefinancial.activity.InformationReportActivity;
import com.ciba.wholefinancial.activity.LoginActivity;
import com.ciba.wholefinancial.activity.MarketingActivity;
import com.ciba.wholefinancial.activity.MemberManagerActivity;
import com.ciba.wholefinancial.activity.MenuManagementActivity;
import com.ciba.wholefinancial.activity.MerchantMyselfActivity;
import com.ciba.wholefinancial.activity.MerchantSettingActivity;
import com.ciba.wholefinancial.activity.MessageActivity;
import com.ciba.wholefinancial.activity.MyselfActivity;
import com.ciba.wholefinancial.activity.OrderManagerActivity;
import com.ciba.wholefinancial.activity.StatisticsActivity;
import com.ciba.wholefinancial.activity.StatisticsMerchantActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.TabBasePager;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.protocol.GetHomeProtocol;
import com.ciba.wholefinancial.protocol.GetTableCodeListProtocol;
import com.ciba.wholefinancial.response.GetHomeResponse;
import com.ciba.wholefinancial.response.GetTableCodeResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.SharedPrefrenceUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.MyLinearLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.ViewUtils;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @作者: 许明达
 * @创建时间: 2016年3月23日上午11:10:20
 * @版权: 特速版权所有
 * @描述: TODO
 */
public class TabHomePager extends TabBasePager implements View.OnClickListener {


    RelativeLayout view;
    LayoutInflater mInflater;
    private FrameLayout mDragLayout;
    private MyLinearLayout mLinearLayout;
    private RelativeLayout rl_order_manager;
    private RelativeLayout rl_marketing;
    private RelativeLayout rl_set,rl_member_manager,rl_message,rl_report,rl_menu,rl_pay,rl_tongji;
    private Dialog loadingDialog;
    private String url;
    private Gson gson;
    private MerchantBean merchantBean;
    private TextView tv_shop_name,tv_todayTurnover,tv_qrPayTurnover,tv_todayOrderSucCount;
    BroadcastReceiver mReceiver;
    /**
     * @param context
     */
    public TabHomePager(Context context, FrameLayout mDragLayout,
                        MyLinearLayout mLinearLayout,BroadcastReceiver mReceiver ) {
        super(context, mDragLayout);
        this.mDragLayout = mDragLayout;
        this.mLinearLayout = mLinearLayout;
        this.mReceiver=mReceiver;
    }


    @Override
    protected View initView() {
        view = (RelativeLayout) View.inflate(mContext, R.layout.home_pager, null);
        ViewUtils.inject(this, view);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }
        return view;
    }

    public void initData() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(mContext, true);
        merchantBean= SPUtils.getBeanFromSp(mContext,"businessObject","MerchantBean");
        tv_shop_name=(TextView)view.findViewById(R.id.tv_shop_name);
        tv_todayTurnover=(TextView)view.findViewById(R.id.tv_todayTurnover);
        tv_qrPayTurnover=(TextView)view.findViewById(R.id.tv_qrPayTurnover);
        tv_todayOrderSucCount=(TextView)view.findViewById(R.id.tv_todayOrderSucCount);
        rl_menu=(RelativeLayout)view.findViewById(R.id.rl_menu);
        rl_pay=(RelativeLayout)view.findViewById(R.id.rl_pay);
        rl_report=(RelativeLayout)view.findViewById(R.id.rl_report);
        rl_message=(RelativeLayout)view.findViewById(R.id.rl_message);
        rl_member_manager=(RelativeLayout)view.findViewById(R.id.rl_member_manager);
        rl_marketing=(RelativeLayout)view.findViewById(R.id.rl_marketing);
        rl_order_manager=(RelativeLayout)view.findViewById(R.id.rl_order_manager);
        rl_tongji=(RelativeLayout)view.findViewById(R.id.rl_tongji);
        rl_set=(RelativeLayout)view.findViewById(R.id.rl_set);
        rl_order_manager.setOnClickListener(this);
        rl_marketing.setOnClickListener(this);
        rl_set.setOnClickListener(this);
        rl_message.setOnClickListener(this);
        rl_member_manager.setOnClickListener(this);
        rl_report.setOnClickListener(this);
        rl_menu.setOnClickListener(this);
        rl_tongji.setOnClickListener(this);
        rl_pay.setOnClickListener(this);
        tv_shop_name.setText(merchantBean.getStoreName());
        getHome();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dessert.mojito.CHANGE_STATUS");
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, filter);
    }








    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pay:{
                Intent intent = new Intent(mContext, CashPayActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_tongji: {
                Intent intent = new Intent(mContext, StatisticsMerchantActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_menu:{
                Intent intent=new Intent(mContext, MenuManagementActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_report:{
                Intent intent=new Intent(mContext, InformationReportActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_message:{
                Intent intent=new Intent(mContext, MessageActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_set:{
                Intent intent=new Intent(mContext,MerchantSettingActivity   .class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_marketing:{
                Intent intent=new Intent(mContext,MarketingActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_order_manager:{
                Intent intent=new Intent(mContext,OrderManagerActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_member_manager:{
                if(SharedPrefrenceUtils.getInt(mContext,"master",0)==1){
                    Intent intent=new Intent(mContext, MemberManagerActivity.class);
                    UIUtils.startActivityNextAnim(intent);
                }else{
                    DialogUtils.showAlertDialog(mContext, "您没有权限查看！");
                }

                break;
            }
        }
    }

    public void getHome() {
        loadingDialog.show();
        GetHomeProtocol getHomeProtocol=new GetHomeProtocol();
        url = getHomeProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("businessId", String.valueOf(merchantBean.getId()));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                GetHomeResponse getHomeResponse = gson.fromJson(json, GetHomeResponse.class);
                LogUtils.e("getHomeResponse:" + getHomeResponse.toString());
                if (getHomeResponse.getCode() == 0) {
                    DecimalFormat df = new java.text.DecimalFormat("#.00");
                    if(getHomeResponse.getQrPayTurnover()>0){
                        tv_qrPayTurnover.setText(df.format(getHomeResponse.getQrPayTurnover()));
                    }else{
                        tv_qrPayTurnover.setText("0.00");
                    }
                    if (getHomeResponse.getTodayTurnover()>0){
                        tv_todayTurnover.setText(df.format(getHomeResponse.getTodayTurnover()));
                    }else{
                        tv_todayTurnover.setText("0.00");
                    }
                   tv_todayOrderSucCount.setText(getHomeResponse.getTodayOrderSucCount());

                } else {
                    if(getHomeResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getHomeResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(mContext, getHomeResponse.msg);
                    }
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
