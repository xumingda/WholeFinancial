package com.ciba.wholefinancial.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.LevelBean;
import com.ciba.wholefinancial.bean.MemberBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.protocol.GetLevelListProtocol;
import com.ciba.wholefinancial.protocol.UpdateMemberLevelProtocol;
import com.ciba.wholefinancial.protocol.UpdateOnlyMemberLevelProtocol;
import com.ciba.wholefinancial.response.GetLevelListResponse;
import com.ciba.wholefinancial.response.UpdateMarketingResponse;
import com.ciba.wholefinancial.tabpager.AllMerchantPager;
import com.ciba.wholefinancial.tabpager.BusinessStatePager;
import com.ciba.wholefinancial.tabpager.MemberBalancePager;
import com.ciba.wholefinancial.tabpager.MemberRechargePager;
import com.ciba.wholefinancial.tabpager.MerchantInfoPager;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.FullCountPopuwindow;
import com.ciba.wholefinancial.weiget.LevelDiscountPopuwindow;
import com.ciba.wholefinancial.weiget.MoneySubtractPopuwindow;
import com.ciba.wholefinancial.weiget.NoScrollViewPager;
import com.ciba.wholefinancial.weiget.SelectShopPopuwindow;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.ciba.wholefinancial.weiget.TabSlidingIndicator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MemberInfoActivity extends BaseActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {

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

    private AllMerchantPager allMerchantPager;
    private View view_back;
    private Dialog loadingDialog;
    private MerchantBean merchantBean;
    private GetLevelListResponse getLevelListResponse;
    private Gson gson;
    private String url;
    private MemberBean memberBean;
    private TextView tv_phone,tv_time,tv_money,tv_leave;
    private SelectShopPopuwindow selectLevelPopuwindow;
    private List<LevelBean> levelBeanList;
    private List<String> levelList;
    private LinearLayout ll_main;
    private String levelName;
    private String businessLevelId;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_member_info, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        merchantBean = SPUtils.getBeanFromSp(this, "businessObject", "MerchantBean");
        levelBeanList = new ArrayList<>();
        levelList=new ArrayList<>();
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(this, true);

        memberBean=(MemberBean)getIntent().getSerializableExtra("MemberBean");
        ll_main=(LinearLayout)findViewById(R.id.ll_main);
        view_back=(View)findViewById(R.id.view_back);
        titleIndicator=(TabSlidingIndicator)findViewById(R.id.indicator_concern_title);
        vpContent=(NoScrollViewPager)findViewById(R.id.vp);
        tv_phone=(TextView)findViewById(R.id.tv_phone);
        tv_leave=(TextView)findViewById(R.id.tv_level);
        tv_time=(TextView)findViewById(R.id.tv_time);
        tv_money=(TextView)findViewById(R.id.tv_money);
        tv_phone.setText("账号/手机："+memberBean.getPhoneNumber());
        tv_time.setText("注册时间："+memberBean.getCreateTime());
        tv_money.setText("账号余额:"+memberBean.getBalance());
        tv_leave.setText("会员等级："+memberBean.getLevelName());
        pagerTitles = new ArrayList<String>();
        pagerTitles.add("会员账户历史");
        pagerTitles.add("会员账户充值");


        concernBasePagerList = new ArrayList<ViewTabBasePager>();
        MemberBalancePager balancePager = new MemberBalancePager(this,String.valueOf(memberBean.getUserId()));
        MemberRechargePager memberRechargePager=new MemberRechargePager(this,String.valueOf(memberBean.getUserId()));
        concernBasePagerList.add(balancePager);
        concernBasePagerList.add(memberRechargePager);


        ConcernInfoPagerAdapter concerninfopageradapter = new ConcernInfoPagerAdapter();
        vpContent.setAdapter(concerninfopageradapter);

        titleIndicator.setViewPager(vpContent);
        titleIndicator.setOnPageChangeListener(this);
        // 设置指示器缩小部分的比例
        titleIndicator.setScaleRadio(0.0f);
        // 设置indicator的颜色
        titleIndicator.setTextColor(UIUtils.getColor(R.color.text_title_gray),
                UIUtils.getColor(R.color.errorColor));
        view_back.setOnClickListener(this);
        tv_leave.setOnClickListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
         concernBasePagerList.get(position).initData();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
        switch (view.getId()){
            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.tv_level: {
                if (levelBeanList.size() <= 0) {
                    getLevel();
                } else {
                    selectLevelPopuwindow.showPopupWindow(ll_main);
                }
                break;
            }
        }
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
                    selectLevelPopuwindow = new SelectShopPopuwindow(UIUtils.getActivity(), "会员等级", levelList, itemsOnClick);
                    selectLevelPopuwindow.showPopupWindow(ll_main);
                } else {
                    DialogUtils.showAlertDialog(MemberInfoActivity.this, getLevelListResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(MemberInfoActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
    private AdapterView.OnItemClickListener itemsOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            LogUtils.e("levels;" + position);
            levelName = levelBeanList.get(position).getLevelName();
            businessLevelId = String.valueOf(levelBeanList.get(position).getId());
            updateMemberLevel();


        }
    };
    //修改会员等级
    private void updateMemberLevel() {
        loadingDialog.show();
        UpdateOnlyMemberLevelProtocol updateMemberLevelProtocol=new UpdateOnlyMemberLevelProtocol();
        url = updateMemberLevelProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId",  String.valueOf(memberBean.getId()));
        params.put("businessLevelId", businessLevelId);


        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                LogUtils.e("updateMarketingResponse:" + json.toString());
                loadingDialog.dismiss();
                UpdateMarketingResponse updateMarketingResponse = gson.fromJson(json, UpdateMarketingResponse.class);
                LogUtils.e("updateMarketingResponse:" + updateMarketingResponse.toString());
                if (updateMarketingResponse.getCode() == 0) {
                    DialogUtils.showAlertDialog(MemberInfoActivity.this, updateMarketingResponse.getMsg());
                    tv_leave.setText("会员等级:"+levelName);
                    selectLevelPopuwindow.dismissPopupWindow();
                } else {
                    selectLevelPopuwindow.dismissPopupWindow();
                    DialogUtils.showAlertDialog(MemberInfoActivity.this, updateMarketingResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                selectLevelPopuwindow.dismissPopupWindow();
                DialogUtils.showAlertDialog(MemberInfoActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {
                selectLevelPopuwindow.dismissPopupWindow();
            }
        });
    }

}
