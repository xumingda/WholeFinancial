package com.ciba.wholefinancial.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.protocol.GetMerchantInfoProtocol;
import com.ciba.wholefinancial.protocol.GetMerchantProtocol;
import com.ciba.wholefinancial.response.GetMerchantInfoResponse;
import com.ciba.wholefinancial.response.GetMerchantListResponse;
import com.ciba.wholefinancial.tabpager.AllMerchantPager;
import com.ciba.wholefinancial.tabpager.AuditMerchantPager;
import com.ciba.wholefinancial.tabpager.BusinessStatePager;
import com.ciba.wholefinancial.tabpager.MerchantInfoPager;
import com.ciba.wholefinancial.tabpager.NoValidMerchantPager;
import com.ciba.wholefinancial.tabpager.ValidMerchantPager;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.NoScrollViewPager;
import com.ciba.wholefinancial.weiget.TabSlidingIndicator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MerchantInfoActivity extends BaseActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {

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
    private int businessId;
    private Dialog loadingDialog;
    private Gson gson;
    private View view_update;
    private    MerchantInfoPager merchantInfoPager;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_merchant_info, null);
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
        businessId=getIntent().getIntExtra("businessId",0);
        LogUtils.e("businessId:"+businessId);
        view_back=(View)findViewById(R.id.view_back);
        view_update=(View)findViewById(R.id.view_update);
        titleIndicator=(TabSlidingIndicator)findViewById(R.id.indicator_concern_title);
        vpContent=(NoScrollViewPager)findViewById(R.id.vp);
        pagerTitles = new ArrayList<String>();
        pagerTitles.add("资料");
        pagerTitles.add("经营状况");
        view_back.setOnClickListener(this);
        view_update.setOnClickListener(this);
        concernBasePagerList = new ArrayList<ViewTabBasePager>();
        merchantInfoPager = new MerchantInfoPager(this,false,businessId);
        BusinessStatePager businessStatePager=new BusinessStatePager(this,false,businessId);
        concernBasePagerList.add(merchantInfoPager);
        concernBasePagerList.add(businessStatePager);


        ConcernInfoPagerAdapter concerninfopageradapter = new ConcernInfoPagerAdapter();
        vpContent.setAdapter(concerninfopageradapter);

        titleIndicator.setViewPager(vpContent);
        titleIndicator.setOnPageChangeListener(this);
        // 设置指示器缩小部分的比例
        titleIndicator.setScaleRadio(0.0f);
        // 设置indicator的颜色
        titleIndicator.setTextColor(UIUtils.getColor(R.color.text_color_black),
                UIUtils.getColor(R.color.errorColor));

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
            case R.id.view_update:{
                Intent intent=new Intent(this,AddMerchantInfoActivity.class);
                intent.putExtra("businessId",businessId);
                UIUtils.startActivityForResultNextAnim(intent,100);
                break;
            }
            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            merchantInfoPager.getMerchantInfo();
        }
    }
}
