package com.ciba.wholefinancial.tabpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.TabBasePager;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.MyLinearLayout;
import com.lidroid.xutils.ViewUtils;

/**
 * @作者: 许明达
 * @创建时间: 2016年3月23日上午11:10:20
 * @版权: 特速版权所有
 * @描述: TODO
 */
public class TabOrderPager extends TabBasePager implements View.OnClickListener {


    RelativeLayout view;
    LayoutInflater mInflater;
    private FrameLayout mDragLayout;
    private MyLinearLayout mLinearLayout;


    /**
     * @param context
     */
    public TabOrderPager(Context context, FrameLayout mDragLayout,
                         MyLinearLayout mLinearLayout) {
        super(context, mDragLayout);
        this.mDragLayout = mDragLayout;
        this.mLinearLayout = mLinearLayout;

    }


    @Override
    protected View initView() {
        view = (RelativeLayout) View.inflate(mContext, R.layout.home_pager, null);
        ViewUtils.inject(this, view);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }
        return view;
    }

    public void initData() {


    }








    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }




}
