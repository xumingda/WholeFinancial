package com.ciba.wholefinancial.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.protocol.AddBusinessShopCodeProtocol;
import com.ciba.wholefinancial.protocol.AddShopProtocol;
import com.ciba.wholefinancial.response.AddOrUpdateLoginResponse;
import com.ciba.wholefinancial.response.UpdateMarketingResponse;
import com.ciba.wholefinancial.tabpager.BusinessShopPager;
import com.ciba.wholefinancial.tabpager.PendingOrderPager;
import com.ciba.wholefinancial.tabpager.TableCodePager;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.NoScrollViewPager;
import com.ciba.wholefinancial.weiget.SlideView;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.ciba.wholefinancial.weiget.TabSlidingIndicator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//订单管理
public class TableCodeManagerActivity extends BaseActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {

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
    //0,店铺，1餐桌码
    private int type;

    private View view_back,view_add;
    private BusinessShopPager businessShopPager;
    private TableCodePager tableCodePager;
    private SlideView slideMenu;
    private TextView tv_cancle,tv_save,tv_title,tv_name;
    private EditText et_name;
    private View v_bg;
    private String url;
    private String shopName;
    private Gson gson;
    private Dialog loadingDialog;
    private MerchantBean merchantBean;
    private RelativeLayout rl;
    private String code;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_table_manager, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        merchantBean= SPUtils.getBeanFromSp(this,"businessObject","MerchantBean");
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(TableCodeManagerActivity.this, false);
        et_name=(EditText)findViewById(R.id.et_name);
        rl=(RelativeLayout)findViewById(R.id.rl);
        v_bg=(View)findViewById(R.id.v_bg);
        v_bg.getBackground().setAlpha(80);
        v_bg.setVisibility(View.GONE);
        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_save=(TextView)findViewById(R.id.tv_save);
        tv_cancle=(TextView)findViewById(R.id.tv_cancle);
        tv_title=(TextView)findViewById(R.id.tv_title);
        view_add=(View)findViewById(R.id.view_add);
        view_back=(View)findViewById(R.id.view_back);
        titleIndicator=(TabSlidingIndicator)findViewById(R.id.indicator_concern_title);
        vpContent=(NoScrollViewPager)findViewById(R.id.vp);

        pagerTitles = new ArrayList<String>();
        pagerTitles.add("店铺");
        pagerTitles.add("餐桌码");


        concernBasePagerList = new ArrayList<ViewTabBasePager>();
        businessShopPager = new BusinessShopPager(this,-1);
        tableCodePager=new TableCodePager(this,0);

        concernBasePagerList.add(businessShopPager);
        concernBasePagerList.add(tableCodePager);


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

        slideMenu = (SlideView)findViewById(R.id.slideMenu);
        //点击返回键打开或关闭Menu
        view_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==0){
                    tv_name.setText("店铺名称");
                    tv_title.setText("新增店铺");
                    et_name.setText("");
                    et_name.setHint("请输入店铺名称");
                    et_name.setInputType(InputType.TYPE_CLASS_TEXT);
                    v_bg.setVisibility(View.VISIBLE);
                    slideMenu.switchMenu();
                }else{
                    if(tableCodePager.getBusinessShopId()>0){
                        et_name.setText("");
                        tv_name.setText(tableCodePager.getShop_name());
                        v_bg.setVisibility(View.VISIBLE);
                        slideMenu.switchMenu();
                        et_name.setInputType(InputType.TYPE_CLASS_NUMBER);
                        tv_title.setText("新增餐桌码");
                        et_name.setHint("请输入餐桌码");
                    }else{
                        DialogUtils.showAlertDialog(TableCodeManagerActivity.this, "请先选择商铺！");
                    }

                }
            }
        });

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v_bg.setVisibility(View.GONE);
                slideMenu.switchMenu();

            }
        });
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type==0){
                    shopName=et_name.getText().toString();
                    if(!TextUtils.isEmpty(shopName)){
                        addShop();
                    }else{
                        DialogUtils.showAlertDialog(TableCodeManagerActivity.this, "商铺名不能为空！");
                    }
                }else{
                    code=et_name.getText().toString();
                    if(!TextUtils.isEmpty(code)){
                        addBusinessCode();
                    }else{
                        DialogUtils.showAlertDialog(TableCodeManagerActivity.this, "餐桌码不能为空！");
                    }
                }


            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        type=position;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            businessShopPager.getShopList();
            shopName="";
            code="";
        }else if(requestCode==2){
            tableCodePager.getTableCodeList();
            shopName="";
            code="";
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

    private void addShop() {
        loadingDialog.show();
        AddShopProtocol addShopProtocol=new AddShopProtocol();
        url = addShopProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("businessId",  String.valueOf(merchantBean.getId()));
        params.put("shopName", shopName);

//        typeJsonArray	活动类型相应的对象数组
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                AddOrUpdateLoginResponse addOrUpdateLoginResponse = gson.fromJson(json, AddOrUpdateLoginResponse.class);
                LogUtils.e("addOrUpdateLoginResponse:" + addOrUpdateLoginResponse.toString());
                if (addOrUpdateLoginResponse.getCode() == 0) {
                    v_bg.setVisibility(View.GONE);
                    slideMenu.switchMenu();
                    businessShopPager.getShopList();
                } else {
                    DialogUtils.showAlertDialog(TableCodeManagerActivity.this, addOrUpdateLoginResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(TableCodeManagerActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    private void addBusinessCode() {
        loadingDialog.show();
        AddBusinessShopCodeProtocol addShopProtocol=new AddBusinessShopCodeProtocol();
        url = addShopProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("businessShopId", String.valueOf(tableCodePager.getBusinessShopId()));
        params.put("code", code);

//        typeJsonArray	活动类型相应的对象数组
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                AddOrUpdateLoginResponse addOrUpdateLoginResponse = gson.fromJson(json, AddOrUpdateLoginResponse.class);
                LogUtils.e("addBusinessCodeResponse:" + addOrUpdateLoginResponse.toString());
                if (addOrUpdateLoginResponse.getCode() == 0) {
                    tableCodePager.getTableCodeList();
                    v_bg.setVisibility(View.GONE);
                    slideMenu.switchMenu();
                } else {
                    DialogUtils.showAlertDialog(TableCodeManagerActivity.this, addOrUpdateLoginResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(TableCodeManagerActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

}
