package com.ciba.wholefinancial.tabpager;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.callback.OnRequestCallBack;
import com.ciba.wholefinancial.protocol.GetMerchantInfoProtocol;
import com.ciba.wholefinancial.protocol.GetMerchantRevenueProtocol;
import com.ciba.wholefinancial.response.GetMerchantInfoResponse;
import com.ciba.wholefinancial.response.GetMerchantListResponse;
import com.ciba.wholefinancial.response.GetMerchantRevenueResponse;
import com.ciba.wholefinancial.util.DateUtils;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.InComePopuwindow;
import com.ciba.wholefinancial.weiget.SelectTimePopuwindow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.ViewUtils;

import java.text.DecimalFormat;
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
public class BusinessStatePager extends ViewTabBasePager implements OnRequestCallBack {

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
    /** 模拟的假数据 */
    private List<String> testData;
    private InComePopuwindow inComePopuwindow;
    private RelativeLayout rl_type_select;
    private TextView tv_type;
    private LinearLayout ll_main;
    private GetMerchantRevenueResponse getMerchantRevenueResponse;
    private int businessId;
    private int seachType=1;
    private TextView tv_income;
    private String endTime;
    private String startTime;
    public BusinessStatePager(Context context, boolean isFirst,int businessId) {
        super(context);
        this.isFirst = isFirst;
        this.businessId=businessId;
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext,
                R.layout.business_state_pager, null);
        ViewUtils.inject(this, view);
        rl_type_select=(RelativeLayout)view.findViewById(R.id.rl_type_select);
        tv_type=(TextView)view.findViewById(R.id.tv_type);
        ll_main=(LinearLayout)view.findViewById(R.id.ll_main);
        tv_income=(TextView)view.findViewById(R.id.tv_income);
        return view;
    }

    @Override
    public void initData() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        loadingDialog = DialogUtils.createLoadDialog(mContext, true);
        tv_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = tv_type.getText().toString();
                inComePopuwindow = new InComePopuwindow(UIUtils.getActivity(),type,testData,itemsOnClick);
                inComePopuwindow.showPopupWindow(ll_main);

            }
        });
        initTypeData();
        if(businessId!=0){
            getMerchantRevenue();
        }
    }
    //SchedulePopuwindow为弹出窗口实现监听类
    private AdapterView.OnItemClickListener itemsOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            seachType=position+1;
            String value = testData.get(inComePopuwindow.getText());
            tv_type.setText(value);
            inComePopuwindow.dismissPopupWindow();
            if(position==3){
                SelectTimePopuwindow selectTimePopuwindow=new SelectTimePopuwindow(UIUtils.getActivity(),BusinessStatePager.this);
                selectTimePopuwindow.showPopupWindow(ll_main);
            }else{
                getMerchantRevenue();
            }
        }
    };

    private void initTypeData() {
        testData = new ArrayList<>();
        testData.add("今日营收");
        testData.add("近7天营收");
        testData.add("近一个月营收");
        testData.add("自定义营收时间段");
    }

    public void setDate(){
        DecimalFormat df = new java.text.DecimalFormat("#.00");
        if(getMerchantRevenueResponse.revenue>0){
            tv_income.setText("￥"+df.format(getMerchantRevenueResponse.revenue));
        }
       else{
            tv_income.setText("￥0.00");
        }
    }

    private void getMerchantRevenue() {
        loadingDialog.show();
        GetMerchantRevenueProtocol getMerchantRevenueProtocol=new GetMerchantRevenueProtocol();
        String url = getMerchantRevenueProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("businessId",  String.valueOf(businessId));
        params.put("seachType",  String.valueOf(seachType));
        if(seachType==4){
            params.put("startTime", DateUtils.formatDate(startTime)+" 00:00:00");
            params.put("endTime",  DateUtils.formatDate(endTime)+" 00:00:00");
        }

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getMerchantRevenueResponse = gson.fromJson(json, GetMerchantRevenueResponse.class);
                LogUtils.e("getMerchantRevenueResponse:" + getMerchantRevenueResponse.toString());
                if (getMerchantRevenueResponse.getCode() == 0) {
                    setDate();
                } else {
                    DialogUtils.showAlertDialog(mContext, getMerchantRevenueResponse.getMsg());
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




    @Override
    public void CallBackToRequest(String startTime, String endTime) {
        this.startTime=startTime;
        this.endTime=endTime;
        getMerchantRevenue();
    }
}