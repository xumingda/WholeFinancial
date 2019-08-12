package com.ciba.wholefinancial.weiget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.TypeBean;
import com.ciba.wholefinancial.callback.OnSetViewCallBack;
import com.ciba.wholefinancial.util.UIUtils;

import java.util.ArrayList;
import java.util.List;
//等级优惠
public class LevelDiscountPopuwindow extends PopupWindow {

    private View conentView;
    private LinearLayout ll_main;
    private Activity context;
    private Button btn_commit;
    private EditText et_discount;
    private TextView tv_levelName;
    private String businessLevelId;
    private String levelName;
    /**
     * @param context 上下文

     */
    @SuppressLint({"InflateParams", "WrongConstant"})
    public LevelDiscountPopuwindow(final Activity context, final OnSetViewCallBack onSetViewCallBack, final String businessLevelId,
                                   String levelName) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.businessLevelId=businessLevelId;
        this.levelName=levelName;
        this.context =context;
        conentView = inflater.inflate(R.layout.level_discount_layout, null);

        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //    this.setWidth(view.getWidth());
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
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
        ll_main=(LinearLayout) conentView.findViewById(R.id.ll_main);

        et_discount=(EditText)conentView.findViewById(R.id.et_discount) ;
        tv_levelName=(TextView) conentView.findViewById(R.id.tv_levelName) ;
        btn_commit = (Button) conentView.findViewById(R.id.btn_commit);
        tv_levelName.setText(levelName+":");
        et_discount.addTextChangedListener(new TextWatcher() {
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

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(temp)) {
                    btn_commit.setBackground(UIUtils.getDrawable(R.drawable.shape_gray));
                    btn_commit.setTextColor(UIUtils.getColor(R.color.text_color_gray));
                } else {
                    btn_commit.setBackground(UIUtils.getDrawable(R.drawable.shape_update_btn));
                    btn_commit.setTextColor(UIUtils.getColor(R.color.tab_background));
                }

            }
        });

        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<TypeBean> typeBeanList=new ArrayList<>();
                String levelDiscount=et_discount.getText().toString();

                if(levelDiscount.length()>0){
                    TypeBean typeBean=new TypeBean();
                    typeBean.setLevelDiscount(levelDiscount);
                    typeBean.setBusinessLevelId(businessLevelId);
                    typeBeanList.add(typeBean);
                    onSetViewCallBack.CallBackDate(typeBeanList);
                    dismiss();
                    darkenBackground(1f);
                }
                else{
                    Toast.makeText(context,"请输入完整的数据!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPopupWindow();
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