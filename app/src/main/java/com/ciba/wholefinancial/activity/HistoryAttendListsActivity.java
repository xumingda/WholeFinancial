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
import com.ciba.wholefinancial.adapter.ClockAdapter;
import com.ciba.wholefinancial.adapter.HistoryClockAdapter;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.ClockBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.protocol.GetClockListProtocol;
import com.ciba.wholefinancial.response.GetClocktListResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//会员管理
public class HistoryAttendListsActivity extends BaseActivity implements View.OnClickListener,PullToRefreshBase.OnRefreshListener{

    private LayoutInflater mInflater;
    private View rootView;


    private ImageView record;
    private View view_back;

    private Dialog loadingDialog;
    private MerchantBean merchantBean;
    private RelativeLayout rl;
    private PullToRefreshListView lv_member;
    private View v_empty;
    private TextView tv_empty;
    private GetClocktListResponse getClocktListResponse;
    private List<ClockBean> clockBeanList;
    private int pageNo = 1;
    //判断是否刷新
    private boolean isRefresh = false;
    private HistoryClockAdapter clockAdapter;
    private TextView tv_people,tv_count_money;
    private SalesmanBean salesmanBean;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_attend_list, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        salesmanBean= SPUtils.getBeanFromSp(this,"salesmanObject","SalesmanBean");
        loadingDialog = DialogUtils.createLoadDialog(HistoryAttendListsActivity.this, false);
        rl=(RelativeLayout)findViewById(R.id.rl);
        record=(ImageView)findViewById(R.id.iv_record);
        view_back=(View)findViewById(R.id.view_back);
        v_empty=(View)findViewById(R.id.v_empty);
        tv_empty=(TextView)findViewById(R.id.tv_empty);
        tv_count_money=(TextView)findViewById(R.id.tv_count_money);
        tv_people=(TextView)findViewById(R.id.tv_people);
        lv_member=(PullToRefreshListView)findViewById(R.id.lv_member);


        clockBeanList=new ArrayList<>();
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
                if(clockBeanList.get(i-1).getStatus()!=4){
                    Intent intent = new Intent(HistoryAttendListsActivity.this, AttendInfoActivity.class);
                    intent.putExtra("attendanceId", clockBeanList.get(i - 1).getAttendanceId());
                    UIUtils.startActivityNextAnim(intent);
                }
            }
        });
        record.setVisibility(View.INVISIBLE);





        view_back.setOnClickListener(this);


        getClockList();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==100){
            pageNo=1;
            getClockList();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        clockBeanList.addAll(getClocktListResponse.getDataList());
        if (clockAdapter == null) {
            clockAdapter = new HistoryClockAdapter( HistoryAttendListsActivity.this, clockBeanList);
            lv_member.setAdapter(clockAdapter);
        } else {
            clockAdapter.setDate(clockBeanList);
            clockAdapter.notifyDataSetChanged();
        }

    }


    //获取考勤数据
    public void getClockList() {
        loadingDialog.show();

        GetClockListProtocol getClockListProtocol = new GetClockListProtocol();

        String url = getClockListProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("salesmanId",  String.valueOf(salesmanBean.getId()));
        params.put("status", "");
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                getClocktListResponse = gson.fromJson(json, GetClocktListResponse.class);
                LogUtils.e("getClocktListResponse:" + getClocktListResponse.toString());
                loadingDialog.dismiss();
                if (getClocktListResponse.code == 0) {
                    if(getClocktListResponse.getDataList().size()>0){

                        if (getClocktListResponse.getDataList().size() > 0) {
                            if (pageNo == 1) {
                                clockBeanList.clear();
                            }
                            setData();
                        } else {
                            DialogUtils.showAlertDialog(HistoryAttendListsActivity.this, "没有更多数据了！");
                        }
                    }else {
                        v_empty.setVisibility(View.VISIBLE);
                        tv_empty.setVisibility(View.VISIBLE);
                        lv_member.setVisibility(View.GONE);
                    }

                } else {

                    DialogUtils.showAlertDialog(HistoryAttendListsActivity.this,
                            getClocktListResponse.msg);
                }

                if (isRefresh) {
                    isRefresh = false;
                    lv_member.onRefreshComplete();
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(HistoryAttendListsActivity.this, error);
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

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (!isRefresh) {
            isRefresh = true;
            if (lv_member.isHeaderShown()) {
                pageNo = 1;
                clockBeanList.clear();
                getClockList();
            } else if (lv_member.isFooterShown()) {
                pageNo++;
                getClockList();
            }
        }
    }
}
