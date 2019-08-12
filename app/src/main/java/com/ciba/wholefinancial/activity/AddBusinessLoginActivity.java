package com.ciba.wholefinancial.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.protocol.AddOrUpdateLoginProtocol;
import com.ciba.wholefinancial.protocol.GetCodeProtocol;
import com.ciba.wholefinancial.protocol.ModifyLoginPwdProtocol;
import com.ciba.wholefinancial.request.GetCodeRequest;
import com.ciba.wholefinancial.response.AddOrUpdateLoginResponse;
import com.ciba.wholefinancial.response.GetCodeResponse;
import com.ciba.wholefinancial.response.ModifyLoginPwdResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.MD5Utils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.regex.Pattern;

public class AddBusinessLoginActivity extends BaseActivity {

    private ImageView iv_top_back;
    private TextView mTextMessage;
    private LayoutInflater mInflater;
    private View rootView;
    private EditText et_new_pwd;
    private EditText et_new_pwd_again,et_name,et_phone;
    private Button btn_submit;
//    private RelativeLayout rl_clear_new_pwd;
//    private RelativeLayout rl_clear_new_pwd_again;
//    private TextView tv_get_code;
    private Dialog loadingDialog;
    private String new_password;
    private String ensure_password;
    private MerchantBean merchantBean;
    private String name;
    private String account;
    private RelativeLayout rl_main;
    private View view_back;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_add_login, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initDate(){
        merchantBean= SPUtils.getBeanFromSp(this,"businessObject","MerchantBean");
        loadingDialog = DialogUtils.createLoadDialog(AddBusinessLoginActivity.this, false);
        view_back=(View) findViewById(R.id.view_back);
        et_name=(EditText)findViewById(R.id.et_name);
        et_phone=(EditText)findViewById(R.id.et_phone);
        et_new_pwd=(EditText)findViewById(R.id.et_pwd);
        et_new_pwd_again=(EditText)findViewById(R.id.et_pwd_again);
        btn_submit=(Button) findViewById(R.id.btn_submit);
//        rl_clear_new_pwd=(RelativeLayout)findViewById(R.id.rl_clear_new_pwd);
//        rl_clear_new_pwd_again=(RelativeLayout)findViewById(R.id.rl_clear_new_pwd_again);
        rl_main=(RelativeLayout)findViewById(R.id.rl_main);
        btn_submit.setBackground(UIUtils.getDrawable(R.drawable.shape_gray));
        btn_submit.setTextColor(UIUtils.getColor(R.color.text_color_gray));


        // 清除
//        rl_clear_new_pwd.setVisibility(View.INVISIBLE);
//        rl_clear_new_pwd.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                et_new_pwd.setText("");
//            }
//        });

//        rl_clear_new_pwd_again.setVisibility(View.INVISIBLE);
//        rl_clear_new_pwd_again.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                et_new_pwd_again.setText("");
//            }
//        });
        et_phone.addTextChangedListener(new TextWatcher() {
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
                    btn_submit.setBackground(UIUtils.getDrawable(R.drawable.login_button_selector));
                    btn_submit.setTextColor(UIUtils.getColor(R.color.tab_background));
                }

            }
        });
        et_name.addTextChangedListener(new TextWatcher() {
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
                    btn_submit.setBackground(UIUtils.getDrawable(R.drawable.login_button_selector));
                    btn_submit.setTextColor(UIUtils.getColor(R.color.tab_background));
                }

            }
        });
        et_new_pwd.addTextChangedListener(new TextWatcher() {
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
                    btn_submit.setBackground(UIUtils.getDrawable(R.drawable.login_button_selector));
                    btn_submit.setTextColor(UIUtils.getColor(R.color.tab_background));
                }

            }
        });

//        et_new_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {// do job here owhen Edittext lose focus
//                    rl_clear_new_pwd.setVisibility(View.INVISIBLE);
//                } else {
//                    if (!TextUtils.isEmpty(et_new_pwd.getText().toString()))
//                        rl_clear_new_pwd.setVisibility(View.VISIBLE);
//                }
//
//            }
//        });

        et_new_pwd_again.addTextChangedListener(new TextWatcher() {
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
                    btn_submit.setBackground(UIUtils.getDrawable(R.drawable.login_button_selector));
                    btn_submit.setTextColor(UIUtils.getColor(R.color.tab_background));
                }

            }
        });

//        et_new_pwd_again.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {// do job here owhen Edittext lose focus
//                    rl_clear_new_pwd_again.setVisibility(View.INVISIBLE);
//                } else {
//                    if (!TextUtils.isEmpty(et_new_pwd_again.getText().toString()))
//                        rl_clear_new_pwd_again.setVisibility(View.VISIBLE);
//                }
//
//            }
//        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();
                account=et_phone.getText().toString();
                new_password = et_new_pwd.getText().toString();
                ensure_password= et_new_pwd_again.getText().toString();
                if(!TextUtils.isEmpty(new_password)&&!TextUtils.isEmpty(ensure_password)&&!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(account)){
                    boolean b= Pattern.compile("[a-zA-Z]").matcher(new_password).find();
                    if(new_password.equals(ensure_password)&&new_password.length()>5){
                        addLogin();
                    }
                    else{
                        if(!new_password.equals(ensure_password)){
                            DialogUtils.showAlertDialog(AddBusinessLoginActivity.this,
                                    "请输入一样的密码！");
                        }
//                        else if (!b) {
//                            DialogUtils.showAlertDialog(UpdatePwdActivity.this,
//                                    "请输入数字加字母的组合密码！");
//                        }
                        else if(new_password.length()<6||ensure_password.length()<6){
                            DialogUtils.showAlertDialog(AddBusinessLoginActivity.this,
                                    "请至少输入6位数密码！");
                        }
                    }
                }
                else{
                    if(TextUtils.isEmpty(new_password)){
                        DialogUtils.showAlertDialog(AddBusinessLoginActivity.this,
                                "新密码不能为空！");
                    }
                    else if(TextUtils.isEmpty(name)){
                        DialogUtils.showAlertDialog(AddBusinessLoginActivity.this,
                                "姓名不能为空！");
                    }else if(TextUtils.isEmpty(account)){
                        DialogUtils.showAlertDialog(AddBusinessLoginActivity.this,
                                "手机号不能为空！");
                    }
                    else {
                        DialogUtils.showAlertDialog(AddBusinessLoginActivity.this,
                                "重复密码不能为空！");
                    }

                }
            }
        });
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });

    }





    public void addLogin() {
        loadingDialog.show();
        AddOrUpdateLoginProtocol addOrUpdateLoginProtocol = new AddOrUpdateLoginProtocol();

        String url = addOrUpdateLoginProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("account", account);
        params.put("businessId",  String.valueOf(merchantBean.getId()));
        params.put("password",  MD5Utils.MD5(new_password));
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                AddOrUpdateLoginResponse addOrUpdateLoginResponse = gson.fromJson(json, AddOrUpdateLoginResponse.class);
                LogUtils.e("addOrUpdateLoginResponse:" + addOrUpdateLoginResponse.toString());
                if (addOrUpdateLoginResponse.code==0) {
                    SuccessPopuwindow clockPopuwindow=new SuccessPopuwindow(AddBusinessLoginActivity.this,null,"保存成功");
                    clockPopuwindow.showPopupWindow(rl_main);
                    loadingDialog.dismiss();
//                    finish();
//                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                } else {

                    loadingDialog.dismiss();
                    if(addOrUpdateLoginResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(AddBusinessLoginActivity.this,
                                addOrUpdateLoginResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(AddBusinessLoginActivity.this, addOrUpdateLoginResponse.msg);
                    }
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AddBusinessLoginActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(AddBusinessLoginActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }
}
