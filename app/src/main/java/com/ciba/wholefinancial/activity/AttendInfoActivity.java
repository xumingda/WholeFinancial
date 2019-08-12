package com.ciba.wholefinancial.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.BaseApplication;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.protocol.ClockProtocol;
import com.ciba.wholefinancial.protocol.GetClockInfoProtocol;
import com.ciba.wholefinancial.response.ClockResponse;
import com.ciba.wholefinancial.response.GetClocktInfoResponse;
import com.ciba.wholefinancial.response.ImageResponse;
import com.ciba.wholefinancial.service.LocationService;
import com.ciba.wholefinancial.util.BitmapUtils;
import com.ciba.wholefinancial.util.Constants;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.PictureOption;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.SharedPrefrenceUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.RoundImageView;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AttendInfoActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;

    private SalesmanBean salesmanBean;
    private View view_back;
    private Dialog loadingDialog;
    private RelativeLayout ll_main;

    private ImageLoader imageLoader;





    private String attendanceId;
    private RoundImageView iv_head;
    private TextView tv_cream_time,tv_clock_time,tv_clock_address;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_attend_info, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        loadingDialog = DialogUtils.createLoadDialog(AttendInfoActivity.this, false);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));
        salesmanBean= SPUtils.getBeanFromSp(AttendInfoActivity.this,"salesmanObject","SalesmanBean");
        attendanceId=getIntent().getStringExtra("attendanceId");
        iv_head=(RoundImageView) findViewById(R.id.iv_head);
        ll_main=(RelativeLayout) findViewById(R.id.ll_main);

        tv_cream_time=(TextView) findViewById(R.id.tv_create_time);
        tv_clock_time=(TextView) findViewById(R.id.tv_clock_time);
        tv_clock_address=(TextView) findViewById(R.id.tv_clock_address);
        view_back=(View)findViewById(R.id.view_back);

        view_back.setOnClickListener(this);

        getClockInfo();

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

        }
    }

    //获取考勤数据
    public void getClockInfo() {
        loadingDialog.show();

        GetClockInfoProtocol getClockInfoProtocol = new GetClockInfoProtocol();

        String url = getClockInfoProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("salesmanId",  String.valueOf(salesmanBean.getId()));
        params.put("attendanceId",  attendanceId);
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                GetClocktInfoResponse getClockInfoResponse = gson.fromJson(json, GetClocktInfoResponse.class);
                LogUtils.e("getClockInfoResponse:" + getClockInfoResponse.toString());
                loadingDialog.dismiss();
                if (getClockInfoResponse.code == 0) {

                    tv_cream_time.setText("发起时间："+getClockInfoResponse.getData().getCreateTime());
                    SpannableStringBuilder style=new SpannableStringBuilder(tv_cream_time.getText());
                    style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_black)), 5, tv_cream_time.getText().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    tv_cream_time.setText(style);

                    tv_clock_time.setText("打卡时间："+getClockInfoResponse.getData().getClickTime());
                    SpannableStringBuilder style1=new SpannableStringBuilder(tv_clock_time.getText());
                    style1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_black)), 5, tv_clock_time.getText().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    tv_clock_time.setText(style1);

                    tv_clock_address.setText("打卡地址："+getClockInfoResponse.getData().getClickPlace());
                    SpannableStringBuilder style2=new SpannableStringBuilder(tv_clock_address.getText());
                    style2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_black)), 5, tv_clock_address.getText().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    tv_clock_address.setText(style2);
                    imageLoader.displayImage(getClockInfoResponse.getData().getPic(), iv_head, PictureOption.getSimpleOptions());

                } else {

                    DialogUtils.showAlertDialog(AttendInfoActivity.this,
                            getClockInfoResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AttendInfoActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
}
