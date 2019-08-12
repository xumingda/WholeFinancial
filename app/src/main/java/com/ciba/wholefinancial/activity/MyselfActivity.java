package com.ciba.wholefinancial.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.CityCodeBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.callback.OnCodeListsCallBack;
import com.ciba.wholefinancial.protocol.ClockCountProtocol;
import com.ciba.wholefinancial.protocol.ClockProtocol;
import com.ciba.wholefinancial.protocol.GetClockListProtocol;
import com.ciba.wholefinancial.protocol.ModifyLoginPwdProtocol;
import com.ciba.wholefinancial.response.ClockCountResponse;
import com.ciba.wholefinancial.response.ClockResponse;
import com.ciba.wholefinancial.response.GetClocktListResponse;
import com.ciba.wholefinancial.response.ModifyLoginPwdResponse;
import com.ciba.wholefinancial.service.LocationService;
import com.ciba.wholefinancial.util.CalendarUtil;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.ListDataSave;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.MD5Utils;
import com.ciba.wholefinancial.util.ReadCityCodeAsyncTask;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.SharedPrefrenceUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.CalendarView;
import com.ciba.wholefinancial.weiget.ClockOutPopuwindow;
import com.ciba.wholefinancial.weiget.ClockPopuwindow;
import com.ciba.wholefinancial.weiget.SpinnerPopuwindow;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.google.gson.Gson;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MyselfActivity extends BaseActivity implements View.OnClickListener, OnCodeListsCallBack {
    private static final int BASIC_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission. WRITE_SETTINGS
    };
    private LayoutInflater mInflater;
    private View rootView;

    /** 模拟的假数据 */
    private List<String> testData;
    private SpinnerPopuwindow mSpinnerPopuwindow;
    private RelativeLayout rl_main;

    private RelativeLayout rl_business;
    private RelativeLayout rl_report;
    private RelativeLayout rl_message,rl_attend,rl_tongji,rl_tuiguan;
    private View v_earnings;
    private RelativeLayout rl_set;
    private SalesmanBean salesmanBean;
    private TextView tv_user_name;
    private TextView tv_id;
    private TextView tv_merchants_num;
    private TextView tv_earnings;
    private Dialog loadingDialog;
    private TextView tv_startTime;
    private TextView tv_endTime;
    private boolean isClockIn=false;
    private boolean isClockOff=false;
    private int clock_type=0;
    private LinearLayout ll_merchant;

    //打卡地址
    private  String addrstr;
    //纬度
    private double latitude;
    //经度
    private double lontitude;
    //年
    private String year;
    //月
    private String month;
    private ListDataSave listDataSave;

    private void requestBasicPermission() {
        boolean isOpen=false;
        for(int i=0;i<BASIC_PERMISSIONS.length;i++){
            if (ContextCompat.checkSelfPermission(this,BASIC_PERMISSIONS[i])
                    == PackageManager.PERMISSION_GRANTED){
                isOpen=true;
            }
        }
        if (!isOpen) {
            ActivityCompat.requestPermissions(this,
                    BASIC_PERMISSIONS, BASIC_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_myself, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        listDataSave=new ListDataSave(this,"whole");
        requestBasicPermission();
        salesmanBean= SPUtils.getBeanFromSp(this,"salesmanObject","SalesmanBean");
        loadingDialog = DialogUtils.createLoadDialog(MyselfActivity.this, false);
        ll_merchant=(LinearLayout) findViewById(R.id.ll_merchant);
        v_earnings=(View) findViewById(R.id.v_earnings);
        tv_user_name=(TextView)findViewById(R.id.tv_user_name);
        tv_id=(TextView)findViewById(R.id.tv_id);
        tv_merchants_num=(TextView)findViewById(R.id.tv_merchants_num);
        tv_earnings=(TextView)findViewById(R.id.tv_earnings);
        rl_message=(RelativeLayout) findViewById(R.id.rl_message);
        rl_business=(RelativeLayout) findViewById(R.id.rl_business);
        rl_report=(RelativeLayout) findViewById(R.id.rl_report);
        rl_attend=(RelativeLayout) findViewById(R.id.rl_attend);
        rl_tongji=(RelativeLayout) findViewById(R.id.rl_tongji);
        rl_main=(RelativeLayout) findViewById(R.id.rl_main);
        rl_tuiguan=(RelativeLayout) findViewById(R.id.rl_tuiguan);
        rl_set=(RelativeLayout) findViewById(R.id.rl_set);
        tv_startTime=(TextView)findViewById(R.id.tv_startTime);
        tv_endTime=(TextView)findViewById(R.id.tv_endTime);

        tv_user_name.setText(salesmanBean.getName());
        if(salesmanBean.getPhoneNumber().length()>10){
            tv_id.setText("账号："+salesmanBean.getPhoneNumber().substring(0,3)+"****"+salesmanBean.getPhoneNumber().substring(7,11));
        }else{
            tv_id.setText("账号："+salesmanBean.getPhoneNumber());
        }

        tv_merchants_num.setText(""+salesmanBean.getBusinessCount());
        tv_earnings.setText(""+salesmanBean.getCommission());
        Calendar now = Calendar.getInstance();
        year= String.valueOf(now.get(Calendar.YEAR));
        month=String.valueOf(now.get(Calendar.MONTH)+1);

        rl_tongji.setOnClickListener(this);
        rl_message.setOnClickListener(this);
        rl_report.setOnClickListener(this);
        rl_business.setOnClickListener(this);
        rl_set.setOnClickListener(this);
        ll_merchant.setOnClickListener(this);
        v_earnings.setOnClickListener(this);
        rl_attend.setOnClickListener(this);
        rl_tuiguan.setOnClickListener(this);
        //设置当天时间
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm:ss");
        String dateNowStr = sdf.format(d);



        boolean getCode=SharedPrefrenceUtils.getBoolean(this,"getCode",false);
        List<CityCodeBean> cityCodeBeanList=listDataSave.getDataList("cityCodeBeanList");
        if(!getCode){
            ReadCityCodeAsyncTask readCityCodeAsyncTask= new ReadCityCodeAsyncTask();
            readCityCodeAsyncTask.execute();
            readCityCodeAsyncTask.setOnCodeListsCallBack(this);
        }else{
            LogUtils.e("cityCodeBeanList:"+cityCodeBeanList.size());
        }

    }





//    public void clockIn(View view) {
//        if(isClockIn){
//            DialogUtils.showAlertDialog(MyselfActivity.this,
//                   "无法重复打卡！");
//        }else{
//            ClockPopuwindow clockPopuwindow=new ClockPopuwindow(MyselfActivity.this,MyselfActivity.this);
//            clockPopuwindow.showPopupWindow(rl_main);
//        }
//
//
//    }
//    public void clockOff(View view) {
//        if(isClockOff){
//            DialogUtils.showAlertDialog(MyselfActivity.this,
//                    "无法重复打卡！");
//        }else{
//            ClockOutPopuwindow clockOutPopuwindow=new ClockOutPopuwindow(MyselfActivity.this,MyselfActivity.this);
//            clockOutPopuwindow.showPopupWindow(rl_main);
//        }
//
//    }
    public void setting() {
        Intent intent=new Intent(this,SettingActivity.class);
        UIUtils.startActivityNextAnim(intent);
    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_tuiguan:{
                Intent intent=new Intent(this,ReceiptCodeActivity.class);
                intent.putExtra("title","推广二维码");
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_tongji:{
                Intent intent=new Intent(this,StatisticsActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_attend:{
                Intent intent=new Intent(this,AttendListsActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_set:{
                setting();
                break;
            }

            case R.id.rl_message:{
                Intent intent=new Intent(this,MessageActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_business:{
                Intent intent=new Intent(this,AddMerchantInfoActivity.class);
                UIUtils.startActivityForResultNextAnim(intent,100);

                break;
            }
            case R.id.ll_merchant:{
                Intent intent=new Intent(this,MyMerchantActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.v_earnings:{
                Intent intent=new Intent(this,CommissionActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_report:{
                Intent intent=new Intent(this,InformationReportActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            salesmanBean= SPUtils.getBeanFromSp(this,"salesmanObject","SalesmanBean");
            tv_merchants_num.setText(""+salesmanBean.getBusinessCount());
        }
    }





    //获取打卡数
    public void getClockCount() {
        loadingDialog.show();
        ClockCountProtocol clockCountProtocol = new ClockCountProtocol();

        String url = clockCountProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("salesmanId",  String.valueOf(salesmanBean.getId()));


        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                ClockCountResponse clockCountResponse = gson.fromJson(json, ClockCountResponse.class);
                LogUtils.e("clockResponse:" + clockCountResponse.toString());
                loadingDialog.dismiss();
                if (clockCountResponse.code==0) {


                } else {
                    DialogUtils.showAlertDialog(MyselfActivity.this,
                            clockCountResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(MyselfActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(MyselfActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }

    public void setTextViewColor(TextView tv){
        SpannableStringBuilder style=new SpannableStringBuilder(tv.getText());
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.errorColor)), 4, tv.getText().length()-1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(style);
    }



    @Override
    public void CallBackCityCodeList(List<CityCodeBean> list) {
        SharedPrefrenceUtils.setBoolean(MyselfActivity.this,"getCode",true);
        List<CityCodeBean> cityCodeBeanList=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(i!=0){
                cityCodeBeanList.add(list.get(i));
            }
        }
        listDataSave.setDataList("cityCodeBeanList",cityCodeBeanList);
    }
}
