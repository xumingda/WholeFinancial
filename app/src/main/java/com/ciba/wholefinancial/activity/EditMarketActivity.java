package com.ciba.wholefinancial.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.adapter.MarketingAdapter;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.MarketingBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.MessageBean;
import com.ciba.wholefinancial.datepicker.CustomDatePicker;
import com.ciba.wholefinancial.datepicker.DateFormatUtils;
import com.ciba.wholefinancial.protocol.DeleteActivityProtocol;
import com.ciba.wholefinancial.protocol.GetMarketingListProtocol;
import com.ciba.wholefinancial.protocol.UpdateMarketingProtocol;
import com.ciba.wholefinancial.response.DeleteLoginResponse;
import com.ciba.wholefinancial.response.GetMarketingListResponse;
import com.ciba.wholefinancial.response.UpdateMarketingResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//营销编辑
public class EditMarketActivity extends BaseActivity implements View.OnClickListener
        {

    private LayoutInflater mInflater;
    private View rootView;

    private View view_back;

    private String url;
    private Gson gson;
    private Dialog loadingDialog;
    private MarketingBean marketingBean;
    private EditText et_activity_title;
    private TextView tv_endTime,tv_startTime,et_activity_type,tv_levelName,tv_remark;
    private Button btn_delect;
    private CustomDatePicker  mTimerPicker,mEndTimerPicker;
    private String title;
    private String startTime;
    private String endTime;
    private Button btn_save;
    private LinearLayout ll_main;
    private AlertDialog alertDialog;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_marketing_edit, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        marketingBean=(MarketingBean) getIntent().getSerializableExtra("MarketingBean");
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(EditMarketActivity.this, false);
        ll_main=(LinearLayout) findViewById(R.id.ll_main);
        view_back=(View)findViewById(R.id.view_back);
        tv_remark=(TextView) findViewById(R.id.tv_remark);
        tv_levelName=(TextView) findViewById(R.id.tv_levelName);
        tv_startTime=(TextView) findViewById(R.id.tv_startTime);
        tv_endTime=(TextView) findViewById(R.id.tv_endTime);
        btn_delect=(Button) findViewById(R.id.btn_delect);
        et_activity_title=(EditText) findViewById(R.id.et_activity_title);
        et_activity_type=(TextView) findViewById(R.id.et_activity_type);
        btn_save=(Button)findViewById(R.id.btn_save);
        et_activity_title.setText(marketingBean.getTitle());
        //type	活动类型设置（1消费金额满减，2等级优惠，3递增优惠）
        if(marketingBean.getType()==1){
            et_activity_type.setText("消费金额满减");
        }else if (marketingBean.getType()==2){
            et_activity_type.setText("等级优惠");
        }else{
            et_activity_type.setText("递增优惠");
        }
        tv_remark.setText(marketingBean.getRemark());
        tv_levelName.setText(marketingBean.getLevelName());
//        tv_startTime.setText(marketingBean.getStartTime());
        tv_endTime.setText(marketingBean.getEndTime());
        view_back.setOnClickListener(this);
        tv_startTime.setOnClickListener(this);
        tv_endTime.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_delect.setOnClickListener(this);
        initTimerPicker();
        initEndTimerPicker();

    }

    private void initTimerPicker() {
            String beginTime = marketingBean.getStartTime();
            String endTime = "2028-10-17 18:00";

            tv_startTime.setText(beginTime);

            // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
            mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
                @Override
                public void onTimeSelected(long timestamp) {
                    tv_startTime.setText(DateFormatUtils.long2Str(timestamp, true)+":00");
                }
            }, beginTime, endTime);
            // 允许点击屏幕或物理返回键关闭
            mTimerPicker.setCancelable(true);
            // 显示时和分
            mTimerPicker.setCanShowPreciseTime(true);
            // 允许循环滚动
            mTimerPicker.setScrollLoop(true);
            // 允许滚动动画
            mTimerPicker.setCanShowAnim(true);
    }

    private void initEndTimerPicker() {
        String beginTime = marketingBean.getEndTime();
        String endTime = "2028-10-17 18:00";

        tv_endTime.setText(beginTime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mEndTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                tv_endTime.setText(DateFormatUtils.long2Str(timestamp, true)+":00");
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mEndTimerPicker.setCancelable(true);
        // 显示时和分
        mEndTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mEndTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mEndTimerPicker.setCanShowAnim(true);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_save:{
                title=et_activity_title.getText().toString();
                startTime=tv_startTime.getText().toString();
                endTime=tv_endTime.getText().toString();
                if(title.length()>0){
                    updateMarketingInfo();
                }else{
                    DialogUtils.showAlertDialog(EditMarketActivity.this, "标题不能为空！");
                }
                break;
            }
            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.tv_startTime:{
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(tv_startTime.getText().toString());
                break;
            }
            case R.id.tv_endTime:{
                // 日期格式为yyyy-MM-dd HH:mm
                mEndTimerPicker.show(tv_endTime.getText().toString());
                break;
            }
            case R.id.btn_delect:{
                LogUtils.e("删除");
                alertDialog=DialogUtils.showAlertDoubleBtnDialog(EditMarketActivity.this,"确认删除？","",1,EditMarketActivity.this);
                break;
            }
            case R.id.tv_ensure:{
                deleteLogin();
                alertDialog.cancel();
                break;
            }

        }
    }
    private void updateMarketingInfo() {
        loadingDialog.show();
        UpdateMarketingProtocol updateMarketingProtocol=new UpdateMarketingProtocol();
        url = updateMarketingProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("activityId",  String.valueOf(marketingBean.getActivityId()));
        params.put("title", title);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
               UpdateMarketingResponse updateMarketingResponse = gson.fromJson(json, UpdateMarketingResponse.class);
                LogUtils.e("updateMarketingResponse:" + updateMarketingResponse.toString());
                if (updateMarketingResponse.getCode() == 0) {
                    SuccessPopuwindow successPopuwindow=new SuccessPopuwindow(EditMarketActivity.this,null,"修改成功");
                    successPopuwindow.showPopupWindow(ll_main);
                } else {
                    DialogUtils.showAlertDialog(EditMarketActivity.this, updateMarketingResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(EditMarketActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }


    public void deleteLogin() {
        loadingDialog.show();
        DeleteActivityProtocol deleteActivityProtocol = new DeleteActivityProtocol();

        String url = deleteActivityProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("activityId",  String.valueOf(marketingBean.getActivityId()));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                DeleteLoginResponse deleteLoginResponse = gson.fromJson(json, DeleteLoginResponse.class);
                LogUtils.e("deleteLoginResponse:" + deleteLoginResponse.toString());
                if (deleteLoginResponse.code==0) {
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                } else {

                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(EditMarketActivity.this,
                            deleteLoginResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(EditMarketActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(EditMarketActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }
}
