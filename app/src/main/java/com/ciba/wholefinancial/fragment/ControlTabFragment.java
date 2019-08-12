package com.ciba.wholefinancial.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.activity.MerchantSettingActivity;
import com.ciba.wholefinancial.activity.OrderManagerActivity;
import com.ciba.wholefinancial.base.BaseFragment;
import com.ciba.wholefinancial.base.TabBasePager;
import com.ciba.wholefinancial.tabpager.TabHomePager;
import com.ciba.wholefinancial.tabpager.TabOrderPager;
import com.ciba.wholefinancial.tabpager.TabSetPager;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.MyLinearLayout;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

;import java.util.ArrayList;
import java.util.List;

/**
 * @作者: 许明达
 * @创建时间: 2016年5月25日上午10:14:18
 * @描述: 控制侧滑菜单以及 附近 找店 发现 我的四个页面
 */
public class ControlTabFragment extends BaseFragment implements
        OnCheckedChangeListener {

    @ViewInject(R.id.rg_content)
    private RadioGroup mRadioGroup;

    @ViewInject(R.id.rb_content_home)
    private RadioButton rb_content_home;
    @ViewInject(R.id.rb_content_order)
    private RadioButton rb_content_order;
    @ViewInject(R.id.rb_content_set)
    private RadioButton rb_content_set;

    // 内容区域
    @ViewInject(R.id.fl_content_fragment)
    private FrameLayout mFrameLayout;
    // 底部区域
    @ViewInject(R.id.fl_bottom)
    private FrameLayout bFrameLayout;
    @ViewInject(R.id.dl)
    private FrameLayout mDragLayout;

    // 处理事件分发的自定义LinearLayout
    @ViewInject(R.id.my_ll)
    private MyLinearLayout mLinearLayout;

    // 默认选中第0页面
    public static int mCurrentIndex = 0;
    // 中间变量
    public static int temp = 0;
    // 底部页面的集合
    private List<TabBasePager> mPagerList;
    private TabHomePager tabHomePager;
    private TabOrderPager tabOrderPager;
    private TabSetPager tabSetPager;
    /**嗅探闪烁动画*/
    AlphaAnimation snifferflashAnimation = new AlphaAnimation(0.1f, 1.0f);


    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.control_tab, null);
        // Viewutil工具的注入
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    protected void initData() {
        //暂时放这里
        SpeechUtility.createUtility(mActivity, "appid=5cefcc06," + SpeechConstant.FORCE_LOGIN +"=true");

        // 添加实际的页面
        mPagerList = new ArrayList<TabBasePager>();
        tabHomePager = new TabHomePager(mActivity, mDragLayout,
                mLinearLayout,mReceiver);
        mPagerList.add(tabHomePager);

        // 给RadioGroup 设置监听
        getmRadioGroup().setOnCheckedChangeListener(this);

        switchCurrentPage();

    }
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.dessert.mojito.CHANGE_STATUS")) {
                getTabNearByPager().getHome();
            }
        }
    };
    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void setChecked(int item) {
        switch (item) {
            case 0:
                rb_content_home.setChecked(true);
                break;
            case 1:
                rb_content_order.setChecked(true);
                break;
            case 2:
                rb_content_set.setChecked(true);
                break;

            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // 根据选中的RadioButton的id切换页面
        switch (checkedId) {
            case R.id.rb_content_home:

                mCurrentIndex = 0;
                break;
            case R.id.rb_content_order: {
                Intent intent = new Intent(mActivity, OrderManagerActivity.class);
                UIUtils.startActivityForResultNextAnim(intent,1);
                break;
            }
            case R.id.rb_content_set:{
                Intent intent=new Intent(mActivity, MerchantSettingActivity.class);
                UIUtils.startActivityForResultNextAnim(intent,2);
                break;
            }
            default:
                break;
        }
        switchCurrentPage();
    }

    /**
     * 切换RadioGroup对应的页面
     */
    public void switchCurrentPage() {
        mFrameLayout.removeAllViews();

        TabBasePager tabBasePager = mPagerList.get(mCurrentIndex);
        // 获得每个页面对应的布局
        View rootView = tabBasePager.getRootView();
        tabBasePager.initData();
        mFrameLayout.addView(rootView);
    }

    public RadioGroup getmRadioGroup() {
        return mRadioGroup;
    }


    public TabHomePager getTabNearByPager() {
        return tabHomePager;
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
