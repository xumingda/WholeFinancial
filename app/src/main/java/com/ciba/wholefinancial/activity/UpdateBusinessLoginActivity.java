package com.ciba.wholefinancial.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
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
import com.ciba.wholefinancial.bean.BusinessLoginBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.protocol.AddOrUpdateLoginProtocol;
import com.ciba.wholefinancial.protocol.DeleteLoginProtocol;
import com.ciba.wholefinancial.response.AddOrUpdateLoginResponse;
import com.ciba.wholefinancial.response.DeleteLoginResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.MD5Utils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.regex.Pattern;

public class UpdateBusinessLoginActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_top_back;
    private TextView mTextMessage;
    private LayoutInflater mInflater;
    private View rootView;
    private EditText et_name,et_phone;
    private Button btn_submit;
//    private RelativeLayout rl_clear_new_pwd;
//    private RelativeLayout rl_clear_new_pwd_again;
//    private TextView tv_get_code;
    private Dialog loadingDialog;
    private MerchantBean merchantBean;
    private BusinessLoginBean businessLoginBean;
    private String name;
    private String account;
    private RelativeLayout rl_main;
    private Button btn_delect;
    private AlertDialog alertDialog;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_update_login, null);
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
        businessLoginBean=(BusinessLoginBean)getIntent().getSerializableExtra("BusinessLoginBean");
        merchantBean= SPUtils.getBeanFromSp(this,"businessObject","MerchantBean");
        loadingDialog = DialogUtils.createLoadDialog(UpdateBusinessLoginActivity.this, false);
        iv_top_back=(ImageView) findViewById(R.id.iv_top_back);
        et_name=(EditText)findViewById(R.id.et_name);
        et_phone=(EditText)findViewById(R.id.et_phone);
        btn_delect=(Button) findViewById(R.id.btn_delect);
        btn_submit=(Button) findViewById(R.id.btn_submit);

        rl_main=(RelativeLayout)findViewById(R.id.rl_main);

        et_name.setText(businessLoginBean.getName());
        et_phone.setText(businessLoginBean.getAccount());

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();
                account=et_phone.getText().toString();

                if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(account)){
                    updateLogin();
                }
                else{
                    if(TextUtils.isEmpty(name)){
                        DialogUtils.showAlertDialog(UpdateBusinessLoginActivity.this,
                                "姓名不能为空！");
                    }else {
                        DialogUtils.showAlertDialog(UpdateBusinessLoginActivity.this,
                                "手机号不能为空！");
                    }


                }
            }
        });
        iv_top_back.setOnClickListener(this);
        btn_delect.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.iv_top_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }

            case R.id.btn_delect:{
                LogUtils.e("删除");
                alertDialog=DialogUtils.showAlertDoubleBtnDialog(UpdateBusinessLoginActivity.this,"删除账号？","",1,UpdateBusinessLoginActivity.this);
                break;
            }
            case R.id.tv_ensure:{
                alertDialog.cancel();
                deleteLogin();
                break;
            }

        }
    }



    public void updateLogin() {
        loadingDialog.show();
        AddOrUpdateLoginProtocol addOrUpdateLoginProtocol = new AddOrUpdateLoginProtocol();

        String url = addOrUpdateLoginProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("account", account);
        params.put("businessId",  String.valueOf(merchantBean.getId()));
        params.put("businessLoginId",  String.valueOf(businessLoginBean.getId()));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                AddOrUpdateLoginResponse addOrUpdateLoginResponse = gson.fromJson(json, AddOrUpdateLoginResponse.class);
                LogUtils.e("addOrUpdateLoginResponse:" + addOrUpdateLoginResponse.toString());
                if (addOrUpdateLoginResponse.code==0) {
                    SuccessPopuwindow clockPopuwindow=new SuccessPopuwindow(UpdateBusinessLoginActivity.this,null,"保存成功");
                    clockPopuwindow.showPopupWindow(rl_main);
                    loadingDialog.dismiss();
//                    finish();
//                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                } else {

                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(UpdateBusinessLoginActivity.this,
                            addOrUpdateLoginResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(UpdateBusinessLoginActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(UpdateBusinessLoginActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }
    public void deleteLogin() {
        loadingDialog.show();
        DeleteLoginProtocol deleteLoginProtocol = new DeleteLoginProtocol();

        String url = deleteLoginProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("businessLoginId",  String.valueOf(businessLoginBean.getId()));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                DeleteLoginResponse deleteLoginResponse = gson.fromJson(json, DeleteLoginResponse.class);
                LogUtils.e("deleteLoginResponse:" + deleteLoginResponse.toString());
                if (deleteLoginResponse.code==0) {
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                } else {

                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(UpdateBusinessLoginActivity.this,
                            deleteLoginResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(UpdateBusinessLoginActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(UpdateBusinessLoginActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }
}
