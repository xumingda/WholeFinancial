package com.ciba.wholefinancial.tabpager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.activity.OrderInfoActivity;
import com.ciba.wholefinancial.adapter.OrderAdapter;
import com.ciba.wholefinancial.adapter.StatisticsAdapter;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.OrderBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.callback.OnStatisticsCallBack;
import com.ciba.wholefinancial.protocol.GetBusinessShopListProtocol;
import com.ciba.wholefinancial.protocol.GetOrderListProtocol;
import com.ciba.wholefinancial.protocol.GetReceivablesStatisticsDescProtocol;
import com.ciba.wholefinancial.response.GetBusinessShopListResponse;
import com.ciba.wholefinancial.response.GetOrderListResponse;
import com.ciba.wholefinancial.response.GetReceivablesStatisticsDescResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
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
public class StatisticsPager extends ViewTabBasePager implements
        PullToRefreshBase.OnRefreshListener {
    // 分类listview
    @ViewInject(R.id.lv_pending_order)
    private PullToRefreshListView lv_pending_order;
    @ViewInject(R.id.ll_empty)
    private LinearLayout ll_empty;

    @ViewInject(R.id.ll_main)
    private LinearLayout ll_main;
    private StatisticsAdapter statisticsAdapter;
    private Dialog loadingDialog;
    private String url;
    private GetReceivablesStatisticsDescResponse getReceivablesStatisticsDescResponse;
    private Gson gson;
    private List<GetReceivablesStatisticsDescResponse.ReceivablesStatisticsDescBean> receivablesStatisticsDescBeanList;
    private int pageNo = 1;
    //判断是否刷新
    private boolean isRefresh = false;
    private String startTime;
    private String endTime;
//    private MerchantBean merchantBean;
    private SalesmanBean salesmanBean;
    private SelectShopPopuwindow selectShopPopuwindow;
    private  List<String> shopList;
    private OnStatisticsCallBack onStatisticsCallBack;
    public StatisticsPager(Context context, String startTime, String endTime, OnStatisticsCallBack onStatisticsCallBack) {
        super(context);
        this.startTime = startTime;
        this.endTime=endTime;
        this.onStatisticsCallBack=onStatisticsCallBack;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext,
                R.layout.statistics_pager, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        shopList=new ArrayList<>();
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(mContext, true);
        receivablesStatisticsDescBeanList = new ArrayList<>();
        lv_pending_order.setMode(PullToRefreshBase.Mode.BOTH);
        lv_pending_order.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        lv_pending_order.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
        lv_pending_order.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以刷新");
        lv_pending_order.setOnRefreshListener(this);

        salesmanBean= SPUtils.getBeanFromSp(mContext,"salesmanObject","SalesmanBean");
//        if(salesmanBean!=null){
//            getReceivablesList();
//        }
//        lv_pending_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent=new Intent(mContext,OrderInfoActivity.class);
//                intent.putExtra("eatType",orderBeanList.get(i-1).getEatType());
//                intent.putExtra("orderId",orderBeanList.get(i-1).getOrderId());
//                UIUtils.startActivityNextAnim(intent);
//            }
//        });


    }

    private void setData() {
        receivablesStatisticsDescBeanList.addAll(getReceivablesStatisticsDescResponse.getDataList());
        if (statisticsAdapter == null) {
            statisticsAdapter = new StatisticsAdapter( mContext, receivablesStatisticsDescBeanList);
            lv_pending_order.setAdapter(statisticsAdapter);
        } else {
            statisticsAdapter.setDate(receivablesStatisticsDescBeanList);
            statisticsAdapter.notifyDataSetChanged();
        }
    }

    public void getReceivablesList() {
        loadingDialog.show();
        GetReceivablesStatisticsDescProtocol getReceivablesStatisticsDescProtocol=new GetReceivablesStatisticsDescProtocol();
        url = getReceivablesStatisticsDescProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
//        params.put("businessId", String.valueOf(merchantBean.getId()));
        params.put("salesmanId", String.valueOf(salesmanBean.getId()));
        params.put("startDate", startTime);
        params.put("endDate", endTime);
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", "100");
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getReceivablesStatisticsDescResponse = gson.fromJson(json, GetReceivablesStatisticsDescResponse.class);
                LogUtils.e("getReceivablesStatisticsDescResponse:" + getReceivablesStatisticsDescResponse.toString());
                if (getReceivablesStatisticsDescResponse.getCode() == 0) {
                    onStatisticsCallBack.CallBackData(getReceivablesStatisticsDescResponse.getData().getTotalMoney(),"");
                    if (getReceivablesStatisticsDescResponse.getDataList().size() > 0) {
                        if (pageNo == 1) {
                            receivablesStatisticsDescBeanList.clear();
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
                    if(getReceivablesStatisticsDescResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getReceivablesStatisticsDescResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(mContext, getReceivablesStatisticsDescResponse.msg);
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

    public void searchReceivablesList(String start,String end,String businessId) {
        startTime=start;
        endTime=end;
        loadingDialog.show();
        GetReceivablesStatisticsDescProtocol getReceivablesStatisticsDescProtocol=new GetReceivablesStatisticsDescProtocol();
        url = getReceivablesStatisticsDescProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        if(!TextUtils.isEmpty(businessId)){
            params.put("businessId", businessId);
        }
        params.put("salesmanId", String.valueOf(salesmanBean.getId()));
        params.put("startDate", start);
        params.put("endDate", end);
        params.put("pageNo", String.valueOf(1));
        params.put("pageSize", "100");
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getReceivablesStatisticsDescResponse = gson.fromJson(json, GetReceivablesStatisticsDescResponse.class);
                LogUtils.e("getReceivablesStatisticsDescResponse:" + getReceivablesStatisticsDescResponse.toString());
                if (getReceivablesStatisticsDescResponse.getCode() == 0) {
                    pageNo = 1;
                    receivablesStatisticsDescBeanList.clear();
                    onStatisticsCallBack.CallBackData(getReceivablesStatisticsDescResponse.getData().getTotalMoney(),"");
                    if (getReceivablesStatisticsDescResponse.getDataList().size() > 0) {
                        setData();
                        ll_empty.setVisibility(View.GONE);
                        lv_pending_order.setVisibility(View.VISIBLE);
                    } else {
                            ll_empty.setVisibility(View.VISIBLE);
                            lv_pending_order.setVisibility(View.GONE);
                    }

                }else {
                    if(getReceivablesStatisticsDescResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getReceivablesStatisticsDescResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(mContext, getReceivablesStatisticsDescResponse.msg);
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
                receivablesStatisticsDescBeanList.clear();
                getReceivablesList();
            } else if (lv_pending_order.isFooterShown()) {
                pageNo++;
                getReceivablesList();
            }
        }
    }


}