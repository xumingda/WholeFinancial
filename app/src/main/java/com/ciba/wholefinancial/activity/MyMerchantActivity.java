package com.ciba.wholefinancial.activity;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.callback.OnSetViewCallBack;
import com.ciba.wholefinancial.tabpager.AllMerchantPager;
import com.ciba.wholefinancial.tabpager.AuditMerchantPager;
import com.ciba.wholefinancial.tabpager.NoValidMerchantPager;
import com.ciba.wholefinancial.tabpager.ValidMerchantPager;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.ClockOutPopuwindow;
import com.ciba.wholefinancial.weiget.ClockPopuwindow;
import com.ciba.wholefinancial.weiget.NoScrollViewPager;
import com.ciba.wholefinancial.weiget.SpinnerPopuwindow;
import com.ciba.wholefinancial.weiget.TabSlidingIndicator;


import java.util.ArrayList;
import java.util.List;


public class MyMerchantActivity extends BaseActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {

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

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_merchant, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {

        view_back=(View)findViewById(R.id.view_back);
        titleIndicator=(TabSlidingIndicator)findViewById(R.id.indicator_concern_title);
        vpContent=(NoScrollViewPager)findViewById(R.id.vp);
        pagerTitles = new ArrayList<String>();
        pagerTitles.add("全部商户");
        pagerTitles.add("有效商户");
        pagerTitles.add("无效商户");
        pagerTitles.add("待审核");

        concernBasePagerList = new ArrayList<ViewTabBasePager>();
        allMerchantPager = new AllMerchantPager(this,false);
        ValidMerchantPager validMerchantPager=new ValidMerchantPager(this,false);
        NoValidMerchantPager noValidMerchantPager=new NoValidMerchantPager(this,false);
        AuditMerchantPager auditMerchantPager=new AuditMerchantPager(this,false);
        concernBasePagerList.add(allMerchantPager);
        concernBasePagerList.add(validMerchantPager);
        concernBasePagerList.add(noValidMerchantPager);
        concernBasePagerList.add(auditMerchantPager);

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

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        if(position==0){
//            personalPager.setPageNo(1);
//        }
//        else{
//            manicuristPager.setPageNo(1);
//        }
//        concernBasePagerList.get(position).initData();
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

        }
    }



}
