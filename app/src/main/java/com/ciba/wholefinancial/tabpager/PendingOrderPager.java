package com.ciba.wholefinancial.tabpager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.activity.MerchantInfoActivity;
import com.ciba.wholefinancial.activity.OrderInfoActivity;
import com.ciba.wholefinancial.adapter.MerchantAdapter;
import com.ciba.wholefinancial.adapter.OrderAdapter;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.OrderBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.protocol.GetBusinessShopListProtocol;
import com.ciba.wholefinancial.protocol.GetMerchantProtocol;
import com.ciba.wholefinancial.protocol.GetOrderListProtocol;
import com.ciba.wholefinancial.response.GetBusinessShopListResponse;
import com.ciba.wholefinancial.response.GetMerchantListResponse;
import com.ciba.wholefinancial.response.GetOrderListResponse;
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
public class PendingOrderPager extends ViewTabBasePager implements
        PullToRefreshBase.OnRefreshListener {
    // 分类listview
    @ViewInject(R.id.lv_pending_order)
    private PullToRefreshListView lv_pending_order;
    @ViewInject(R.id.ll_empty)
    private LinearLayout ll_empty;
    @ViewInject(R.id.tv_select_shop)
    private TextView tv_select_shop;
    @ViewInject(R.id.ll_main)
    private LinearLayout ll_main;
    private OrderAdapter orderAdapter;
    private Dialog loadingDialog;
    private String url;
    private GetOrderListResponse getOrderListResponse;
    private Gson gson;
    private List<OrderBean> orderBeanList;
    private int pageNo = 1;
    //判断是否刷新
    private boolean isRefresh = false;
    private int status;
    private MerchantBean merchantBean;
    private SelectShopPopuwindow selectShopPopuwindow;
    private GetBusinessShopListResponse getBusinessShopListResponse;
    private  List<String> shopList;
    public PendingOrderPager(Context context, int status) {
        super(context);
        this.status = status;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext,
                R.layout.pending_order_pager, null);
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
        orderBeanList = new ArrayList<>();
        lv_pending_order.setMode(PullToRefreshBase.Mode.BOTH);
        lv_pending_order.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        lv_pending_order.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
        lv_pending_order.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以刷新");
        lv_pending_order.setOnRefreshListener(this);

        merchantBean= SPUtils.getBeanFromSp(mContext,"businessObject","MerchantBean");
        if(merchantBean!=null){
            getOrderList();
        }
        lv_pending_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(mContext,OrderInfoActivity.class);
                intent.putExtra("status",orderBeanList.get(i-1).getStatus());
                intent.putExtra("eatType",orderBeanList.get(i-1).getEatType());
                intent.putExtra("orderId",orderBeanList.get(i-1).getOrderId());
                UIUtils.startActivityNextAnim(intent);
            }
        });
        tv_select_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getShopList();


            }
        });

    }
    //SchedulePopuwindow为弹出窗口实现监听类
    private AdapterView.OnItemClickListener itemsOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String value = shopList.get(position);
            tv_select_shop.setText(value);
            selectShopPopuwindow.dismissPopupWindow();
        }
    };
    private void setData() {
        orderBeanList.addAll(getOrderListResponse.getDataList());
        if (orderAdapter == null) {
            orderAdapter = new OrderAdapter( mContext, orderBeanList);
            lv_pending_order.setAdapter(orderAdapter);
        } else {
            orderAdapter.setDate(orderBeanList);
            orderAdapter.notifyDataSetChanged();
        }
    }

    private void getOrderList() {
        loadingDialog.show();
        GetOrderListProtocol getOrderListProtocol=new GetOrderListProtocol();
        url = getOrderListProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("businessId", String.valueOf(merchantBean.getId()));
//        params.put("businessShopId", String.valueOf(merchantBean.getAgentId()));
        if(status>-1){
            params.put("status",  String.valueOf(status));
        }else{
            params.put("status",  "");
        }

        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", "100");
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getOrderListResponse = gson.fromJson(json, GetOrderListResponse.class);
                LogUtils.e("getOrderListResponse:" + getOrderListResponse.toString());
                if (getOrderListResponse.getCode() == 0) {
                    if (getOrderListResponse.getDataList().size() > 0) {
                        if (pageNo == 1) {
                            orderBeanList.clear();
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
                    if(getOrderListResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getOrderListResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(mContext, getOrderListResponse.msg);
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
                orderBeanList.clear();
                getOrderList();
            } else if (lv_pending_order.isFooterShown()) {
                pageNo++;
                getOrderList();
            }
        }
    }

    public void getShopList() {
        loadingDialog.show();
        GetBusinessShopListProtocol getBusinessShopListProtocol=new GetBusinessShopListProtocol();
        url = getBusinessShopListProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("businessId", String.valueOf(merchantBean.getId()));
//        params.put("businessShopId", String.valueOf(merchantBean.getAgentId()));
        if(status>0){
            params.put("status",  String.valueOf(status));
        }


        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getBusinessShopListResponse = gson.fromJson(json, GetBusinessShopListResponse.class);
                LogUtils.e("getBusinessShopListResponse:" + getBusinessShopListResponse.toString());
                if (getBusinessShopListResponse.getCode() == 0) {
                    if (getBusinessShopListResponse.getDataList().size() > 0) {
                        shopList.clear();
                        for(int i=0;i<getBusinessShopListResponse.getDataList().size();i++){
                            shopList.add(getBusinessShopListResponse.getDataList().get(i).shopName);
                        }
                        selectShopPopuwindow = new SelectShopPopuwindow(UIUtils.getActivity(),"所有店铺",shopList,itemsOnClick);
                        selectShopPopuwindow.showPopupWindow(ll_main);
                    }

                } else {
                    DialogUtils.showAlertDialog(mContext, getBusinessShopListResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
}