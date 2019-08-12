package com.ciba.wholefinancial.tabpager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.activity.MerchantInfoActivity;
import com.ciba.wholefinancial.activity.OrderInfoActivity;
import com.ciba.wholefinancial.activity.UpdateShopActivity;
import com.ciba.wholefinancial.adapter.BusinessShopAdapter;
import com.ciba.wholefinancial.adapter.OrderAdapter;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.BusinessLoginBean;
import com.ciba.wholefinancial.bean.BusinessShopBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.OrderBean;
import com.ciba.wholefinancial.protocol.GetBusinessShopListProtocol;
import com.ciba.wholefinancial.protocol.GetOrderListProtocol;
import com.ciba.wholefinancial.response.GetBusinessShopListResponse;
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
public class BusinessShopPager extends ViewTabBasePager  {
    // 分类listview
    @ViewInject(R.id.lv_business_shop)
    private ListView lv_business_shop;
    @ViewInject(R.id.ll_empty)
    private LinearLayout ll_empty;

    @ViewInject(R.id.ll_main)
    private LinearLayout ll_main;
    private BusinessShopAdapter businessShopAdapter;
    private Dialog loadingDialog;
    private String url;
    private GetBusinessShopListResponse getBusinessShopListResponse;
    private Gson gson;
    private List<BusinessShopBean> businessShopBeanList;

    private int status;
    private MerchantBean merchantBean;
    private SelectShopPopuwindow selectShopPopuwindow;
    public BusinessShopPager(Context context, int status) {
        super(context);
        this.status = status;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext,
                R.layout.business_shop_pager, null);
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
        businessShopBeanList = new ArrayList<>();


        merchantBean= SPUtils.getBeanFromSp(mContext,"businessObject","MerchantBean");
        if(merchantBean!=null){
            getShopList();
        }
        lv_business_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(mContext,UpdateShopActivity.class);
                intent.putExtra("BusinessShopBean",businessShopBeanList.get(i-1));
                UIUtils.startActivityNextAnim(intent);
            }
        });
        final List<String> testData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            testData.add(""+i);
        }

    }
    //SchedulePopuwindow为弹出窗口实现监听类
    private AdapterView.OnItemClickListener itemsOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            String value = testData.get(mSpinnerPopuwindow.getText());
//            tv_year.setText(value);
            selectShopPopuwindow.dismissPopupWindow();
        }
    };
    private void setData() {
        businessShopBeanList.clear();
        businessShopBeanList.addAll(getBusinessShopListResponse.getDataList());
        if (businessShopAdapter == null) {
            businessShopAdapter = new BusinessShopAdapter( mContext, businessShopBeanList);
            lv_business_shop.setAdapter(businessShopAdapter);
        } else {
            businessShopAdapter.setDate(businessShopBeanList);
            businessShopAdapter.notifyDataSetChanged();
        }
        lv_business_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(mContext, UpdateShopActivity.class);
                intent.putExtra("BusinessShopBean",businessShopBeanList.get(i));
                UIUtils.startActivityForResultNextAnim(intent,1);
            }
        });
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
        LogUtils.e("getBusinessShopListResponse:" + params.toString());

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getBusinessShopListResponse = gson.fromJson(json, GetBusinessShopListResponse.class);
                LogUtils.e("getBusinessShopListResponse:" + getBusinessShopListResponse.toString());
                if (getBusinessShopListResponse.getCode() == 0) {
                    if (getBusinessShopListResponse.getDataList().size() > 0) {
                        setData();
                    } else {
                        ll_empty.setVisibility(View.VISIBLE);
                        lv_business_shop.setVisibility(View.GONE);
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