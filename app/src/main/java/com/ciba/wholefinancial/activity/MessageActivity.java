package com.ciba.wholefinancial.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.adapter.MessageAdapter;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.protocol.GetMessageListProtocol;
import com.ciba.wholefinancial.response.GetMessageListResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

//消息
public class MessageActivity extends BaseActivity implements View.OnClickListener {

    private LayoutInflater mInflater;
    private View rootView;
    private String url;

    private Dialog loadingDialog;
    //判断是否刷新
    private SalesmanBean salesmanBean;
    private MerchantBean merchantBean;
    private MessageAdapter messageAdapter;
    private ListView lv_message;
    private GetMessageListResponse getMessageListResponse;
    private View v_empty;
    private TextView tv_empty;
    private View view_back;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_message, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        tv_empty=(TextView)rootView.findViewById(R.id.tv_empty);
        v_empty=(View) rootView.findViewById(R.id.v_empty);
        view_back=(View)findViewById(R.id.view_back);
        lv_message=(ListView)rootView.findViewById(R.id.lv_message);
        view_back.setOnClickListener(this);
        loadingDialog = DialogUtils.createLoadDialog(MessageActivity.this, false);
        salesmanBean= SPUtils.getBeanFromSp(MessageActivity.this,"salesmanObject","SalesmanBean");
        merchantBean= SPUtils.getBeanFromSp(this,"businessObject","MerchantBean");
        getMessageList();
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


        }
    }




    private void getMessageList() {
        loadingDialog.show();
        GetMessageListProtocol getMessageListProtocol=new GetMessageListProtocol();
        url = getMessageListProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
//        params.put("status", "1");
        if(salesmanBean!=null){
            params.put("type",  String.valueOf(0));
            params.put("salesmanId",  String.valueOf(salesmanBean.getId()));
        }else{
            params.put("type",  String.valueOf(1));
            params.put("businessId",  String.valueOf(merchantBean.getId()));
        }


        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                Gson gson = new Gson();
                loadingDialog.dismiss();
                getMessageListResponse = gson.fromJson(json, GetMessageListResponse.class);
                LogUtils.e("getMessageListResponse:" + getMessageListResponse.toString());
                if (getMessageListResponse.getCode() == 0) {

                    if (getMessageListResponse.getDataList().size() > 0) {
                        if (messageAdapter == null) {
                            messageAdapter = new MessageAdapter( MessageActivity.this, getMessageListResponse.getDataList());
                            lv_message.setAdapter(messageAdapter);
                        } else {
                            messageAdapter.notifyDataSetChanged();
                        }
                        lv_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent=new Intent(MessageActivity.this,MessageInfoActivity.class);
                                intent.putExtra("MessageBean",getMessageListResponse.getDataList().get(i));
                                UIUtils.startActivityForResultNextAnim(intent,100);
                            }
                        });
                    } else {
                        v_empty.setVisibility(View.VISIBLE);
                        tv_empty.setVisibility(View.VISIBLE);
                        lv_message.setVisibility(View.GONE);
//                        DialogUtils.showAlertDialog(MessageActivity.this, "没有更多数据了！");
                    }

                } else {
                    DialogUtils.showAlertDialog(MessageActivity.this, getMessageListResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(MessageActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==100){
            getMessageList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
