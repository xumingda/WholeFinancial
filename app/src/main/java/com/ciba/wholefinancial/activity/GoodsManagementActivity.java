package com.ciba.wholefinancial.activity;

import android.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.adapter.BusinessLoginAdapter;
import com.ciba.wholefinancial.adapter.ClassAdapter;
import com.ciba.wholefinancial.adapter.GoodsAdapter;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.BusinessLoginBean;
import com.ciba.wholefinancial.bean.ClassBean;
import com.ciba.wholefinancial.bean.GoodsBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.callback.OnClassNameCallBack;
import com.ciba.wholefinancial.callback.OnDeleteCallBack;
import com.ciba.wholefinancial.protocol.AddClassProtocol;
import com.ciba.wholefinancial.protocol.DeleteClassProtocol;
import com.ciba.wholefinancial.protocol.DeleteGoodProtocol;
import com.ciba.wholefinancial.protocol.GetClassListProtocol;
import com.ciba.wholefinancial.protocol.GetGoodsListProtocol;
import com.ciba.wholefinancial.protocol.GetLoginListProtocol;
import com.ciba.wholefinancial.protocol.UpdateClassProtocol;
import com.ciba.wholefinancial.response.DeleteLoginResponse;
import com.ciba.wholefinancial.response.GetClassListResponse;
import com.ciba.wholefinancial.response.GetGoodsListResponse;
import com.ciba.wholefinancial.response.GetLoginListResponse;
import com.ciba.wholefinancial.response.UpdateMarketingResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.SuccessPopuwindow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//菜品管理
public class GoodsManagementActivity extends BaseActivity implements View.OnClickListener,  PullToRefreshBase.OnRefreshListener , OnClassNameCallBack , OnDeleteCallBack {

    private LayoutInflater mInflater;
    private View rootView;

    private View view_back, view_add;
    private int businessId;

    private MerchantBean merchantBean;
    private String url;
    private Gson gson;
    private List<ClassBean> classBeanList;
    private Dialog loadingDialog;
    GetClassListResponse getClassListResponse;
    private ClassAdapter classAdapter;
    private RelativeLayout ll_main;
    private PullToRefreshListView lv_goods;
    private int pageNo = 1;
    private String className;
    private AlertDialog alertDialog;
    //判断是否刷新
    private boolean isRefresh = false;
    private String classId;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_good_management, null);
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
        loadingDialog = DialogUtils.createLoadDialog(GoodsManagementActivity.this, false);
        merchantBean = SPUtils.getBeanFromSp(this, "businessObject", "MerchantBean");
        classBeanList = new ArrayList<>();

        businessId = getIntent().getIntExtra("businessId", 0);
        ll_main=(RelativeLayout)findViewById(R.id.rl_main);
        view_back = (View) findViewById(R.id.view_back);
        view_add = (View) findViewById(R.id.view_add);
        lv_goods = (PullToRefreshListView) findViewById(R.id.lv_goods);
        lv_goods.setMode(PullToRefreshBase.Mode.BOTH);
        lv_goods.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        lv_goods.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
        lv_goods.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以刷新");
        lv_goods.setOnRefreshListener(this);
        view_back.setOnClickListener(this);
        view_add.setOnClickListener(this);
        if (merchantBean != null) {
            getGoodList();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_back: {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
                break;
            }
            case R.id.view_add: {
                DialogUtils.createInputDialog(GoodsManagementActivity.this,"","",0,GoodsManagementActivity.this);
                break;
            }
            case R.id.tv_ensure:{
                alertDialog.cancel();
                deleteClass();
                break;
            }
        }
    }
    public void deleteClass() {
        loadingDialog.show();
        DeleteClassProtocol deleteClassProtocol = new DeleteClassProtocol();

        String url = deleteClassProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("classId",  classId);

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                DeleteLoginResponse deleteLoginResponse = gson.fromJson(json, DeleteLoginResponse.class);
                LogUtils.e("deleteLoginResponse:" + deleteLoginResponse.toString());
                if (deleteLoginResponse.code==0) {
                    getGoodList();
                } else {

                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(GoodsManagementActivity.this,
                            deleteLoginResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(GoodsManagementActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(GoodsManagementActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }
    private void setData() {
        classBeanList.addAll(getClassListResponse.getDataList());
        if (classAdapter == null) {
            classAdapter = new ClassAdapter(GoodsManagementActivity.this, classBeanList,GoodsManagementActivity.this);
            lv_goods.setAdapter(classAdapter);
            lv_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    classId=String.valueOf(classBeanList.get(i-1).getClassId());
                    DialogUtils.createInputDialog(GoodsManagementActivity.this,"",classBeanList.get(i-1).getClassName(),1,GoodsManagementActivity.this);
                }
            });
        } else {
            classAdapter.setDate(classBeanList);
            classAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            getGoodList();
        }
    }

    private void getGoodList() {
        loadingDialog.show();
        GetClassListProtocol getClassListProtocol = new GetClassListProtocol();
        url = getClassListProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("businessId", String.valueOf(merchantBean.getId()));
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", "100");
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                getClassListResponse = gson.fromJson(json, GetClassListResponse.class);
                LogUtils.e("getClassListResponse:" + getClassListResponse.toString());
                if (getClassListResponse.getCode() == 0) {
                    if (getClassListResponse.getDataList().size() > 0) {
                        if (pageNo == 1) {
                            classBeanList.clear();
//                            SharedPrefrenceUtils.setString(mContext, url + "3", json);
                        }
                        setData();
                    } else {
                        DialogUtils.showAlertDialog(GoodsManagementActivity.this, "没有更多数据了！");
                    }

                } else {
                    DialogUtils.showAlertDialog(GoodsManagementActivity.this, getClassListResponse.getMsg());
                }
                if (isRefresh) {
                    LogUtils.e("结束");
                    isRefresh = false;
                    lv_goods.onRefreshComplete();
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(GoodsManagementActivity.this, error);
                if (isRefresh) {
                    isRefresh = false;
                    lv_goods.onRefreshComplete();
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
            if (lv_goods.isHeaderShown()) {
                pageNo = 1;
                classBeanList.clear();
                getGoodList();
            } else if (lv_goods.isFooterShown()) {
                pageNo++;
                getGoodList();
            }
        }
    }
    private void addClass() {
        loadingDialog.show();
        AddClassProtocol addClassProtocol=new AddClassProtocol();
        url = addClassProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("businessId",  String.valueOf(merchantBean.getId()));
        params.put("className", className);

//        typeJsonArray	活动类型相应的对象数组
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                UpdateMarketingResponse updateMarketingResponse = gson.fromJson(json, UpdateMarketingResponse.class);
                LogUtils.e("updateMarketingResponse:" + updateMarketingResponse.toString());
                if (updateMarketingResponse.getCode() == 0) {
                    SuccessPopuwindow successPopuwindow=new SuccessPopuwindow(GoodsManagementActivity.this,null,"添加成功");
                    successPopuwindow.showPopupWindow(ll_main);
                    pageNo=1;
                    getGoodList();
                } else {
                    DialogUtils.showAlertDialog(GoodsManagementActivity.this, updateMarketingResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(GoodsManagementActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
    private void updateClass() {
        loadingDialog.show();
        UpdateClassProtocol updateClassProtocol=new UpdateClassProtocol();
        url = updateClassProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("classId",  classId);
        params.put("className", className);
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {
                classId=null;
                loadingDialog.dismiss();
                UpdateMarketingResponse updateMarketingResponse = gson.fromJson(json, UpdateMarketingResponse.class);
                LogUtils.e("update:" + updateMarketingResponse.toString());
                if (updateMarketingResponse.getCode() == 0) {
                    SuccessPopuwindow successPopuwindow=new SuccessPopuwindow(GoodsManagementActivity.this,null,"修改成功");
                    successPopuwindow.showPopupWindow(ll_main);
                    pageNo=1;
                    getGoodList();
                } else {
                    DialogUtils.showAlertDialog(GoodsManagementActivity.this, updateMarketingResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                classId=null;
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(GoodsManagementActivity.this, error);

            }

            @Override
            public void dealTokenOverdue() {
                classId=null;
            }
        });
    }
    @Override
    public void CallBackClassName(String classnName) {
        className=classnName;
        if(classId!=null&&classId!=""){
            updateClass();
        }else{
            addClass();
        }

    }

    @Override
    public void deleteSuccessCallBack(int classId) {
        this.classId=String.valueOf(classId);
        alertDialog=DialogUtils.showAlertDoubleBtnDialog(GoodsManagementActivity.this,"删除分类？","",1, GoodsManagementActivity.this);
    }
}