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

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.adapter.BusinessLoginAdapter;
import com.ciba.wholefinancial.adapter.MarketingAdapter;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.BusinessLoginBean;
import com.ciba.wholefinancial.bean.MarketingBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.protocol.GetLoginListProtocol;
import com.ciba.wholefinancial.protocol.GetMarketingListProtocol;
import com.ciba.wholefinancial.response.GetLoginListResponse;
import com.ciba.wholefinancial.response.GetMarketingListResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//子账号管理
public class SubaccountManagementActivity extends BaseActivity implements View.OnClickListener
       {

    private LayoutInflater mInflater;
    private View rootView;

    private ImageView iv_top_back,iv_add;
    private View view_back,view_add;
    private int businessId;

    private MerchantBean merchantBean;
    private String url;
    private Gson gson;
    private List<BusinessLoginBean> businessLoginBeanList;
    private Dialog loadingDialog;
    GetLoginListResponse getLoginListResponse;
    private BusinessLoginAdapter businessLoginAdapter;
    private ListView lv_subaccount;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_subaccount_management, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(SubaccountManagementActivity.this, false);
        merchantBean=SPUtils.getBeanFromSp(this,"businessObject","MerchantBean");
        businessLoginBeanList=new ArrayList<>();

        businessId=getIntent().getIntExtra("businessId",0);
        iv_top_back=(ImageView)findViewById(R.id.iv_top_back);
        view_back=(View)findViewById(R.id.view_back);
        view_add=(View)findViewById(R.id.view_add);
        iv_add=(ImageView)findViewById(R.id.iv_add);
        lv_subaccount=(ListView)findViewById(R.id.lv_subaccount);
        view_back.setOnClickListener(this);
        view_add.setOnClickListener(this);
        if(merchantBean!=null){
            getLoginList();
        }

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
            case R.id.view_add:{
                Intent intent=new Intent(SubaccountManagementActivity.this,AddBusinessLoginActivity.class);
                UIUtils.startActivityForResultNextAnim(intent,1);
                break;
            }
        }
    }
    private void setData() {
        if (businessLoginAdapter == null) {
            businessLoginAdapter = new BusinessLoginAdapter( SubaccountManagementActivity.this, getLoginListResponse.dataList);
            lv_subaccount.setAdapter(businessLoginAdapter);
            lv_subaccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent=new Intent(SubaccountManagementActivity.this,UpdateBusinessLoginActivity.class);
                    intent.putExtra("BusinessLoginBean",getLoginListResponse.getDataList().get(i));
                    UIUtils.startActivityForResultNextAnim(intent,1);
                }
            });
        } else {
            businessLoginAdapter.setDate( getLoginListResponse.dataList);
            businessLoginAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            getLoginList();
        }
    }

    private void getLoginList() {
        loadingDialog.show();
        GetLoginListProtocol getLoginListProtocol=new GetLoginListProtocol();
        url = getLoginListProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
//        params.put("status", "1");
        params.put("businessId",  String.valueOf(merchantBean.getId()));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
               getLoginListResponse = gson.fromJson(json, GetLoginListResponse.class);
                LogUtils.e("getLoginListResponse:" + getLoginListResponse.toString());
                if (getLoginListResponse.getCode() == 0) {
                    if (getLoginListResponse.getDataList().size() > 0) {

                        setData();
                    } else {
                        DialogUtils.showAlertDialog(SubaccountManagementActivity.this, "没有更多数据了！");
                    }

                } else {
                    DialogUtils.showAlertDialog(SubaccountManagementActivity.this, getLoginListResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(SubaccountManagementActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }


}
