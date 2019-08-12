package com.ciba.wholefinancial.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.LevelBean;
import com.ciba.wholefinancial.bean.MarketingBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.TypeBean;
import com.ciba.wholefinancial.callback.OnSetViewCallBack;
import com.ciba.wholefinancial.callback.OnUpdateCallBack;
import com.ciba.wholefinancial.datepicker.CustomDatePicker;
import com.ciba.wholefinancial.datepicker.DateFormatUtils;
import com.ciba.wholefinancial.protocol.AddActivityProtocol;
import com.ciba.wholefinancial.protocol.GetLevelListProtocol;
import com.ciba.wholefinancial.protocol.GetLoginListProtocol;
import com.ciba.wholefinancial.protocol.UpdateMarketingProtocol;
import com.ciba.wholefinancial.response.GetLevelListResponse;
import com.ciba.wholefinancial.response.UpdateMarketingResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.FullCountPopuwindow;
import com.ciba.wholefinancial.weiget.LevelDiscountPopuwindow;
import com.ciba.wholefinancial.weiget.MoneySubtractPopuwindow;
import com.ciba.wholefinancial.weiget.SelectShopPopuwindow;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//营销编辑
public class AddMarketActivity extends BaseActivity implements View.OnClickListener, OnSetViewCallBack{

    private LayoutInflater mInflater;
    private View rootView;

    private ImageView iv_top_back;

    private String url;
    private Gson gson;
    private GetLevelListResponse getLevelListResponse;
    private Dialog loadingDialog;
    private EditText et_activity_title, et_remark;
    private TextView tv_endTime, tv_startTime, et_activity_type, tv_levelName;
    private CustomDatePicker mTimerPicker, mEndTimerPicker;
    private String title;
    private String startTime;
    private String endTime;
    private Button btn_save;
    private LinearLayout ll_main;
    private AlertDialog alertDialog;
    private SelectShopPopuwindow selectShopPopuwindow;
    private SelectShopPopuwindow selectLevelPopuwindow;
    private List<String> testData;
    private List<LevelBean> levelBeanList;
    private List<String> levelList;
    private MerchantBean merchantBean;
    //0代表类型，1代表对象
    private int select_type;
    private String levels;
    private String levelName;
    private String remark;
    //（1消费金额满减，2等级优惠，3递增优惠）
    private int type = 1;
    private List<TypeBean> typeJsonArray;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_marketing_add, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        typeJsonArray=new ArrayList<>();
        levelList=new ArrayList<>();
        merchantBean = SPUtils.getBeanFromSp(this, "businessObject", "MerchantBean");
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(AddMarketActivity.this, false);
        ll_main = (LinearLayout) findViewById(R.id.ll_main);
        iv_top_back = (ImageView) findViewById(R.id.iv_top_back);
        tv_startTime = (TextView) findViewById(R.id.tv_startTime);
        tv_levelName = (TextView) findViewById(R.id.tv_levelName);
        tv_endTime = (TextView) findViewById(R.id.tv_endTime);
        et_remark = (EditText) findViewById(R.id.et_remark);
        et_activity_title = (EditText) findViewById(R.id.et_activity_title);
        et_activity_type = (TextView) findViewById(R.id.et_activity_type);
        btn_save = (Button) findViewById(R.id.btn_save);
        et_activity_type.setOnClickListener(this);
        iv_top_back.setOnClickListener(this);
        tv_startTime.setOnClickListener(this);
        tv_endTime.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        tv_levelName.setOnClickListener(this);
        initTimerPicker();
        initEndTimerPicker();
        testData = new ArrayList<>();
        levelBeanList = new ArrayList<>();
        testData.add("消费金额满减");
        testData.add("等级优惠");
        testData.add("递增优惠");
        et_activity_type.setText("请选择活动类型");
    }

    private void initTimerPicker() {
        String beginTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        String endTime = "2028-10-17 18:00";

        tv_startTime.setText(beginTime+":00");

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                tv_startTime.setText(DateFormatUtils.long2Str(timestamp, true) + ":00");
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
        String beginTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        String endTime = "2028-10-17 18:00";

        tv_endTime.setText(beginTime+":00");

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mEndTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                tv_endTime.setText(DateFormatUtils.long2Str(timestamp, true) + ":00");
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
        switch (view.getId()) {
            case R.id.tv_levelName: {
                if (levelBeanList.size() <= 0) {
                    getLevel();
                } else {
                    selectLevelPopuwindow.showPopupWindow(ll_main);
                }
                break;
            }
            case R.id.btn_save: {
                remark = et_remark.getText().toString();
                title = et_activity_title.getText().toString();
                startTime = tv_startTime.getText().toString();
                endTime = tv_endTime.getText().toString();
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(remark) && !TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)&&typeJsonArray.size()>0) {
                    addMarketingInfo();
                } else {
                    if (TextUtils.isEmpty(title)) {
                        DialogUtils.showAlertDialog(AddMarketActivity.this, "标题不能为空！");
                    } else if (TextUtils.isEmpty(startTime)) {
                        DialogUtils.showAlertDialog(AddMarketActivity.this,
                                "开始时间不能为空！");
                    } else if (TextUtils.isEmpty(endTime)) {
                        DialogUtils.showAlertDialog(AddMarketActivity.this,
                                "结束时间不能为空！");
                    }
                    else if(typeJsonArray.size()==0){
                        DialogUtils.showAlertDialog(AddMarketActivity.this,
                                "请先设置好活动类型！");
                    }
                    else {
                        DialogUtils.showAlertDialog(AddMarketActivity.this,
                                "活动设置不能为空！");
                    }

                }
                break;
            }
            case R.id.iv_top_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.tv_startTime: {
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(tv_startTime.getText().toString());
                break;
            }
            case R.id.tv_endTime: {
                // 日期格式为yyyy-MM-dd HH:mm
                mEndTimerPicker.show(tv_endTime.getText().toString());
                break;
            }
            case R.id.btn_delect: {
                LogUtils.e("删除");
                alertDialog = DialogUtils.showAlertDoubleBtnDialog(AddMarketActivity.this, "确认删除？", "删除", 1, AddMarketActivity.this);
                break;
            }
            case R.id.tv_ensure: {
                alertDialog.cancel();
                break;
            }
            case R.id.et_activity_type: {
                if(tv_levelName.getText().toString().equals("请选择用户")){
                    DialogUtils.showAlertDialog(AddMarketActivity.this, "请先选择活动参与对象");
                }else {
                    select_type = 0;
                    selectShopPopuwindow = new SelectShopPopuwindow(UIUtils.getActivity(), "所有活动类型", testData, itemsOnClick);
                    selectShopPopuwindow.showPopupWindow(ll_main);
                }

                break;
            }
        }
    }

    private AdapterView.OnItemClickListener itemsOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (select_type == 0) {
                if (position == 0) {
                    MoneySubtractPopuwindow moneySubtractPopuwindow = new MoneySubtractPopuwindow(AddMarketActivity.this, AddMarketActivity.this);
                    moneySubtractPopuwindow.showPopupWindow(ll_main);
                }else if(position==2){
                    FullCountPopuwindow fullCountPopuwindow = new FullCountPopuwindow(AddMarketActivity.this, AddMarketActivity.this);
                    fullCountPopuwindow.showPopupWindow(ll_main);
                }else{
                    LevelDiscountPopuwindow levelDiscountPopuwindow = new LevelDiscountPopuwindow(AddMarketActivity.this, AddMarketActivity.this,levels,levelName);
                    levelDiscountPopuwindow.showPopupWindow(ll_main);
                }
                type = position + 1;
                String value = testData.get(selectShopPopuwindow.getText());
                et_activity_type.setText(value);
                selectShopPopuwindow.dismissPopupWindow();
            } else {
                LogUtils.e("levels;" + position);
                String value = levelBeanList.get(position).getLevelName();
                levels = String.valueOf(levelBeanList.get(position).getId());
                levelName=value;
                tv_levelName.setText(value);
                selectLevelPopuwindow.dismissPopupWindow();
            }

        }
    };


    private void addMarketingInfo() {
        loadingDialog.show();
        AddActivityProtocol addActivityProtocol = new AddActivityProtocol();
        url = addActivityProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("businessId", String.valueOf(merchantBean.getId()));
        params.put("title", title);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("levels", levels);
        params.put("remark", remark);
        params.put("type", String.valueOf(type));
        params.put("typeJsonArray", gson.toJson(typeJsonArray));
        LogUtils.e("params:"+params.toString());
//        typeJsonArray	活动类型相应的对象数组
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                UpdateMarketingResponse updateMarketingResponse = gson.fromJson(json, UpdateMarketingResponse.class);
                LogUtils.e("updateMarketingResponse:" + updateMarketingResponse.toString());
                if (updateMarketingResponse.getCode() == 0) {
                    SuccessPopuwindow successPopuwindow = new SuccessPopuwindow(AddMarketActivity.this, null, "保存成功");
                    successPopuwindow.showPopupWindow(ll_main);
                } else {
                    if(updateMarketingResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(AddMarketActivity.this,
                                updateMarketingResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(AddMarketActivity.this, updateMarketingResponse.msg);
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AddMarketActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    private void getLevel() {
        loadingDialog.show();
        GetLevelListProtocol getLevelListProtocol = new GetLevelListProtocol();
        url = getLevelListProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("businessId", String.valueOf(merchantBean.getId()));


        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getLevelListResponse = gson.fromJson(json, GetLevelListResponse.class);
                LogUtils.e("getLevelListResponse:" + getLevelListResponse.toString());
                if (getLevelListResponse.getCode() == 0) {

                    for (int i = 0; i < getLevelListResponse.getDataList().size(); i++) {
                        levelBeanList.addAll(getLevelListResponse.getDataList());
                        levelList.add(getLevelListResponse.getDataList().get(i).getLevelName());
                    }
                    select_type = 1;
                    selectLevelPopuwindow = new SelectShopPopuwindow(UIUtils.getActivity(), "活动参与对象", levelList, itemsOnClick);
                    selectLevelPopuwindow.showPopupWindow(ll_main);
                } else {
                    DialogUtils.showAlertDialog(AddMarketActivity.this, getLevelListResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AddMarketActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }


    @Override
    public void CallBackSuccess(int type) {

    }

    @Override
    public void CallBackDate(List<TypeBean> typeBeanList) {
        typeJsonArray.addAll(typeBeanList);
    }
}