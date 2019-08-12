package com.ciba.wholefinancial.weiget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.callback.OnSetViewCallBack;

public class SuccessPopuwindow extends PopupWindow {

    private View conentView;

    private Activity context;
    private TextView tv_slogan;
    private TextView tv_commit;
    /**
     * @param context 上下文

     */
    @SuppressLint({"InflateParams", "WrongConstant"})
    public SuccessPopuwindow(final Activity context, final OnSetViewCallBack onSetViewCallBack,String txt) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context =context;
        conentView = inflater.inflate(R.layout.success_layout, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //    this.setWidth(view.getWidth());
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 刷新状态
        this.update();
        this.setOutsideTouchable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
            }
        });

        //解决软键盘挡住弹窗问题
        this.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        //  this.setAnimationStyle(R.style.AnimationPreview);


        tv_slogan = (TextView) conentView.findViewById(R.id.tv_slogan);
        tv_commit = (TextView) conentView.findViewById(R.id.tv_commit);
        TextView tv_clock_title= (TextView) conentView.findViewById(R.id.tv_clock_title);
        tv_clock_title.setText(txt);
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                darkenBackground(1f);
                if(onSetViewCallBack!=null){
                    onSetViewCallBack.CallBackSuccess(1);
                }

            }
        });
    }

    //给下拉列表的设置标题，增加复用性
    public void setTitleText(String str){
        tv_slogan.setText(str);
    }


    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.CENTER, 0, 0);
            darkenBackground(0.4f);//弹出时让页面背景回复给原来的颜色降低透明度，让背景看起来变成灰色
        }
    }



        /**
         * 关闭popupWindow
         */
    public void dismissPopupWindow() {
        this.dismiss();
        darkenBackground(1f);//关闭时让页面背景回复为原来的颜色

    }
    /**
     * 改变背景颜色，主要是在PopupWindow弹出时背景变化，通过透明度设置
     */
    private void darkenBackground(Float bgcolor){
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgcolor;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
}