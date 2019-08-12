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
import com.ciba.wholefinancial.activity.UpdateCodeActivity;
import com.ciba.wholefinancial.adapter.OrderAdapter;
import com.ciba.wholefinancial.adapter.TableCodeAdapter;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.OrderBean;
import com.ciba.wholefinancial.bean.TableCodeBean;
import com.ciba.wholefinancial.protocol.GetBusinessShopListProtocol;
import com.ciba.wholefinancial.protocol.GetOrderListProtocol;
import com.ciba.wholefinancial.protocol.GetTableCodeListProtocol;
import com.ciba.wholefinancial.response.GetBusinessShopListResponse;
import com.ciba.wholefinancial.response.GetOrderListResponse;
import com.ciba.wholefinancial.response.GetTableCodeResponse;
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
 * @更新内容: TODO餐桌码
 */
public class TableCodePager extends ViewTabBasePager  {
    // 分类listview
    @ViewInject(R.id.lv_table_code)
    private ListView lv_table_code;
    @ViewInject(R.id.ll_empty)
    private LinearLayout ll_empty;
    @ViewInject(R.id.tv_select_shop)
    private TextView tv_select_shop;
    @ViewInject(R.id.ll_main)
    private LinearLayout ll_main;
    private TableCodeAdapter tableCodeAdapter;
    private Dialog loadingDialog;
    private String url;
    private GetTableCodeResponse getTableCodeResponse;
    private Gson gson;
    private List<TableCodeBean> tableCodeBeanList;

    private int status;
    private MerchantBean merchantBean;
    private SelectShopPopuwindow selectShopPopuwindow;
    private List<String> shopList;
    private int businessShopId;
    private String shop_name;
    private GetBusinessShopListResponse getBusinessShopListResponse;
    public TableCodePager(Context context, int status) {
        super(context);
        this.status = status;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext,
                R.layout.table_code_pager, null);
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
        tableCodeBeanList = new ArrayList<>();
        shopList=new ArrayList<>();

        merchantBean= SPUtils.getBeanFromSp(mContext,"businessObject","MerchantBean");
        if(merchantBean!=null){
            getTableCodeList();
        }
        lv_table_code.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(mContext, UpdateCodeActivity.class);
                intent.putExtra("TableCodeBean",tableCodeBeanList.get(i));
                UIUtils.startActivityForResultNextAnim(intent,2);
            }
        });
        final List<String> testData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            testData.add(""+i);
        }
        tv_select_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shopList.size()<=0) {
                    getShopList();
                }else{
                    selectShopPopuwindow.showPopupWindow(ll_main);
                }

            }
        });
    }

    public int getBusinessShopId() {
        return businessShopId;
    }

    public String getShop_name() {
        return shop_name;
    }

    //SchedulePopuwindow为弹出窗口实现监听类
    private AdapterView.OnItemClickListener itemsOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            businessShopId=getBusinessShopListResponse.getDataList().get(position).getId();
            shop_name=getBusinessShopListResponse.getDataList().get(position).getShopName();
            String value = shopList.get(selectShopPopuwindow.getText());
            tv_select_shop.setText(value);
            selectShopPopuwindow.dismissPopupWindow();
            getTableCodeList();
        }
    };
    private void setData() {
        tableCodeBeanList.addAll(getTableCodeResponse.getDataList());
        if (tableCodeAdapter == null) {
            tableCodeAdapter = new TableCodeAdapter( mContext, tableCodeBeanList);
            lv_table_code.setAdapter(tableCodeAdapter);
        } else {
            tableCodeAdapter.setDate(tableCodeBeanList);
            tableCodeAdapter.notifyDataSetChanged();
        }
    }

    public void getTableCodeList() {
        loadingDialog.show();
        GetTableCodeListProtocol getTableCodeListProtocol=new GetTableCodeListProtocol();
        url = getTableCodeListProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("businessId", String.valueOf(merchantBean.getId()));
        if(businessShopId>0){
            params.put("businessShopId", String.valueOf(businessShopId));
        }


        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getTableCodeResponse = gson.fromJson(json, GetTableCodeResponse.class);
                LogUtils.e("getTableCodeResponse:" + getTableCodeResponse.toString());
                if (getTableCodeResponse.getCode() == 0) {
                    if (getTableCodeResponse.getDataList().size() > 0) {
                        tableCodeBeanList.clear();
                        setData();
                        ll_empty.setVisibility(View.GONE);
                        lv_table_code.setVisibility(View.VISIBLE);
                    } else {
                        ll_empty.setVisibility(View.VISIBLE);
                        lv_table_code.setVisibility(View.GONE);
                    }

                } else {
                    if(getTableCodeResponse.msg.indexOf("此账号在其他地方登陆")!=-1){

                        DialogUtils.showAlertToLoginDialog(mContext,
                                getTableCodeResponse.msg);
                    }else{
                        DialogUtils.showAlertDialog(mContext, getTableCodeResponse.msg);
                    }
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
                        for (int i=0;i<getBusinessShopListResponse.getDataList().size();i++){
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