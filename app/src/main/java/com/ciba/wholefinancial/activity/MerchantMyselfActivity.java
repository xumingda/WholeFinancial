package com.ciba.wholefinancial.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.BaseApplication;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.DateStateBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.callback.OnSetViewCallBack;
import com.ciba.wholefinancial.fragment.ControlTabFragment;
import com.ciba.wholefinancial.listener.OnPagerChangeListener;
import com.ciba.wholefinancial.protocol.ClockCountProtocol;
import com.ciba.wholefinancial.protocol.ClockProtocol;
import com.ciba.wholefinancial.protocol.GetClockListProtocol;
import com.ciba.wholefinancial.response.ClockCountResponse;
import com.ciba.wholefinancial.response.ClockResponse;
import com.ciba.wholefinancial.response.GetClocktListResponse;
import com.ciba.wholefinancial.service.LocationService;
import com.ciba.wholefinancial.util.CalendarUtil;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.CalendarView;
import com.ciba.wholefinancial.weiget.ClockOutPopuwindow;
import com.ciba.wholefinancial.weiget.ClockPopuwindow;
import com.ciba.wholefinancial.weiget.SpinnerPopuwindow;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class MerchantMyselfActivity extends BaseActivity  {
    private static final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission. WRITE_SETTINGS
    };
    private static final int MSG_SET_ALIAS = 1001;
    public static boolean isForeground = false;
    public static ControlTabFragment ctf;
    private static final int BASIC_PERMISSION_REQUEST_CODE = 1000;
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
        View rootView = View.inflate(this, R.layout.activity_merchant_myself, null);
        setContentView(rootView);
        initFragment();
        requestBasicPermission();
        return rootView;
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        // 1. 开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        // 添加主页fragment
        ctf = new ControlTabFragment();
        transaction.replace(R.id.main_container, ctf);
        transaction.commit();

    }

    public static ControlTabFragment getCtf() {
        return ctf;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 监听返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onKeyDownBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
//        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


    @Override
    protected void onResume() {
//        JPushInterface.onResume(this);
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
//        JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ctf.setChecked(0);
    }


}
