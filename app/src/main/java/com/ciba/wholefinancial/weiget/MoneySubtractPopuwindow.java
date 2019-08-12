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
import android.widget.TextView;
import android.widget.Toast;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.adapter.ListsAddAdapter;
import com.ciba.wholefinancial.bean.TypeBean;
import com.ciba.wholefinancial.callback.OnSetViewCallBack;
import com.ciba.wholefinancial.callback.OnUpdateCallBack;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.UIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MoneySubtractPopuwindow extends PopupWindow implements OnUpdateCallBack {

    private View conentView;
    private LinearLayout ll_main;
    private Activity context;
    private Button btn_commit;
    private List<String> list;
    private  List<TypeBean> typeBeanList;
    private RelativeLayout rl_add;
    ListsAddAdapter listsAddAdapter;
//    private RelativeLayout rl_three;
    private ListView lv;
//    private EditText et_money,et_money_two,et_money_three,et_subtract_three,et_subtract_two,et_subtract;
    /**
     * @param context 上下文

     */
    @SuppressLint({"InflateParams", "WrongConstant"})
    public MoneySubtractPopuwindow(final Activity context, final OnSetViewCallBack onSetViewCallBack) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context =context;
        conentView = inflater.inflate(R.layout.money_subtract_layout, null);

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
//        rl_three=(RelativeLayout)conentView.findViewById(R.id.rl_three);
//        et_money=(EditText)conentView.findViewById(R.id.et_money) ;
//        et_money_three=(EditText)conentView.findViewById(R.id.et_money_three) ;
//        et_money_two=(EditText)conentView.findViewById(R.id.et_money_two) ;
//        et_subtract=(EditText)conentView.findViewById(R.id.et_subtract) ;
//        et_subtract_two=(EditText)conentView.findViewById(R.id.et_subtract_two) ;
//        et_subtract_three=(EditText)conentView.findViewById(R.id.et_subtract_three) ;
        lv=(ListView) conentView.findViewById(R.id.lv);
        btn_commit = (Button) conentView.findViewById(R.id.btn_commit);
        typeBeanList=new ArrayList<>();
        list=new ArrayList<>();
        list.add("1");
        list.add("1");
        for (int i=0;i<list.size();i++){
            TypeBean typeBean=new TypeBean();
            typeBeanList.add(typeBean);
        }
        if(listsAddAdapter==null){
            listsAddAdapter=new ListsAddAdapter(context,list,MoneySubtractPopuwindow.this,typeBeanList);
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
//                if(rl_three.getVisibility()==0){
//                    Toast.makeText(context,"只能添加3个",Toast.LENGTH_SHORT).show();
//                }else{
//                    rl_three.setVisibility(View.VISIBLE);
//                }
            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<TypeBean> typeNewBeanList=new ArrayList<>();
                for(int i=0;i<typeBeanList.size();i++){
                    if(!TextUtils.isEmpty(typeBeanList.get(i).getSubMoney())&&!TextUtils.isEmpty(typeBeanList.get(i).getConsumeFullMoney())){
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
//                String consumeFullMoney=et_money.getText().toString();
//                String subMoney=et_subtract.getText().toString();
//                String consumeFullMoney_two=et_money_two.getText().toString();
//                String subMoney_two=et_subtract_two.getText().toString();
//                String consumeFullMoney_three=et_money_three.getText().toString();
//                String subMoney_three=et_subtract_three.getText().toString();
//                if((consumeFullMoney.length()>0&&subMoney.length()>0)||(consumeFullMoney_two.length()>0&&subMoney_two.length()>0)||(consumeFullMoney_three.length()>0&&subMoney_three.length()>0)){
//                    if(consumeFullMoney.length()>0&&subMoney.length()>0){
//                        TypeBean typeBean=new TypeBean();
//                        typeBean.setConsumeFullMoney(consumeFullMoney);
//                        typeBean.setSubMoney(subMoney);
//                        typeBeanList.add(typeBean);
//                    }
//                    if(consumeFullMoney_two.length()>0&&subMoney_two.length()>0){
//                        TypeBean typeBean=new TypeBean();
//                        typeBean.setConsumeFullMoney(consumeFullMoney_two);
//                        typeBean.setSubMoney(subMoney_two);
//                        typeBeanList.add(typeBean);
//                    }
//                    if(consumeFullMoney_three.length()>0&&consumeFullMoney_three.length()>0) {
//                        TypeBean typeBean=new TypeBean();
//                        typeBean.setConsumeFullMoney(consumeFullMoney_three);
//                        typeBean.setSubMoney(subMoney_three);
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