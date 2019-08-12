package com.ciba.wholefinancial.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.MessageBean;
import com.ciba.wholefinancial.bean.ReportBean;
import com.ciba.wholefinancial.protocol.DeleteActivityProtocol;
import com.ciba.wholefinancial.protocol.DeleteMessageProtocol;
import com.ciba.wholefinancial.response.DeleteLoginResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.PictureOption;
import com.ciba.wholefinancial.util.SharedPrefrenceUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.HashMap;


public class MessageInfoActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;

    private View view_back;
    private int businessId;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_time;
    private MessageBean messageBean;
    private Button btn_delect;
    private AlertDialog alertDialog;
    private Dialog loadingDialog;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_message_info, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        messageBean=(MessageBean) getIntent().getSerializableExtra("MessageBean");

        loadingDialog = DialogUtils.createLoadDialog(MessageInfoActivity.this, false);
        businessId=getIntent().getIntExtra("businessId",0);
        btn_delect=(Button) findViewById(R.id.btn_delect);
        view_back=(View)findViewById(R.id.view_back);
        tv_title=(TextView) findViewById(R.id.tv_title);
        tv_time=(TextView) findViewById(R.id.tv_time);
        tv_content=(TextView) findViewById(R.id.tv_content);
        view_back.setOnClickListener(this);
        tv_title.setText(messageBean.getTitle());
        tv_time.setText(messageBean.getCreateTime());
        tv_content.setText(messageBean.getContent());
        btn_delect.setOnClickListener(this);
    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.btn_delect:{
                LogUtils.e("删除");
                alertDialog= DialogUtils.showAlertDoubleBtnDialog(MessageInfoActivity.this,"确认删除？","",1,MessageInfoActivity.this);
                break;
            }
            case R.id.tv_ensure:{
                deleteMsg();
                alertDialog.cancel();
                break;
            }
        }
    }

    public void deleteMsg() {
        loadingDialog.show();
        DeleteMessageProtocol deleteMessageProtocol = new DeleteMessageProtocol();

        String url = deleteMessageProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("authorization", SharedPrefrenceUtils.getString(MessageInfoActivity.this,"token"));
        params.put("msgIds",  String.valueOf(messageBean.getId()));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                DeleteLoginResponse deleteLoginResponse = gson.fromJson(json, DeleteLoginResponse.class);
                LogUtils.e("deleteLoginResponse:" + deleteLoginResponse.toString());
                if (deleteLoginResponse.code==0) {
                    finish();
                    overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                } else {
                    DialogUtils.showAlertDialog(MessageInfoActivity.this,
                            deleteLoginResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(MessageInfoActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(MessageInfoActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }

}
