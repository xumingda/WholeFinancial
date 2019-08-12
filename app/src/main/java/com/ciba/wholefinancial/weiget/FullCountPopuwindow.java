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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.adapter.ListsAddAdapter;
import com.ciba.wholefinancial.adapter.ListsDiscountAddAdapter;
import com.ciba.wholefinancial.bean.TypeBean;
import com.ciba.wholefinancial.callback.OnSetViewCallBack;
import com.ciba.wholefinancial.callback.OnUpdateCallBack;
import com.ciba.wholefinancial.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class FullCountPopuwindow extends PopupWindow implements OnUpdateCallBack {

    private View conentView;
    private LinearLayout ll_main;
    private Activity context;
    private Button btn_commit;
    private RelativeLayout rl_add;
    private ListView lv;
    private List<String> list;
    private  List<TypeBean> typeBeanList;
    private ListsDiscountAddAdapter listsAddAdapter;
//    private LinearLayout rl_three;
//    private EditText et_count,et_count_two,et_count_three,et_discount_three,et_discount_two,et_discount,et_time_three,et_time_two,et_time;
    /**
     * @param context 上下文

     */
    @SuppressLint({"InflateParams", "WrongConstant"})
    public FullCountPopuwindow(final Activity context, final OnSetViewCallBack onSetViewCallBack) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context =context;
        conentView = inflater.inflate(R.layout.full_count_layout, null);

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
        rl_add=(RelativeLayout)conentView.findViewById(R.id.rl_add);
        lv=(ListView) conentView.findViewById(R.id.lv);
//        et_time_three=(EditText)conentView.findViewById(R.id.et_time_three) ;
//        et_time_two=(EditText)conentView.findViewById(R.id.et_time_two) ;
//        et_time=(EditText)conentView.findViewById(R.id.et_time) ;
//        et_count=(EditText)conentView.findViewById(R.id.et_count) ;
//        et_count_three=(EditText)conentView.findViewById(R.id.et_count_three) ;
//        et_count_two=(EditText)conentView.findViewById(R.id.et_count_two) ;
//        et_discount=(EditText)conentView.findViewById(R.id.et_discount) ;
//        et_discount_two=(EditText)conentView.findViewById(R.id.et_discount_two) ;
//        et_discount_three=(EditText)conentView.findViewById(R.id.et_discount_three) ;
        btn_commit = (Button) conentView.findViewById(R.id.btn_commit);
//        et_count.addTextChangedListener(new TextWatcher() {
//            private CharSequence temp = null;
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                temp = s;
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//
//            }
//
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//                if (TextUtils.isEmpty(temp)) {
//                    btn_commit.setBackground(UIUtils.getDrawable(R.drawable.shape_gray));
//                    btn_commit.setTextColor(UIUtils.getColor(R.color.text_color_gray));
//                } else {
//                    btn_commit.setBackground(UIUtils.getDrawable(R.drawable.shape_update_btn));
//                    btn_commit.setTextColor(UIUtils.getColor(R.color.tab_background));
//                }
//
//            }
//        });
        typeBeanList=new ArrayList<>();
        list=new ArrayList<>();
        list.add("1");
        list.add("1");
        for (int i=0;i<list.size();i++){
            TypeBean typeBean=new TypeBean();
            typeBeanList.add(typeBean);
        }
        if(listsAddAdapter==null){
            listsAddAdapter=new ListsDiscountAddAdapter(context,list,FullCountPopuwindow.this,typeBeanList);
            lv.setAdapter(listsAddAdapter);
        }else{
            listsAddAdapter.notifyDataSetChanged();
        }
        rl_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.add("1");
                TypeBean typeBean=new TypeBean();
                typeBeanList.add(typeBean);
                listsAddAdapter.notifyDataSetChanged();
            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TypeBean> typeNewBeanList=new ArrayList<>();
                for(int i=0;i<typeBeanList.size();i++){
                    if(!TextUtils.isEmpty(typeBeanList.get(i).getConsumeFullCount())&&!TextUtils.isEmpty(typeBeanList.get(i).getCountDiscount())&&!TextUtils.isEmpty(typeBeanList.get(i).getValidDay())){
                        typeNewBeanList.add(typeBeanList.get(i));
                    }
                }
                if(typeNewBeanList.size()>0){
                    onSetViewCallBack.CallBackDate(typeBeanList);
                    dismiss();
                    darkenBackground(1f);
                }else{
                    Toast.makeText(context,"请输入完整的数据!",Toast.LENGTH_SHORT).show();
                }
//                List<TypeBean> typeBeanList=new ArrayList<>();
//                String consumeFullCount=et_count.getText().toString();
//                String countDiscount=et_discount.getText().toString();
//                String validDay=et_time.getText().toString();
//                String consumeFullCount_two=et_count_two.getText().toString();
//                String countDiscount_two=et_discount_two.getText().toString();
//                String validDay_two=et_time_two.getText().toString();
//                String consumeFullCount_three=et_count_three.getText().toString();
//                String countDiscount_three=et_discount_three.getText().toString();
//                String validDay_three=et_time_three.getText().toString();
//                if((consumeFullCount.length()>0&&countDiscount.length()>0)||(consumeFullCount_two.length()>0&&countDiscount_two.length()>0)||(consumeFullCount_three.length()>0&&countDiscount_three.length()>0)){
//                    if(consumeFullCount.length()>0&&countDiscount.length()>0){
//                        TypeBean typeBean=new TypeBean();
//                        typeBean.setConsumeFullCount(consumeFullCount);
//                        typeBean.setCountDiscount(countDiscount);
//                        typeBean.setValidDay(validDay);
//                        typeBeanList.add(typeBean);
//                    }
//                    if(consumeFullCount_two.length()>0&&countDiscount_two.length()>0){
//                        TypeBean typeBean=new TypeBean();
//                        typeBean.setConsumeFullCount(consumeFullCount_two);
//                        typeBean.setCountDiscount(countDiscount_two);
//                        typeBean.setValidDay(validDay_two);
//                        typeBeanList.add(typeBean);
//                    }
//                    if(consumeFullCount_three.length()>0&&consumeFullCount_three.length()>0) {
//                        TypeBean typeBean=new TypeBean();
//                        typeBean.setConsumeFullCount(consumeFullCount_three);
//                        typeBean.setCountDiscount(countDiscount_three);
//                        typeBean.setValidDay(validDay_three);
//                        typeBeanList.add(typeBean);
//                    }
//                    onSetViewCallBack.CallBackDate(typeBeanList);
//                    dismiss();
//                    darkenBackground(1f);
//                }
//                else{
//                    Toast.makeText(context,"请输入完整的数据!",Toast.LENGTH_SHORT).show();
//                }

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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void update(int type) {
        if (type==0) {
            btn_commit.setBackground(UIUtils.getDrawable(R.drawable.shape_gray));
            btn_commit.setTextColor(UIUtils.getColor(R.color.text_color_gray));
        } else {
            btn_commit.setBackground(UIUtils.getDrawable(R.drawable.shape_update_btn));
            btn_commit.setTextColor(UIUtils.getColor(R.color.tab_background));
        }
    }


}