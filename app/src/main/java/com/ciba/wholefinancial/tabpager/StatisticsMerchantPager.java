package com.ciba.wholefinancial.tabpager;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.adapter.StatisticsAdapter;
import com.ciba.wholefinancial.adapter.StatisticsMerchantAdapter;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.callback.OnStatisticsCallBack;
import com.ciba.wholefinancial.protocol.GetReceivablesStatisticsDescProtocol;
import com.ciba.wholefinancial.protocol.GetReceivablesStatisticsProtocol;
import com.ciba.wholefinancial.response.GetReceivablesStatisticsDescResponse;
import com.ciba.wholefinancial.response.GetReceivablesStatisticsResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.weiget.SelectShopPopuwindow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @作者: 许明达
 * @创建时间: 2016-4-27下午5:24:30
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class StatisticsMerchantPager extends ViewTabBasePager implements
        PullToRefreshBase.OnRefreshListener {
    // 分类listview
    @ViewInject(R.id.lv_pending_order)
    private PullToRefreshListView lv_pending_order;
    @ViewInject(R.id.ll_empty)
    private LinearLayout ll_empty;

    @ViewInject(R.id.ll_main)
    private LinearLayout ll_main;
    private StatisticsMerchantAdapter statisticsMerchantAdapter;
    private Dialog loadingDialog;
    private String url;
    private GetReceivablesStatisticsResponse getReceivablesStatisticsResponse;
    private Gson gson;
    private List<GetReceivablesStatisticsResponse.ReceivablesStatisticsBean> receivablesStatisticsBeanList;
    private int pageNo = 1;
    //判断是否刷新
    private boolean isRefresh = false;
    private String startTime;
    private String endTime;
    private MerchantBean merchantBean;
    private OnStatisticsCallBack onStatisticsCallBack;
    public StatisticsMerchantPager(Context context, String startTime, String endTime, OnStatisticsCallBack onStatisticsCallBack) {
        super(context);
        this.startTime = startTime;
        this.endTime=endTime;
        this.onStatisticsCallBack=onStatisticsCallBack;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext,
                R.layout.statistics_merchant_pager, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(mContext, true);
        receivablesStatisticsBeanList = new ArrayList<>();
        lv_pending_order.setMode(PullToRefreshBase.Mode.BOTH);
        lv_pending_order.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        lv_pending_order.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
        lv_pending_order.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以刷新");
        lv_pending_order.setOnRefreshListener(this);

        merchantBean = SPUtils.getBeanFromSp(mContext, "businessObject", "MerchantBean");



    }

    private void setData() {
        receivablesStatisticsBeanList.addAll(getReceivablesStatisticsResponse.getDataList());
        if (statisticsMerchantAdapter == null) {
            statisticsMerchantAdapter = new StatisticsMerchantAdapter( mContext, receivablesStatisticsBeanList);
            lv_pending_order.setAdapter(statisticsMerchantAdapter);
        } else {
            statisticsMerchantAdapter.setDate(receivablesStatisticsBeanList);
            statisticsMerchantAdapter.notifyDataSetChanged();
        }
    }

    public void getReceivablesList() {
        loadingDialog.show();
        GetReceivablesStatisticsProtocol getReceivablesStatisticsDescProtocol=new GetReceivablesStatisticsProtocol();
        url = getReceivablesStatisticsDescProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("businessId", String.valueOf(merchantBean.getId()));
        params.put("startDate", startTime);
        params.put("endDate", endTime);
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", "100");
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getReceivablesStatisticsResponse = gson.fromJson(json, GetReceivablesStatisticsResponse.class);
                LogUtils.e("getReceivablesStatisticsResponse:" + getReceivablesStatisticsResponse.toString());
                if (getReceivablesStatisticsResponse.getCode() == 0) {
                    onStatisticsCallBack.CallBackData(getReceivablesStatisticsResponse.getData().getTotalMoney(),getReceivablesStatisticsResponse.getData().getTotalCount());
                    if (getReceivablesStatisticsResponse.getDataList().size() > 0) {
                        if (pageNo == 1) {
                            receivablesStatisticsBeanList.clear();
                        }
                        setData();

                    } else {
                        if(pageNo==1){
                            ll_empty.setVisibility(View.VISIBLE);
                            lv_pending_order.setVisibility(View.GONE);
                        }else {
                            DialogUtils.showAlertDialog(mContext, "没有更多数据了！");
                        }

                    }

                }else {
                    if(getReceivablesStatisticsResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getReceivablesStatisticsResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(mContext, getReceivablesStatisticsResponse.msg);
                    }
                }
                if (isRefresh) {
                    LogUtils.e("结束");
                    isRefresh = false;
                    lv_pending_order.onRefreshComplete();
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
                if (isRefresh) {
                    isRefresh = false;
                    lv_pending_order.onRefreshComplete();
                }
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    public void searchReceivablesList(String start,String end,String businessShopId) {
        startTime=start;
        endTime=end;
        loadingDialog.show();
        GetReceivablesStatisticsProtocol getReceivablesStatisticsDescProtocol=new GetReceivablesStatisticsProtocol();
        url = getReceivablesStatisticsDescProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        if(!TextUtils.isEmpty(businessShopId)){
            params.put("businessShopId", businessShopId);
        }
        params.put("businessId", String.valueOf(merchantBean.getId()));
        params.put("startDate", start);
        params.put("endDate", end);
        params.put("pageNo", String.valueOf(1));
        params.put("pageSize", "100");
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getReceivablesStatisticsResponse = gson.fromJson(json, GetReceivablesStatisticsResponse.class);
                LogUtils.e("getReceivablesStatisticsResponse:" + getReceivablesStatisticsResponse.toString());
                if (getReceivablesStatisticsResponse.getCode() == 0) {
                    pageNo = 1;
                    receivablesStatisticsBeanList.clear();
                    onStatisticsCallBack.CallBackData(getReceivablesStatisticsResponse.getData().getTotalMoney(),getReceivablesStatisticsResponse.getData().getTotalCount());
                    if (getReceivablesStatisticsResponse.getDataList().size() > 0) {
                        setData();
                        ll_empty.setVisibility(View.GONE);
                        lv_pending_order.setVisibility(View.VISIBLE);
                    } else {
                            ll_empty.setVisibility(View.VISIBLE);
                            lv_pending_order.setVisibility(View.GONE);
                    }

                }else {
                    if(getReceivablesStatisticsResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getReceivablesStatisticsResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(mContext, getReceivablesStatisticsResponse.msg);
                    }
                }
                if (isRefresh) {
                    LogUtils.e("结束");
                    isRefresh = false;
                    lv_pending_order.onRefreshComplete();
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
                if (isRefresh) {
                    isRefresh = false;
                    lv_pending_order.onRefreshComplete();
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
            if (lv_pending_order.isHeaderShown()) {
                pageNo = 1;
                receivablesStatisticsBeanList.clear();
                getReceivablesList();
            } else if (lv_pending_order.isFooterShown()) {
                pageNo++;
                getReceivablesList();
            }
        }
    }


}