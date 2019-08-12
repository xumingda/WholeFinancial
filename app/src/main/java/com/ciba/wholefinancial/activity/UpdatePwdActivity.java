package com.ciba.wholefinancial.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ciba.wholefinancial.bean.TypeBean;
import com.ciba.wholefinancial.callback.OnSetViewCallBack;
import com.ciba.wholefinancial.protocol.GetCodeProtocol;
import com.ciba.wholefinancial.protocol.ModifyLoginPwdProtocol;
import com.ciba.wholefinancial.request.GetCodeRequest;
import com.ciba.wholefinancial.response.GetCodeResponse;
import com.ciba.wholefinancial.response.ModifyLoginPwdResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.MD5Utils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.SharedPrefrenceUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class UpdatePwdActivity extends BaseActivity implements OnSetViewCallBack {

    private TextView mTextMessage;
    private LayoutInflater mInflater;
    private View rootView;
    private EditText et_code;
    private EditText et_new_pwd;
    private EditText et_new_pwd_again,et_phone;
    private Button btn_submit;
    private RelativeLayout rl_clear_new_pwd;
    private RelativeLayout rl_clear_new_pwd_again,rl_clear_phone;
    private TextView tv_get_code;
    private Dialog loadingDialog;
    private int time = 60;
    private String new_password;
    private String ensure_password;
    private SalesmanBean salesmanBean;
    private MerchantBean merchantBean;
    private String code;
    private String phoneNumber;
    private View view_back;
    private RelativeLayout rl_main;
    private TextView tv_title;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_update_pwd, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    public void initDate(){

        merchantBean= SPUtils.getBeanFromSp(this,"businessObject","MerchantBean");
        salesmanBean= SPUtils.getBeanFromSp(this,"salesmanObject","SalesmanBean");
        loadingDialog = DialogUtils.createLoadDialog(UpdatePwdActivity.this, false);
        tv_get_code=(TextView) findViewById(R.id.tv_get_code);
        tv_title=(TextView) findViewById(R.id.tv_title);
        et_phone=(EditText) findViewById(R.id.et_phone);
        view_back=(View)findViewById(R.id.view_back);
        et_code=(EditText)findViewById(R.id.et_code);
        et_new_pwd=(EditText)findViewById(R.id.et_new_pwd);
        et_new_pwd_again=(EditText)findViewById(R.id.et_new_pwd_again);
        btn_submit=(Button) findViewById(R.id.btn_submit);
        rl_clear_phone=(RelativeLayout)findViewById(R.id.rl_clear_phone);
        rl_clear_new_pwd=(RelativeLayout)findViewById(R.id.rl_clear_new_pwd);
        rl_clear_new_pwd_again=(RelativeLayout)findViewById(R.id.rl_clear_new_pwd_again);
        rl_main=(RelativeLayout)findViewById(R.id.rl_main);
        if(!TextUtils.isEmpty(getIntent().getStringExtra("title"))){
            tv_title.setText(getIntent().getStringExtra("title"));
        }
        btn_submit.getBackground().setAlpha(87);
        et_phone.setText(SharedPrefrenceUtils.getString(this,"userphone"));
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });
        // 清除
        rl_clear_new_pwd.setVisibility(View.INVISIBLE);
        rl_clear_new_pwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                et_new_pwd.setText("");
            }
        });

        rl_clear_new_pwd_again.setVisibility(View.INVISIBLE);
        rl_clear_new_pwd_again.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                et_new_pwd_again.setText("");
            }
        });
        rl_clear_phone.setVisibility(View.INVISIBLE);
        rl_clear_phone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                et_phone.setText("");
            }
        });
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
                if (TextUtils.isEmpty(temp)) {
                    rl_clear_phone.setVisibility(View.INVISIBLE);
                } else {
                    rl_clear_phone.setVisibility(View.VISIBLE);
                    btn_submit.getBackground().setAlpha(255);
                }

            }
        });

        et_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {// do job here owhen Edittext lose focus
                    rl_clear_phone.setVisibility(View.INVISIBLE);
                } else {
                    if (!TextUtils.isEmpty(et_phone.getText().toString()))
                        rl_clear_phone.setVisibility(View.VISIBLE);
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
                if (TextUtils.isEmpty(temp)) {
                    rl_clear_new_pwd.setVisibility(View.INVISIBLE);
                } else {
                    rl_clear_new_pwd.setVisibility(View.VISIBLE);
                    btn_submit.getBackground().setAlpha(255);
                }

            }
        });

        et_new_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {// do job here owhen Edittext lose focus
                    rl_clear_new_pwd.setVisibility(View.INVISIBLE);
                } else {
                    if (!TextUtils.isEmpty(et_new_pwd.getText().toString()))
                        rl_clear_new_pwd.setVisibility(View.VISIBLE);
                }

            }
        });

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
                if (TextUtils.isEmpty(temp)) {
                    rl_clear_new_pwd_again.setVisibility(View.INVISIBLE);
                } else {
                    rl_clear_new_pwd_again.setVisibility(View.VISIBLE);
                    btn_submit.getBackground().setAlpha(255);
                }

            }
        });

        et_new_pwd_again.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {// do job here owhen Edittext lose focus
                    rl_clear_new_pwd_again.setVisibility(View.INVISIBLE);
                } else {
                    if (!TextUtils.isEmpty(et_new_pwd_again.getText().toString()))
                        rl_clear_new_pwd_again.setVisibility(View.VISIBLE);
                }

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber=et_phone.getText().toString();
                code = et_code.getText().toString();
                new_password = et_new_pwd.getText().toString();
                ensure_password= et_new_pwd_again.getText().toString();
                if(!TextUtils.isEmpty(new_password)&&!TextUtils.isEmpty(ensure_password)&&!TextUtils.isEmpty(code)){
                    boolean b= Pattern.compile("[a-zA-Z]").matcher(new_password).find();
                    if(new_password.equals(ensure_password)&&new_password.length()>5&&!TextUtils.isEmpty(phoneNumber)){
                        runUpdatePwd();
                    }
                    else{
                        if(!new_password.equals(ensure_password)){
                            DialogUtils.showAlertDialog(UpdatePwdActivity.this,
                                    "请输入一样的密码！");
                        }
//                        else if (!b) {
//                            DialogUtils.showAlertDialog(UpdatePwdActivity.this,
//                                    "请输入数字加字母的组合密码！");
//                        }
                        else if(new_password.length()<6||ensure_password.length()<6){
                            DialogUtils.showAlertDialog(UpdatePwdActivity.this,
                                    "请至少输入6位数密码！");
                        }else{
                            DialogUtils.showAlertDialog(UpdatePwdActivity.this,
                                    "账号不能为空！");
                        }
                    }
                }
                else{
                    if(TextUtils.isEmpty(new_password)){
                        DialogUtils.showAlertDialog(UpdatePwdActivity.this,
                                "新密码不能为空！");
                    }
                    else if(TextUtils.isEmpty(code)){
                        DialogUtils.showAlertDialog(UpdatePwdActivity.this,
                                "验证码不能为空！");
                    }
                    else {
                        DialogUtils.showAlertDialog(UpdatePwdActivity.this,
                                "重复密码不能为空！");
                    }

                }
            }
        });
        tv_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber=et_phone.getText().toString();
                if(!TextUtils.isEmpty(phoneNumber)){
                    runGetGode();
                }else {
                    DialogUtils.showAlertDialog(UpdatePwdActivity.this,
                            "手机号不能为空！");
                }

            }
        });
    }

    //获取验证码
    public void runGetGode() {
        loadingDialog.show();
        GetCodeProtocol getCodeProtocol = new GetCodeProtocol();
        GetCodeRequest getCodeRequest = new GetCodeRequest();
        String url = getCodeProtocol.getApiFun();
        getCodeRequest.map.put("phoneNumber", phoneNumber);
        getCodeRequest.map.put("type", "3");
        MyVolley.uploadNoFile(MyVolley.POST, url, getCodeRequest.map, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                GetCodeResponse getCodeResponse = gson.fromJson(json, GetCodeResponse.class);
                LogUtils.e("appSendMsgResponse:" + getCodeResponse.toString());
                if (getCodeResponse.code == 0) {
                    tv_get_code.setClickable(false);
                    loadingDialog.dismiss();
                    Countdowmtimer(60000);
                } else {
                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(UpdatePwdActivity.this,
                            getCodeResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(UpdatePwdActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    /**
     * 计时器
     */
    public void Countdowmtimer(long dodate) {
        new CountDownTimer(dodate, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time = time - 1;
                tv_get_code.setText(time + "s后重发");
            }

            @Override
            // 计时结束
            public void onFinish() {
                time = 60;
                tv_get_code.setText("获取验证码");
                tv_get_code.setClickable(true);

            }
        }.start();
    }

    public void runUpdatePwd() {
        loadingDialog.show();
        ModifyLoginPwdProtocol modifyLoginPwdProtocol = new ModifyLoginPwdProtocol();

        String url = modifyLoginPwdProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("phoneNumber", phoneNumber);
//        if(salesmanBean!=null){
//            params.put("type", "0");
//            params.put("salesmanId",  String.valueOf(salesmanBean.getId()));
//        }else{
//            params.put("type", "1");
//            params.put("businessId",  String.valueOf(merchantBean.getId()));
//        }

        params.put("password",  MD5Utils.MD5(new_password));
        params.put("code", code);
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                ModifyLoginPwdResponse modifyLoginPwdResponse = gson.fromJson(json, ModifyLoginPwdResponse.class);
                LogUtils.e("modifyLoginPwdResponse:" + modifyLoginPwdResponse.toString());
                if (modifyLoginPwdResponse.code.equals("0")) {
                    SuccessPopuwindow clockPopuwindow=new SuccessPopuwindow(UpdatePwdActivity.this,UpdatePwdActivity.this,"修改成功");
                    clockPopuwindow.showPopupWindow(rl_main);
                    loadingDialog.dismiss();

                } else {

                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(UpdatePwdActivity.this,
                            modifyLoginPwdResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(UpdatePwdActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(UpdatePwdActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }

    @Override
    public void CallBackSuccess(int type) {
        Intent intent=new Intent(this,LoginActivity.class);
        UIUtils.startActivity(intent);
        finish();
    }

    @Override
    public void CallBackDate(List<TypeBean> typeBeanList) {

    }
}
