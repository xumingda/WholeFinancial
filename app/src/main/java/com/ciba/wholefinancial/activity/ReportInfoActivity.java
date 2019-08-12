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
import android.widget.ImageView;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.ReportBean;
import com.ciba.wholefinancial.tabpager.AllMerchantPager;
import com.ciba.wholefinancial.tabpager.BusinessStatePager;
import com.ciba.wholefinancial.tabpager.MerchantInfoPager;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.PictureOption;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.NoScrollViewPager;
import com.ciba.wholefinancial.weiget.TabSlidingIndicator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class ReportInfoActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;

    private View view_back;
    private int businessId;
    private TextView tv_title;
    private TextView tv_content;
    private ImageView iv_one;
    private ImageView iv_two;
    private ImageView iv_three;
    private ImageLoader imageLoader;
    private ReportBean reportBean;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_report_info, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        reportBean=(ReportBean) getIntent().getSerializableExtra("reportList");

        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(this)));
        businessId=getIntent().getIntExtra("businessId",0);
        LogUtils.e("businessId:"+businessId);
        view_back=(View)findViewById(R.id.view_back);
        tv_title=(TextView) findViewById(R.id.tv_title);
        tv_content=(TextView) findViewById(R.id.tv_content);
        iv_one=(ImageView)findViewById(R.id.iv_one);
        iv_three=(ImageView)findViewById(R.id.iv_three);
        iv_two=(ImageView)findViewById(R.id.iv_two);
        tv_title.setText("举报主题:"+reportBean.getTitle());
        tv_content.setText(reportBean.getContent());
        if(reportBean.getPics().indexOf("[")!=-1&&reportBean.getPics().length()>5){
            char delChar = '[';
            String temp=deleteString(reportBean.getPics(),delChar);
            char delChar_two = ']';
            String last=deleteString(temp,delChar_two);
            switch (convertStrToArray(last).length){
                case 1:{
                    imageLoader.displayImage(convertStrToArray(last)[0].substring(1,convertStrToArray(last)[0].length()-1), iv_one, PictureOption.getSimpleOptions());
                    break;
                }
                case 2:{
                    imageLoader.displayImage(convertStrToArray(last)[0].substring(1,convertStrToArray(last)[0].length()-1), iv_one, PictureOption.getSimpleOptions());
                    imageLoader.displayImage(convertStrToArray(last)[1].substring(1,convertStrToArray(last)[1].length()-1), iv_two, PictureOption.getSimpleOptions());
                    break;
                }
                case 3:{
                    imageLoader.displayImage(convertStrToArray(last)[0].substring(1,convertStrToArray(last)[0].length()-1), iv_one, PictureOption.getSimpleOptions());
                    imageLoader.displayImage(convertStrToArray(last)[1].substring(1,convertStrToArray(last)[1].length()-1), iv_two, PictureOption.getSimpleOptions());
                    imageLoader.displayImage(convertStrToArray(last)[2].substring(1,convertStrToArray(last)[2].length()-1), iv_three, PictureOption.getSimpleOptions());
                    break;
                }
            }
            LogUtils.e("reportBean:"+convertStrToArray(last)[0].substring(1,convertStrToArray(last)[0].length()-1));
        }
        view_back.setOnClickListener(this);
    }

    public static String[] convertStrToArray(String str){
        String[] strArray = null;
        strArray = str.split(","); //拆分字符为"," ,然后把结果交给数组strArray
        return strArray;
    }
    public static String deleteString(String str, char delChar){
        String delStr = "";
        for (int i = 0; i < str.length(); i++) {
            if(str.charAt(i) != delChar){
                delStr += str.charAt(i);
            }
        }
        return delStr;
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
