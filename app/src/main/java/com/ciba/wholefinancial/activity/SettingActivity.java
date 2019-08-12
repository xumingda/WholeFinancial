package com.ciba.wholefinancial.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.jspush.TagAliasOperatorHelper;
import com.ciba.wholefinancial.tabpager.MyReportPager;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.SharedPrefrenceUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.NoScrollViewPager;
import com.ciba.wholefinancial.weiget.TabSlidingIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.ciba.wholefinancial.jspush.TagAliasOperatorHelper.ACTION_CLEAN;
import static com.ciba.wholefinancial.jspush.TagAliasOperatorHelper.ACTION_DELETE;
import static com.ciba.wholefinancial.jspush.TagAliasOperatorHelper.ACTION_SET;
import static com.ciba.wholefinancial.jspush.TagAliasOperatorHelper.sequence;

public class SettingActivity extends BaseActivity implements
        ViewPager.OnPageChangeListener,View.OnClickListener {


    private LayoutInflater mInflater;
    private View rootView;
    private RelativeLayout rl_update_pwd;
    private RelativeLayout rl_out;
    private AlertDialog alertDialog;
    private View view_back;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_setting, null);
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
        rl_update_pwd=(RelativeLayout)rootView.findViewById(R.id.rl_update_pwd);
        rl_out=(RelativeLayout)rootView.findViewById(R.id.rl_out);
        rl_update_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this,UpdatePwdActivity.class);
                UIUtils.startActivityNextAnim(intent);
            }
        });
        rl_out.setOnClickListener(this);
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
            case R.id.tv_ensure:{
                BaseActivity.finishAll();
                deleteAlias();
                SPUtils.saveBean2Sp(SettingActivity.this,null,"salesmanObject","SalesmanBean");
                SPUtils.saveBean2Sp(SettingActivity.this,null,"businessObject","MerchantBean");
                SharedPrefrenceUtils.setString(SettingActivity.this,"userphone","");
                SharedPrefrenceUtils.setString(SettingActivity.this,"token","");
                Intent intent = new Intent(UIUtils.getContext(), LoginActivity.class);
                UIUtils.startActivityNextAnim(intent);
                alertDialog.cancel();
                break;
            }

            case R.id.rl_out:{
                alertDialog=DialogUtils.showAlertDoubleBtnDialog(SettingActivity.this,"你真的要退出登录吗？","退出登录",0,SettingActivity.this);
                break;
            }


        }
    }
    /**
     * 删除标签与别名
     */
    private void deleteAlias() {
        /**
         *这里设置了别名，在这里获取的用户登录的信息
         *并且此时已经获取了用户的userId,然后就可以用用户的userId来设置别名了*/
        //上下文、别名【Sting行】、标签【Set型】、回调
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = ACTION_DELETE;
        tagAliasBean.alias = SharedPrefrenceUtils.getString( SettingActivity.this,"alias");
        sequence++;
        tagAliasBean.isAliasAction = true;
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(),sequence,tagAliasBean);
    }
}
