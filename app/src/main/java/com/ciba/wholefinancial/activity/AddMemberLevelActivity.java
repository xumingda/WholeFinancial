package com.ciba.wholefinancial.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.MarketingBean;
import com.ciba.wholefinancial.bean.MemberLevelBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.datepicker.CustomDatePicker;
import com.ciba.wholefinancial.datepicker.DateFormatUtils;
import com.ciba.wholefinancial.protocol.DeleteActivityProtocol;
import com.ciba.wholefinancial.protocol.DeleteLevelProtocol;
import com.ciba.wholefinancial.protocol.GetLevelListProtocol;
import com.ciba.wholefinancial.protocol.UpdateMarketingProtocol;
import com.ciba.wholefinancial.protocol.UpdateMemberLevelProtocol;
import com.ciba.wholefinancial.response.DeleteLoginResponse;
import com.ciba.wholefinancial.response.GetLevelListResponse;
import com.ciba.wholefinancial.response.UpdateMarketingResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.SelectShopPopuwindow;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//营销编辑
public class AddMemberLevelActivity extends BaseActivity implements View.OnClickListener
        {

    private LayoutInflater mInflater;
    private View rootView;
    private List<String> levelList;
    private View view_back;
    private GetLevelListResponse getLevelListResponse;
    private String url;
    private Gson gson;
    private Dialog loadingDialog;
    private MemberLevelBean memberLevelBean;
    private EditText et_discount,et_money,et_count;
    private Button btn_delect;
    private Button btn_save;
    private LinearLayout ll_main;
    private AlertDialog alertDialog;
    private MerchantBean merchantBean;
    private SelectShopPopuwindow selectLevelPopuwindow;
    private String businessLevelId;
//    private TextView tv_levelName;
    private EditText et_levelName;
    private String levelName;
    private String count;
    private String money;
    private String rate;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_member_level_add, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        levelList=new ArrayList<>();
        merchantBean= SPUtils.getBeanFromSp(this,"businessObject","MerchantBean");
        memberLevelBean=(MemberLevelBean) getIntent().getSerializableExtra("MemberLevelBean");
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(AddMemberLevelActivity.this, false);
        ll_main=(LinearLayout) findViewById(R.id.ll_main);
        view_back=(View)findViewById(R.id.view_back);
        et_levelName=(EditText) findViewById(R.id.et_levelName);
        btn_delect=(Button) findViewById(R.id.btn_delect);
        et_count=(EditText) findViewById(R.id.et_count);
        et_money=(EditText) findViewById(R.id.et_money);
        et_discount=(EditText) findViewById(R.id.et_discount);
        btn_save=(Button)findViewById(R.id.btn_save);



        view_back.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_delect.setOnClickListener(this);
//        tv_levelName.setOnClickListener(this);
        if(memberLevelBean!=null){
            et_levelName.setText(memberLevelBean.getLevelName());
            businessLevelId=String.valueOf(memberLevelBean.getId());
            levelName=memberLevelBean.getLevelName();
            et_count.setText(memberLevelBean.getCount()+"");
            et_discount.setText(memberLevelBean.getRate()+"");
            et_money.setText(memberLevelBean.getMoney()+"");
        }else {
            btn_delect.setVisibility(View.GONE);
        }

    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_save:{
                count=et_count.getText().toString();
                money=et_money.getText().toString();
                rate=et_discount.getText().toString();
                levelName=et_levelName.getText().toString();
                if(!TextUtils.isEmpty(count)||!TextUtils.isEmpty(money)&&!TextUtils.isEmpty(rate)&&!TextUtils.isEmpty(levelName)){
                    updateMemberLevel();
                }else{
                    if(TextUtils.isEmpty(rate)){
                        DialogUtils.showAlertDialog(AddMemberLevelActivity.this, "折扣不能为空！");
                    }else if(TextUtils.isEmpty(levelName)){
                        DialogUtils.showAlertDialog(AddMemberLevelActivity.this, "会员等级名称必填！");
                    }
                    else{
                        DialogUtils.showAlertDialog(AddMemberLevelActivity.this, "次数或金额不能为空！");
                    }

                }
                break;
            }
            case R.id.view_back:{
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }

            case R.id.btn_delect:{
                LogUtils.e("删除");
                alertDialog=DialogUtils.showAlertDoubleBtnDialog(AddMemberLevelActivity.this,"确认删除？","",1, AddMemberLevelActivity.this);
                break;
            }
            case R.id.tv_ensure:{
                deleteLevel();
                alertDialog.cancel();
                break;
            }
            case R.id.tv_levelName:{
                if(levelList.size()<=0){
                    getLevel();
                }else{
                    selectLevelPopuwindow.showPopupWindow(ll_main);
                }
                break;
            }

        }
    }
    private void updateMemberLevel() {
        loadingDialog.show();
        UpdateMemberLevelProtocol updateMemberLevelProtocol=new UpdateMemberLevelProtocol();
        url = updateMemberLevelProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("businessId",  String.valueOf(merchantBean.getId()));
        if(businessLevelId!=null){
            params.put("businessLevelId", businessLevelId);
        }
        params.put("levelName", levelName);
        params.put("count", count);
        params.put("money", money);
        params.put("rate", rate);
        if(memberLevelBean!=null){
            params.put("higherLevelId", String.valueOf(memberLevelBean.getHigherLevelId()));
            params.put("lowerLevelId", String.valueOf(memberLevelBean.getLowerLevelId()));
        }
        else{
            params.put("higherLevelId", String.valueOf(0));
            params.put("lowerLevelId", String.valueOf(0));
        }


        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                LogUtils.e("updateMarketingResponse:" + json.toString());
                loadingDialog.dismiss();
               UpdateMarketingResponse updateMarketingResponse = gson.fromJson(json, UpdateMarketingResponse.class);
                LogUtils.e("updateMarketingResponse:" + updateMarketingResponse.toString());
                if (updateMarketingResponse.getCode() == 0) {
                    SuccessPopuwindow successPopuwindow=new SuccessPopuwindow(AddMemberLevelActivity.this,null,"修改成功");
                    successPopuwindow.showPopupWindow(ll_main);
                } else {
                    if(updateMarketingResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(AddMemberLevelActivity.this,
                                updateMarketingResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(AddMemberLevelActivity.this, updateMarketingResponse.msg);
                    }
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AddMemberLevelActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }


    public void deleteLevel() {
        loadingDialog.show();
        DeleteLevelProtocol deleteLevelProtocol = new DeleteLevelProtocol();

        String url = deleteLevelProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("businessId",  String.valueOf(merchantBean.getId()));
        params.put("businessLevelId", String.valueOf(businessLevelId));

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
                    DialogUtils.showAlertDialog(AddMemberLevelActivity.this,
                            deleteLoginResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(AddMemberLevelActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(AddMemberLevelActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }

    private void getLevel() {
            loadingDialog.show();
            GetLevelListProtocol getLevelListProtocol=new GetLevelListProtocol();
            url = getLevelListProtocol.getApiFun();
            Map<String, String> params = new HashMap<String, String>();
            params.put("businessId",  String.valueOf(merchantBean.getId()));


            MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

                @Override
                public void dealWithJson(String address, String json) {

                    loadingDialog.dismiss();
                    getLevelListResponse = gson.fromJson(json, GetLevelListResponse.class);
                    LogUtils.e("getLevelListResponse:" + getLevelListResponse.toString());
                    if (getLevelListResponse.getCode() == 0) {

                        for(int i=0;i<getLevelListResponse.getDataList().size();i++){
                            levelList.add(getLevelListResponse.getDataList().get(i).getLevelName());
                        }
                        selectLevelPopuwindow = new SelectShopPopuwindow(UIUtils.getActivity(),"等级名称",levelList,itemsOnClick);
                        selectLevelPopuwindow.showPopupWindow(ll_main);
                    } else {
                        DialogUtils.showAlertDialog(AddMemberLevelActivity.this, getLevelListResponse.getMsg());
                    }

                }

                @Override
                public void dealWithError(String address, String error) {
                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(AddMemberLevelActivity.this, error);

                }

                @Override
                public void dealTokenOverdue() {

                }
            });
    }
    private AdapterView.OnItemClickListener itemsOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            businessLevelId=String.valueOf(getLevelListResponse.getDataList().get(position).getId());
            LogUtils.e("levels;"+businessLevelId);
            String value = levelList.get(selectLevelPopuwindow.getText());
            levelName=value;
//            tv_levelName.setText(value);
            selectLevelPopuwindow.dismissPopupWindow();

        }
    };
}
