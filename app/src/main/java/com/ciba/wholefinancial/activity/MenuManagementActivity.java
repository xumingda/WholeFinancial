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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.adapter.GoodItemAdapter;
import com.ciba.wholefinancial.adapter.LeftAdapter;
import com.ciba.wholefinancial.adapter.RightAdapter;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.ClassBean;
import com.ciba.wholefinancial.adapter.MenuGoodsAdapter;
import com.ciba.wholefinancial.bean.RightMenu;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.bean.GoodsBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.callback.OnDeleteCallBack;
import com.ciba.wholefinancial.protocol.DeleteGoodProtocol;
import com.ciba.wholefinancial.protocol.GetClassListProtocol;
import com.ciba.wholefinancial.protocol.GetGoodsListProtocol;
import com.ciba.wholefinancial.response.DeleteLoginResponse;
import com.ciba.wholefinancial.response.GetClassListResponse;
import com.ciba.wholefinancial.response.GetGoodsListResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//菜单管理
public class MenuManagementActivity extends BaseActivity implements View.OnClickListener, OnDeleteCallBack {

    private LayoutInflater mInflater;
    private View rootView;
    private View view_back,view_add;
    private MerchantBean merchantBean;
    private String url;
    private Gson gson;
    private AlertDialog alertDialog;
    private Dialog loadingDialog;
    private int goodsId;
    private GoodItemAdapter goodItemAdapter;
    private MenuGoodsAdapter menuGoodsAdapter;
    private ListView leftListView,rightListView;
    private LeftAdapter leftAdapter;
    private RightAdapter rightAdapter;
    private List<RightMenu> rightMenu;
    private List<ClassBean> classBeanList;
    private List<GoodsBean> goodsBeanList;
    private int currentItem = 0;
    private int classId;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_menu_management, null);
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
        loadingDialog = DialogUtils.createLoadDialog(MenuManagementActivity.this, false);
        rightMenu = new ArrayList<RightMenu>();
        merchantBean=SPUtils.getBeanFromSp(this,"businessObject","MerchantBean");
        goodsBeanList=new ArrayList<>();

        view_back=(View)findViewById(R.id.view_back);
        view_add=(View)findViewById(R.id.view_add);
        view_back.setOnClickListener(this);
        view_add.setOnClickListener(this);

        // TODO Auto-generated method stub
        leftListView = (ListView) findViewById(R.id.list_left);
        rightListView = (ListView) findViewById(R.id.list_right);
        classBeanList=new ArrayList<>();








        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
//                leftAdapter = new LeftAdapter(MenuManagementActivity.this,classBeanList,position,"isClick");
                if(leftAdapter!=null){
                    leftAdapter.setSelect(position);
                    rightListView.setSelection(position);
                    classId=classBeanList.get(position).getClassId();
                    getGoodsList();
                }

            }
        });


        rightListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    leftAdapter = new LeftAdapter(MenuManagementActivity.this, classBeanList,currentItem);
                    leftListView.setAdapter(leftAdapter);
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                currentItem = firstVisibleItem;

            }
        });

        getClassList();

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
                Intent intent=new Intent(MenuManagementActivity.this,AddGoodsActivity.class);
                intent.putExtra("classId",String.valueOf(classId));
                UIUtils.startActivityForResultNextAnim(intent,1);
                break;
            }
            case R.id.tv_ensure:{
                alertDialog.cancel();
                deleteGoods();
                break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            LogUtils.e("class:"+classId);
            getGoodsList();
        }
    }

    //获取分类列表
    private void getClassList() {
        loadingDialog.show();
        GetClassListProtocol getClassListProtocol=new GetClassListProtocol();
        url = getClassListProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();
        params.put("businessId",  String.valueOf(merchantBean.getId()));
        params.put("pageNo", String.valueOf(1));
        params.put("pageSize", "100");
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
               GetClassListResponse getClassListResponse = gson.fromJson(json, GetClassListResponse.class);
                LogUtils.e("getClassListResponse:" + getClassListResponse.toString());
                if (getClassListResponse.getCode() == 0) {
                    if(getClassListResponse.getDataList().size()>0){
                        classBeanList.clear();
                        classBeanList.addAll(getClassListResponse.getDataList());
                        if(leftAdapter==null){
                            leftAdapter = new LeftAdapter(MenuManagementActivity.this, classBeanList,0);
                            leftListView.setAdapter(leftAdapter);
                        }else{
                            leftAdapter.setDate(classBeanList);
                            leftAdapter.notifyDataSetChanged();
                        }
                        classId=classBeanList.get(0).getClassId();
                        getGoodsList();
                    }


                } else {
                    DialogUtils.showAlertDialog(MenuManagementActivity.this, getClassListResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(MenuManagementActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }

    //获取商品列表
    private void getGoodsList() {
        loadingDialog.show();
        GetGoodsListProtocol getGoodsListProtocol=new GetGoodsListProtocol();
        url = getGoodsListProtocol.getApiFun();
        Map<String, String> params = new HashMap<String, String>();

        params.put("classId",  String.valueOf(classId));
        params.put("pageNo", String.valueOf(1));
        params.put("pageSize", "100");
        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {

            @Override
            public void dealWithJson(String address, String json) {

                loadingDialog.dismiss();
                GetGoodsListResponse getGoodsListResponse = gson.fromJson(json, GetGoodsListResponse.class);
                LogUtils.e("getGoodsListResponse:" + getGoodsListResponse.toString());
                if (getGoodsListResponse.getCode() == 0) {
                    if(getGoodsListResponse.dataList.size()>0){

                        RightMenu menu = new RightMenu();
                        menu.setRightTitle("商品标题");
                        menu.setRightMenuItem(getGoodsListResponse.dataList);
                        rightMenu.add(menu);
                        if(rightAdapter==null){
                            rightAdapter = new RightAdapter(MenuManagementActivity.this,rightMenu,MenuManagementActivity.this);
                            rightListView.setAdapter(rightAdapter);
                        }else{
                            rightAdapter.setDate(rightMenu);
                            rightAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    DialogUtils.showAlertDialog(MenuManagementActivity.this, getGoodsListResponse.getMsg());
                }

            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(MenuManagementActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {

            }
        });
    }
    public void deleteGoods() {
        loadingDialog.show();
        DeleteGoodProtocol deleteGoodProtocol = new DeleteGoodProtocol();

        String url = deleteGoodProtocol.getApiFun();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("goodsId",  String.valueOf(goodsId));

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {

                Gson gson = new Gson();
                DeleteLoginResponse deleteLoginResponse = gson.fromJson(json, DeleteLoginResponse.class);
                LogUtils.e("deleteLoginResponse:" + deleteLoginResponse.toString());
                if (deleteLoginResponse.code==0) {
                    getGoodsList();
                } else {

                    loadingDialog.dismiss();
                    DialogUtils.showAlertDialog(MenuManagementActivity.this,
                            deleteLoginResponse.msg);
                }


            }

            @Override
            public void dealWithError(String address, String error) {
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(MenuManagementActivity.this, error);
            }

            @Override
            public void dealTokenOverdue() {
                loadingDialog.dismiss();
                DialogUtils.showAlertToLoginDialog(MenuManagementActivity.this,
                        "登录超时，请重新登录！");
            }
        });
    }
    @Override
    public void deleteSuccessCallBack(int goodsId) {
        this.goodsId=goodsId;
        alertDialog=DialogUtils.showAlertDoubleBtnDialog(MenuManagementActivity.this,"删除商品？","",1, MenuManagementActivity.this);
        LogUtils.e("goodsId:"+goodsId);
    }
}
