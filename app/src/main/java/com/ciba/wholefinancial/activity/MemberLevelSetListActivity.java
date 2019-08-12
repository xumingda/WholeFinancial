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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.adapter.BusinessLoginAdapter;
import com.ciba.wholefinancial.adapter.MemberLevelAdapter;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.BusinessLoginBean;
import com.ciba.wholefinancial.bean.MemberLevelBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.protocol.GetLoginListProtocol;
import com.ciba.wholefinancial.protocol.GetMemberLevelListProtocol;
import com.ciba.wholefinancial.response.GetLoginListResponse;
import com.ciba.wholefinancial.response.GetMemberLevelListResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//子账号管理
public class MemberLevelSetListActivity extends BaseActivity implements View.OnClickListener
       {

    private LayoutInflater mInflater;
    private View rootView;

    private View view_back;
    private int businessId;

    private MemberLevelAdapter memberLevelAdapter;
    private String url;
    private Gson gson;
    private List<MemberLevelBean> memberLevelBeanList;
    private Dialog loadingDialog;
    GetMemberLevelListResponse getMemberLevelListResponse;
    private ListView lv_member_level;
    private MerchantBean merchantBean;
    private Button btn_add_level;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_memberlevel, null);
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
        loadingDialog = DialogUtils.createLoadDialog(MemberLevelSetListActivity.this, false);
        merchantBean=SPUtils.getBeanFromSp(this,"businessObject","MerchantBean");
        memberLevelBeanList=new ArrayList<>();

        businessId=getIntent().getIntExtra("businessId",0);
        view_back=(View)findViewById(R.id.view_back);
        lv_member_level=(ListView)findViewById(R.id.lv_member_level);
        btn_add_level=(Button)findViewById(R.id.btn_add_level);
        view_back.setOnClickListener(this);
        btn_add_level.setOnClickListener(this);
        if(merchantBean!=null){
            getMemberLevelList();
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
            case R.id.btn_add_level:{
                Intent intent=new Intent(MemberLevelSetListActivity.this,AddMemberLevelActivity.class);
                UIUtils.startActivityForResultNextAnim(intent,1);
                break;
            }
        }
    }
    private void setData() {
        if (memberLevelAdapter == null) {
            memberLevelAdapter = new MemberLevelAdapter( MemberLevelSetListActivity.this, getMemberLevelListResponse.dataList);
            lv_member_level.setAdapter(memberLevelAdapter);
            lv_member_level.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent=new Intent(MemberLevelSetListActivity.this,AddMemberLevelActivity.class);
                    intent.putExtra("MemberLevelBean",getMemberLevelListResponse.getDataList().get(i));
                    UIUtils.startActivityForResultNextAnim(intent,1);
                }
            });
        } else {
            memberLevelAdapter.setDate( getMemberLevelListResponse.dataList);
            memberLevelAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            getMemberLevelList();
        }
    }

    private void getMemberLevelList() {
        loadingDialog.show();
        GetMemberLevelListProtocol getMemberLevelListProtocol=new GetMemberLevelListProtocol();
        url = getMemberLevelListProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
//        params.put("status", "1");
        params.put("businessId",  String.valueOf(merchantBean.getId()));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
               getMemberLevelListResponse = gson.fromJson(json, GetMemberLevelListResponse.class);
                LogUtils.e("getMemberLevelListResponse:" + getMemberLevelListResponse.toString());
                if (getMemberLevelListResponse.getCode() == 0) {
                    if (getMemberLevelListResponse.getDataList().size() > 0) {

                        setData();
                    } else {
                        DialogUtils.showAlertDialog(MemberLevelSetListActivity.this, "没有更多数据了！");
                    }

                } else {
                    DialogUtils.showAlertDialog(MemberLevelSetListActivity.this, getMemberLevelListResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(MemberLevelSetListActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }


}
