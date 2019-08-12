package com.ciba.wholefinancial.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.SharedPrefrenceUtils;
import com.ciba.wholefinancial.util.UIUtils;

public class MerchantSettingActivity extends BaseActivity implements
        ViewPager.OnPageChangeListener,View.OnClickListener {


    private LayoutInflater mInflater;
    private View rootView;
    private RelativeLayout rl_update_pwd;
    private RelativeLayout rl_bypass_account,rl_code_management,rl_level_management,rl_good;
    private Button btn_out;
    private AlertDialog alertDialog;
    private View view_back;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_merchant_setting, null);
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
        view_back.setOnClickListener(this);
        rl_good=(RelativeLayout)rootView.findViewById(R.id.rl_good);
        rl_update_pwd=(RelativeLayout)rootView.findViewById(R.id.rl_update_pwd);
        rl_level_management=(RelativeLayout)rootView.findViewById(R.id.rl_level_management);
        rl_bypass_account=(RelativeLayout)rootView.findViewById(R.id.rl_bypass_account);
        rl_code_management=(RelativeLayout)rootView.findViewById(R.id.rl_code_management);
        btn_out=(Button)rootView.findViewById(R.id.btn_out);
        rl_update_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MerchantSettingActivity.this,UpdatePwdActivity.class);
                UIUtils.startActivityNextAnim(intent);
            }
        });
        btn_out.setOnClickListener(this);
        rl_bypass_account.setOnClickListener(this);
        rl_level_management.setOnClickListener(this);
        rl_code_management.setOnClickListener(this);
        rl_good.setOnClickListener(this);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        concernBasePagerList.get(position).initData();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }

            case R.id.rl_good:{
                Intent intent=new Intent(MerchantSettingActivity.this,GoodsManagementActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_level_management:{
                Intent intent=new Intent(MerchantSettingActivity.this,MemberLevelSetListActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_code_management:{
                Intent intent=new Intent(MerchantSettingActivity.this,TableCodeManagerActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }
            case R.id.rl_bypass_account:{
                Intent intent=new Intent(MerchantSettingActivity.this,SubaccountManagementActivity.class);
                UIUtils.startActivityNextAnim(intent);
                break;
            }

            case R.id.btn_out:{
                alertDialog=DialogUtils.showAlertDoubleBtnDialog(MerchantSettingActivity.this,"你真的要退出登录吗？","退出登录",0,MerchantSettingActivity.this);
                break;
            }
            case R.id.tv_ensure:{
                BaseActivity.finishAll();
                SPUtils.saveBean2Sp(MerchantSettingActivity.this,null,"salesmanObject","SalesmanBean");
                SPUtils.saveBean2Sp(MerchantSettingActivity.this,null,"businessObject","MerchantBean");
                SharedPrefrenceUtils.setString(MerchantSettingActivity.this,"token","");
                Intent intent = new Intent(UIUtils.getContext(), LoginActivity.class);
                UIUtils.startActivityNextAnim(intent);
                alertDialog.cancel();
                break;
            }

        }
    }
}
