package com.ciba.wholefinancial.weiget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.activity.LoginActivity;
import com.ciba.wholefinancial.callback.OnRequestCallBack;
import com.ciba.wholefinancial.callback.OnSetViewCallBack;
import com.ciba.wholefinancial.util.DialogUtils;

public class SelectTimePopuwindow extends PopupWindow {

    private View conentView;

    private Activity context;
    private TextView tv_commit;
    private ImageView iv_close;
    private EditText et_start_time;
    private EditText et_end_time;
    /**
     * @param context 上下文

     */
    @SuppressLint({"InflateParams", "WrongConstant"})
    public SelectTimePopuwindow(final Activity context, final OnRequestCallBack onRequestCallBack) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context =context;
        conentView = inflater.inflate(R.layout.select_time_layout, null);
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

        et_start_time= (EditText) conentView.findViewById(R.id.et_start_time);
        et_end_time= (EditText) conentView.findViewById(R.id.et_end_time);
        tv_commit = (TextView) conentView.findViewById(R.id.tv_commit);
        tv_commit.getBackground().setAlpha(87);
        iv_close= (ImageView) conentView.findViewById(R.id.iv_close);
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                darkenBackground(1f);
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                darkenBackground(1f);
            }
        });


        et_start_time.addTextChangedListener(new TextWatcher() {
            private CharSequence temp = null;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (!TextUtils.isEmpty(temp)) {
                    tv_commit.getBackground().setAlpha(255);
                }

            }
        });

        et_end_time.addTextChangedListener(new TextWatcher() {
            private CharSequence temp = null;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (!TextUtils.isEmpty(temp)) {
                    tv_commit.getBackground().setAlpha(255);
                }

            }
        });


        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String startTime = et_start_time.getText().toString();
                String endTime = et_end_time.getText().toString();
                if (!TextUtils.isEmpty(startTime)&&!TextUtils.isEmpty(endTime)&&startTime.length()==8&&endTime.length()==8) {
                    dismiss();
                    darkenBackground(1f);
                    onRequestCallBack.CallBackToRequest(startTime,endTime);
                } else {
                    if (TextUtils.isEmpty(startTime)) {
                        DialogUtils.showAlertDialog(context,
                                "起始时间不能为空！");
                    }else if(TextUtils.isEmpty(endTime)){
                        DialogUtils.showAlertDialog(context,
                                "结束时间不能为空！");
                    }else{
                        DialogUtils.showAlertDialog(context,
                                "请输入正确的时间格式！");
                    }
                }
            }
        });
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