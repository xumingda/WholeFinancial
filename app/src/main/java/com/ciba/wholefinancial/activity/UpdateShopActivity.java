package com.ciba.wholefinancial.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import com.ciba.wholefinancial.bean.BusinessShopBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.protocol.AddOrUpdateLoginProtocol;
import com.ciba.wholefinancial.protocol.DeleteLoginProtocol;
import com.ciba.wholefinancial.protocol.DeleteShopProtocol;
import com.ciba.wholefinancial.protocol.UpdateShopProtocol;
import com.ciba.wholefinancial.response.AddOrUpdateLoginResponse;
import com.ciba.wholefinancial.response.DeleteLoginResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.google.gson.Gson;

import java.util.HashMap;

public class UpdateShopActivity extends BaseActivity implements View.OnClickListener {

    private View view_back;
    private TextView mTextMessage;
    private LayoutInflater mInflater;
    private View rootView;
    private EditText et_name;
    private TextView tv_code;
//    private RelativeLayout rl_clear_new_pwd;
//    private RelativeLayout rl_clear_new_pwd_again;
//    private TextView tv_get_code;
    private Dialog loadingDialog;
    private BusinessShopBean businessShopBean;
    private String name;
    private RelativeLayout rl_main;
    private TextView tv_delete,tv_save;
    private AlertDialog alertDialog;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_update_shop, null);
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
        businessShopBean=(BusinessShopBean) getIntent().getSerializableExtra("BusinessShopBean");

        loadingDialog = DialogUtils.createLoadDialog(UpdateShopActivity.this, false);
        view_back=(View) findViewById(R.id.view_back);
        et_name=(EditText)findViewById(R.id.et_name);
        tv_code=(TextView) findViewById(R.id.tv_code);
        tv_delete=(TextView) findViewById(R.id.tv_delete);
        tv_save=(TextView) findViewById(R.id.tv_save);

        rl_main=(RelativeLayout)findViewById(R.id.rl_main);
        LogUtils.e("businessShopBeann:"+businessShopBean);
        et_name.setText(businessShopBean.getShopName());
        tv_code.setText("餐桌码数量："+businessShopBean.getCodeCount());

        SpannableStringBuilder style=new SpannableStringBuilder(tv_code.getText());
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.errorColor)), 6, tv_code.getText().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv_code.setText(style);
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();

                if(!TextUtils.isEmpty(name)){
                    updateShop();
                }
                else{
                    DialogUtils.showAlertDialog(UpdateShopActivity.this,
                            "姓名不能为空！");
                }
            }
        });
        view_back.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }

            case R.id.tv_delete:{
                LogUtils.e("删除");
                alertDialog=DialogUtils.showAlertDoubleBtnDialog(UpdateShopActivity.this,"删除店铺？","",1, UpdateShopActivity.this);
                break;
            }
            case R.id.tv_ensure:{
                alertDialog.cancel();
                deleteShop();
                break;
            }

        }
    }



    public void updateShop() {
        loadingDialog.show();
        UpdateShopProtocol updateShopProtocol = new UpdateShopProtocol();

        String url = updateShopProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("shopName", name);
        params.put("businessShopId",  String.valueOf(businessShopBean.getId()));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                AddOrUpdateLoginResponse addOrUpdateLoginResponse = gson.fromJson(json, AddOrUpdateLoginResponse.class);
                LogUtils.e("addOrUpdateLoginResponse:" + addOrUpdateLoginResponse.toString());
                if (addOrUpdateLoginResponse.code==0) {
                    SuccessPopuwindow clockPopuwindow=new SuccessPopuwindow(UpdateShopActivity.this,null,"保存成功");
                    clockPopuwindow.showPopupWindow(rl_main);
                    loadingDialog.dismiss();
//                    finish();
//                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                } else {

                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(UpdateShopActivity.this,
                            addOrUpdateLoginResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(UpdateShopActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(UpdateShopActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }
    public void deleteShop() {
        loadingDialog.show();
        DeleteShopProtocol deleteShopProtocol = new DeleteShopProtocol();

        String url = deleteShopProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("businessShopId",  String.valueOf(businessShopBean.getId()));
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
                    DialogUtils.showAlertDialog(UpdateShopActivity.this,
                            deleteLoginResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(UpdateShopActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(UpdateShopActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }
}
