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
import com.ciba.wholefinancial.adapter.MarketingAdapter;
import com.ciba.wholefinancial.adapter.MessageAdapter;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.MarketingBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.MessageBean;
import com.ciba.wholefinancial.protocol.GetMarketingListProtocol;
import com.ciba.wholefinancial.protocol.GetMessageListProtocol;
import com.ciba.wholefinancial.response.GetMarketingListResponse;
import com.ciba.wholefinancial.response.GetMessageListResponse;
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

//营销
public class MarketingActivity extends BaseActivity implements View.OnClickListener,
        PullToRefreshBase.OnRefreshListener {

    private LayoutInflater mInflater;
    private View rootView;

    private View view_back,view_add;
    private int businessId;

    private MerchantBean merchantBean;
    private String url;
    private Gson gson;
    private int pageNo = 1;
    private List<MarketingBean> merchantBeanList;
    private Dialog loadingDialog;
    GetMarketingListResponse getMerchantListResponse;
    private MarketingAdapter marketingAdapter;
    private PullToRefreshListView lv_marketing;
    //判断是否刷新
    private boolean isRefresh = false;
    private View v_empty;
    private TextView tv_empty;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_marketing, null);
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
        loadingDialog = DialogUtils.createLoadDialog(MarketingActivity.this, false);
        merchantBean=SPUtils.getBeanFromSp(this,"businessObject","MerchantBean");
        merchantBeanList=new ArrayList<>();

        businessId=getIntent().getIntExtra("businessId",0);
        v_empty=(View)findViewById(R.id.v_empty);
        tv_empty=(TextView)findViewById(R.id.tv_empty);
        view_back=(View)findViewById(R.id.view_back);
        view_add=(View)findViewById(R.id.view_add);
        lv_marketing=(PullToRefreshListView)findViewById(R.id.lv_marketing);
        view_back.setOnClickListener(this);
        view_add.setOnClickListener(this);
        lv_marketing.setMode(PullToRefreshBase.Mode.BOTH);
        lv_marketing.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        lv_marketing.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
        lv_marketing.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以刷新");
        lv_marketing.setOnRefreshListener(this);
        getMarketingList();
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
                Intent intent=new Intent(MarketingActivity.this,AddMarketActivity.class);
                UIUtils.startActivityForResultNextAnim(intent,1);
                break;
            }
        }
    }
    private void setData() {
        merchantBeanList.addAll(getMerchantListResponse.getDataList());
        if (marketingAdapter == null) {
            marketingAdapter = new MarketingAdapter( MarketingActivity.this, merchantBeanList);
            lv_marketing.setAdapter(marketingAdapter);
            lv_marketing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent=new Intent(MarketingActivity.this,EditMarketActivity.class);
                    intent.putExtra("MarketingBean",merchantBeanList.get(i-1));
                    UIUtils.startActivityForResultNextAnim(intent,1);
                    LogUtils.e("设置数据"+i);
                }
            });

        } else {
            marketingAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            getMarketingList();
        }
    }

    private void getMarketingList() {
        loadingDialog.show();
        GetMarketingListProtocol getMerchantProtocol=new GetMarketingListProtocol();
        url = getMerchantProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
//        params.put("status", "1");
        params.put("businessId",  String.valueOf(merchantBean.getId()));
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", "100");
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
               getMerchantListResponse = gson.fromJson(json, GetMarketingListResponse.class);
                LogUtils.e("getMerchantListResponse:" + getMerchantListResponse.toString());
                if (getMerchantListResponse.getCode() == 0) {
                    if (getMerchantListResponse.getDataList().size() > 0) {
                        if (pageNo == 1) {
                            merchantBeanList.clear();
                        }
                        setData();
                    } else {
                        if (pageNo == 1) {
                            v_empty.setVisibility(View.VISIBLE);
                            tv_empty.setVisibility(View.VISIBLE);
                            lv_marketing.setVisibility(View.GONE);
                        }else {
                            DialogUtils.showAlertDialog(MarketingActivity.this, "没有更多数据了！");
                        }

                    }

                } else {
                    DialogUtils.showAlertDialog(MarketingActivity.this, getMerchantListResponse.getMsg());
                }
                if (isRefresh) {
                    LogUtils.e("结束");
                    isRefresh = false;
                    lv_marketing.onRefreshComplete();
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(MarketingActivity.this, error);
                if (isRefresh) {
                    isRefresh = false;
                    lv_marketing.onRefreshComplete();
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
            if (lv_marketing.isHeaderShown()) {
                pageNo = 1;
                merchantBeanList.clear();
                getMarketingList();
            } else if (lv_marketing.isFooterShown()) {
                pageNo++;
                getMarketingList();
            }
        }
    }
}
