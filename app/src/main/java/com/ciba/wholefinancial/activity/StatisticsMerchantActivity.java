package com.ciba.wholefinancial.activity;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.ciba.wholefinancial.tabpager.StatisticsPager;
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

//收款统计
public class StatisticsMerchantActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, OnStatisticsCallBack {

    private LayoutInflater mInflater;
    private View rootView;
    /**
     * 关注标题指示器
     */
    private TabSlidingIndicator titleIndicator;
    /**
     * 关注的内容viewpager
     */
    private NoScrollViewPager vpContent;
    /**
     * 关注标题
     */
    private List<String> pagerTitles;
    /**
     * 存放商场,品牌,喜欢我，我喜欢页面的集合
     */
    private List<ViewTabBasePager> concernBasePagerList;
    //当前选择项
    private int item;
    private String businessShopId;
    private View view_back;
    private Button btn_search;
    private TextView tv_startTime, tv_endTime,tv_totalMoney;
    private CustomDatePicker mTimerPicker, mEndTimerPicker;
    private StatisticsMerchantPager intradayPager;
    private StatisticsMerchantPager weekPager;
    private StatisticsMerchantPager monthPager;
    private TextView tv_count;
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
        rootView = View.inflate(this, R.layout.activity_statistics_merchant, null);
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
        tv_count= (TextView) findViewById(R.id.tv_count);
        tv_title= (TextView) findViewById(R.id.tv_title);
        btn_search = (Button) findViewById(R.id.btn_search);
        view_back = (View) findViewById(R.id.view_back);
        tv_totalMoney= (TextView) findViewById(R.id.tv_totalMoney);
        tv_startTime = (TextView) findViewById(R.id.tv_startTime);
        tv_endTime = (TextView) findViewById(R.id.tv_endTime);
        titleIndicator = (TabSlidingIndicator) findViewById(R.id.indicator_concern_title);
        vpContent = (NoScrollViewPager) findViewById(R.id.vp);
        pagerTitles = new ArrayList<String>();
        pagerTitles.add("当天");
        pagerTitles.add("本周");
        pagerTitles.add("本月");


        concernBasePagerList = new ArrayList<ViewTabBasePager>();
        String startTime=DateUtils.milliToSimpleDateYear(System.currentTimeMillis());
        String endTime=DateUtils.milliToSimpleDateYear(System.currentTimeMillis());
        intradayPager = new StatisticsMerchantPager(this, startTime,endTime,this);
        weekPager = new StatisticsMerchantPager(this,  startTime,endTime,this);
        monthPager= new StatisticsMerchantPager(this,  startTime,endTime,this);

        concernBasePagerList.add(intradayPager);
        concernBasePagerList.add(weekPager);
        concernBasePagerList.add(monthPager);
        initTimerPicker();
        initEndTimerPicker();

        ConcernInfoPagerAdapter concerninfopageradapter = new ConcernInfoPagerAdapter();
        vpContent.setAdapter(concerninfopageradapter);

        titleIndicator.setViewPager(vpContent);
        titleIndicator.setOnPageChangeListener(this);
        // 设置指示器缩小部分的比例
        titleIndicator.setScaleRadio(0.0f);
        // 设置indicator的颜色
        titleIndicator.setTextColor(UIUtils.getColor(R.color.text_color_black),
                UIUtils.getColor(R.color.errorColor));
        view_back.setOnClickListener(this);

        tv_startTime.setText(startTime);
        tv_endTime.setText(endTime);
        tv_startTime.setOnClickListener(this);
        tv_endTime.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        tv_title.setOnClickListener(this);
    }
    private void initTimerPicker() {
        String beginTime = "2019-01-01 00:00";
        String endTime = "2028-10-17 18:00";

        tv_startTime.setText(beginTime+":00");

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                tv_startTime.setText(DateUtils.milliToSimpleDateYear(timestamp));
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(false);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }

    private void initEndTimerPicker() {
        String beginTime = "2019-01-01 00:00";
        String endTime = "2028-10-17 18:00";

        tv_endTime.setText(beginTime+":00");

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mEndTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                tv_endTime.setText(DateUtils.milliToSimpleDateYear(timestamp));
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mEndTimerPicker.setCancelable(true);
        // 显示时和分
        mEndTimerPicker.setCanShowPreciseTime(false);
        // 允许循环滚动
        mEndTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mEndTimerPicker.setCanShowAnim(true);
        vpContent.setCurrentItem(0);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        concernBasePagerList.get(position).initData();
        item=position;
        if(TextUtils.isEmpty(businessShopId)){
            if(item==0){
                intradayPager.searchReceivablesList(tv_startTime.getText().toString(),tv_endTime.getText().toString(),null);
            }else if(item==1){
                String end=DateUtils.milliToSimpleDateYear(System.currentTimeMillis());
                String start=DateUtils.milliToSimpleDateYear(System.currentTimeMillis()-604800*1000L);
                tv_startTime.setText(start);
                tv_endTime.setText(end);
                weekPager.searchReceivablesList(start,end,null);
            }else{
                String end=DateUtils.milliToSimpleDateYear(System.currentTimeMillis());
                String start=end.substring(0,8)+"01";

                tv_startTime.setText(start);
                tv_endTime.setText(end);
                monthPager.searchReceivablesList(start,end,null);
            }
        }else{
            if(item==0){
                intradayPager.searchReceivablesList(tv_startTime.getText().toString(),tv_endTime.getText().toString(),businessShopId);
            }else if(item==1){
                String end=DateUtils.milliToSimpleDateYear(System.currentTimeMillis());
                String start=DateUtils.milliToSimpleDateYear(System.currentTimeMillis()-604800*1000L);
                tv_startTime.setText(start);
                tv_endTime.setText(end);
                weekPager.searchReceivablesList(start,end,businessShopId);
            }else{
                String end=DateUtils.milliToSimpleDateYear(System.currentTimeMillis());
                String start=end.substring(0,8)+"01";

                tv_startTime.setText(start);
                tv_endTime.setText(end);
                monthPager.searchReceivablesList(start,end,businessShopId);
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void CallBackData(String money,String count) {
        tv_totalMoney.setText("$"+money);
        tv_count.setText("共"+count+"笔");
    }

    private class ConcernInfoPagerAdapter extends PagerAdapter {
        @Override
        public CharSequence getPageTitle(int position) {

            return pagerTitles.get(position);
        }

        @Override
        public int getCount() {
            return pagerTitles.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewTabBasePager concernBasePager = concernBasePagerList
                    .get(position);
            View rootView = concernBasePager.getRootView();
            container.removeView(rootView);
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title:{
                if(nameList.size()<=0){
                    getShopList();
                }else{
                    selectshopPopuwindow.showPopupWindow(ll_main);
                }
                break;
            }
            case R.id.btn_search:{
                if(TextUtils.isEmpty(businessShopId)){
                    if(item==0){
                        intradayPager.searchReceivablesList(tv_startTime.getText().toString(),tv_endTime.getText().toString(),null);
                    }else if(item==1){
                        weekPager.searchReceivablesList(tv_startTime.getText().toString(),tv_endTime.getText().toString(),null);
                    }else{
                        monthPager.searchReceivablesList(tv_startTime.getText().toString(),tv_endTime.getText().toString(),null);
                    }
                }else{
                    if(item==0){
                        intradayPager.searchReceivablesList(tv_startTime.getText().toString(),tv_endTime.getText().toString(),businessShopId);
                    }else if(item==1){
                        weekPager.searchReceivablesList(tv_startTime.getText().toString(),tv_endTime.getText().toString(),businessShopId);
                    }else{
                        monthPager.searchReceivablesList(tv_startTime.getText().toString(),tv_endTime.getText().toString(),businessShopId);
                    }
                }

                break;
            }
            case R.id.view_back: {

                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            } case R.id.tv_startTime: {
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(tv_startTime.getText().toString());
                break;
            }
            case R.id.tv_endTime: {
                // 日期格式为yyyy-MM-dd HH:mm
                mEndTimerPicker.show(tv_endTime.getText().toString());
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
                    DialogUtils.showAlertDialog(StatisticsMerchantActivity.this, getBusinessShopListResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(StatisticsMerchantActivity.this, error);

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
            if(item==0){
                intradayPager.searchReceivablesList(tv_startTime.getText().toString(),tv_endTime.getText().toString(),businessShopId);
            }else if(item==1){
                weekPager.searchReceivablesList(tv_startTime.getText().toString(),tv_endTime.getText().toString(),businessShopId);
            }else{
                monthPager.searchReceivablesList(tv_startTime.getText().toString(),tv_endTime.getText().toString(),businessShopId);
            }
            selectshopPopuwindow.dismissPopupWindow();
        }
    };

}
