package com.ciba.wholefinancial.tabpager;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.adapter.MerchantAdapter;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.protocol.GetMerchantProtocol;
import com.ciba.wholefinancial.response.GetMerchantListResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
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
public class NoValidMerchantPager extends ViewTabBasePager implements
        PullToRefreshBase.OnRefreshListener {
    // 分类listview
    @ViewInject(R.id.lv_novaild_merchant)
    private PullToRefreshListView lv_novaild_merchant;
    private MerchantAdapter merchantAdapter;
    private Dialog loadingDialog;
    private String url;
    private GetMerchantListResponse getMerchantListResponse;
    private Gson gson;
    private List<MerchantBean> merchantBeanList;
    private int pageNo = 1;
    //判断是否刷新
    private boolean isRefresh = false;
    private boolean isFirst;
    private SalesmanBean salesmanBean;
    public NoValidMerchantPager(Context context, boolean isFirst) {
        super(context);
        this.isFirst = isFirst;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext,
                R.layout.novaild_merchant_pager, null);
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
        merchantBeanList = new ArrayList<>();
        lv_novaild_merchant.setMode(PullToRefreshBase.Mode.BOTH);
        lv_novaild_merchant.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        lv_novaild_merchant.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
        lv_novaild_merchant.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以刷新");
        lv_novaild_merchant.setOnRefreshListener(this);

//        url = "video/getVideoList.do";
//        String json = SharedPrefrenceUtils.getString(mContext, url + "3");
//        if (json.equals("null") || TextUtils.isEmpty(json) || isFirst) {
//            LogUtils.e("json:" + isFirst);
////            getViedeoList();
//        } else {
//            videoListResponse = gson.fromJson(json, VideoListResponse.class);
//            setData();
//        }
        salesmanBean= SPUtils.getBeanFromSp(mContext,"salesmanObject","SalesmanBean");
        if(salesmanBean!=null){
            getMerchantList();
        }
    }

    private void setData() {
        merchantBeanList.addAll(getMerchantListResponse.getDataList());
        if (merchantAdapter == null) {
            merchantAdapter = new MerchantAdapter( mContext, merchantBeanList,0);
            lv_novaild_merchant.setAdapter(merchantAdapter);
            LogUtils.e("设置数据"+merchantBeanList.size());
        } else {
            merchantAdapter.notifyDataSetChanged();
        }
    }

    private void getMerchantList() {
        loadingDialog.show();
        GetMerchantProtocol getMerchantProtocol=new GetMerchantProtocol();
        url = getMerchantProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("status", "1");
        params.put("effectiveStatus", "0");
        params.put("salesmanId",  String.valueOf(salesmanBean.getId()));
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", "100");
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getMerchantListResponse = gson.fromJson(json, GetMerchantListResponse.class);
                LogUtils.e("getMerchantListResponse:" + getMerchantListResponse.toString());
                if (getMerchantListResponse.getCode() == 0) {
                    isFirst = false;
                    if (getMerchantListResponse.getDataList().size() > 0) {
                        if (pageNo == 1) {
                            merchantBeanList.clear();
//                            SharedPrefrenceUtils.setString(mContext, url + "3", json);
                        }
                        setData();
                    } else {
                         DialogUtils.showAlertDialog(mContext, "没有更多数据了！");
                    }

                } else {
                    DialogUtils.showAlertDialog(mContext, getMerchantListResponse.getMsg());
                }
                if (isRefresh) {
                    LogUtils.e("结束");
                    isRefresh = false;
                    lv_novaild_merchant.onRefreshComplete();
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(mContext, error);
                if (isRefresh) {
                    isRefresh = false;
                    lv_novaild_merchant.onRefreshComplete();
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
            if (lv_novaild_merchant.isHeaderShown()) {
                pageNo = 1;
                merchantBeanList.clear();
                getMerchantList();
            } else if (lv_novaild_merchant.isFooterShown()) {
                pageNo++;
                getMerchantList();
            }
        }
    }
}