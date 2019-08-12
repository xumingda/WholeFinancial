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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.adapter.MemberAdapter;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.MemberBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.protocol.GetMemberProtocol;
import com.ciba.wholefinancial.protocol.GetUserHomeProtocol;
import com.ciba.wholefinancial.response.GetMemberResponse;
import com.ciba.wholefinancial.response.GetUserHomeResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//会员管理
public class MemberManagerActivity extends BaseActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener{

    private LayoutInflater mInflater;
    private View rootView;


    private ImageView iv_search;
    private View view_back;

    private Dialog loadingDialog;
    private MerchantBean merchantBean;
    private RelativeLayout rl;
    private PullToRefreshListView lv_member;
    private View v_empty;
    private TextView tv_empty;
    private GetMemberResponse getMemberResponse;
    private List<MemberBean> memberBeanList;
    private int pageNo = 1;
    //判断是否刷新
    private boolean isRefresh = false;
    private MemberAdapter memberAdapter;
    private TextView tv_people,tv_count_money;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_member_manager, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        merchantBean= SPUtils.getBeanFromSp(this,"businessObject","MerchantBean");
        loadingDialog = DialogUtils.createLoadDialog(MemberManagerActivity.this, false);
        rl=(RelativeLayout)findViewById(R.id.rl);
        iv_search=(ImageView)findViewById(R.id.iv_search);
        view_back=(View)findViewById(R.id.view_back);
        v_empty=(View)findViewById(R.id.v_empty);
        tv_empty=(TextView)findViewById(R.id.tv_empty);
        tv_count_money=(TextView)findViewById(R.id.tv_count_money);
        tv_people=(TextView)findViewById(R.id.tv_people);
        lv_member=(PullToRefreshListView)findViewById(R.id.lv_member);


        memberBeanList=new ArrayList<>();
        merchantBean= SPUtils.getBeanFromSp(this,"businessObject","MerchantBean");
        loadingDialog = DialogUtils.createLoadDialog(this, false);
        lv_member.setMode(PullToRefreshBase.Mode.BOTH);
        lv_member.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        lv_member.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
        lv_member.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以刷新");
        lv_member.setOnRefreshListener(this);
        lv_member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MemberManagerActivity.this, MemberInfoActivity.class);
                intent.putExtra("MemberBean",memberBeanList.get(i-1));
                UIUtils.startActivity(intent);
            }
        });
        getHome();






        view_back.setOnClickListener(this);

        //点击返回键打开或关闭Menu
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent=new Intent(MemberManagerActivity.this,MemberSearchActivity.class);
                    UIUtils.startActivityNextAnim(intent);
            }
        });

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

    private void setData() {
        memberBeanList.addAll(getMemberResponse.getDataList());
        if (memberAdapter == null) {
            memberAdapter = new MemberAdapter( MemberManagerActivity.this, memberBeanList);
            lv_member.setAdapter(memberAdapter);
            LogUtils.e("设置数据"+memberBeanList.size());
        } else {
            memberAdapter.setDate(memberBeanList);
            memberAdapter.notifyDataSetChanged();
        }

    }

    //获取数据
    public void getMemberList() {
        loadingDialog.show();
        GetMemberProtocol getMemberProtocol = new GetMemberProtocol();
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("businessId",  String.valueOf(merchantBean.getId()));
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", "100");
        String url = getMemberProtocol.getApiFun();

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                getMemberResponse = gson.fromJson(json, GetMemberResponse.class);
                LogUtils.e("getMemberResponse:" + getMemberResponse.toString());
                loadingDialog.dismiss();
                if (getMemberResponse.code == 0) {
                    if(getMemberResponse.getDataList().size()>0){

                        if (getMemberResponse.getDataList().size() > 0) {
                            if (pageNo == 1) {
                                memberBeanList.clear();
                            }
                            setData();
                        } else {
                            DialogUtils.showAlertDialog(MemberManagerActivity.this, "没有更多数据了！");
                        }
                    }else {
                        v_empty.setVisibility(View.VISIBLE);
                        tv_empty.setVisibility(View.VISIBLE);
                        lv_member.setVisibility(View.GONE);
                    }

                } else {

                    DialogUtils.showAlertDialog(MemberManagerActivity.this,
                            getMemberResponse.msg);
                }

                if (isRefresh) {
                    isRefresh = false;
                    lv_member.onRefreshComplete();
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(MemberManagerActivity.this, error);
                if (isRefresh) {
                    isRefresh = false;
                    lv_member.onRefreshComplete();
                }
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    //获取数据
    public void getHome() {
        loadingDialog.show();
        GetUserHomeProtocol getUserHomeProtocol = new GetUserHomeProtocol();
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("businessId",  String.valueOf(merchantBean.getId()));
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", "100");
        String url = getUserHomeProtocol.getApiFun();

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                GetUserHomeResponse getUserHomeResponse = gson.fromJson(json, GetUserHomeResponse.class);
                LogUtils.e("getUserHomeResponse:" + getUserHomeResponse.toString());
                loadingDialog.dismiss();
                if (getUserHomeResponse.code == 0) {
                    DecimalFormat df = new java.text.DecimalFormat("#.00");
                    tv_count_money.setText("总金额"+df.format(getUserHomeResponse.getBalance()));
                    tv_people.setText("总共"+getUserHomeResponse.getUserCount()+"人");
                    getMemberList();
                } else {

                    DialogUtils.showAlertDialog(MemberManagerActivity.this,
                            getUserHomeResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(MemberManagerActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (!isRefresh) {
            isRefresh = true;
            if (lv_member.isHeaderShown()) {
                pageNo = 1;
                memberBeanList.clear();
                getMemberList();
            } else if (lv_member.isFooterShown()) {
                pageNo++;
                getMemberList();
            }
        }
    }
}
