package com.ciba.wholefinancial.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.adapter.MemberAdapter;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.base.ViewTabBasePager;
import com.ciba.wholefinancial.bean.MemberBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.protocol.GetMemberProtocol;
import com.ciba.wholefinancial.response.GetMemberResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @作者: 许明达
 * @创建时间: 2016-4-27下午5:24:30
 * @更新人:
 * @更新时间:
 * @更新内容: TODO
 */
public class MemberSearchActivity extends BaseActivity implements
        PullToRefreshBase.OnRefreshListener {


    private Dialog loadingDialog;
    private MerchantBean merchantBean;
    private PullToRefreshListView lv_member;
    private View v_empty;
    private TextView tv_empty;
    private GetMemberResponse getMemberResponse;
    private List<MemberBean> memberBeanList;
    private int pageNo = 1;
    //判断是否刷新
    private boolean isRefresh = false;
    private MemberAdapter memberAdapter;
    private LayoutInflater mInflater;
    private View rootView;
    private EditText et_search;
    private String phoneNumber;
    private boolean isOpen;

    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_member_search, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initData();
        return rootView;
    }


    public void initData() {
        memberBeanList = new ArrayList<>();
        merchantBean = SPUtils.getBeanFromSp(this, "businessObject", "MerchantBean");
        loadingDialog = DialogUtils.createLoadDialog(this, false);
        et_search = (EditText) rootView.findViewById(R.id.et_search);
        v_empty = (View) rootView.findViewById(R.id.v_empty);
        tv_empty = (TextView) rootView.findViewById(R.id.tv_empty);
        lv_member = (PullToRefreshListView) rootView.findViewById(R.id.lv_member);
        lv_member.setMode(PullToRefreshBase.Mode.BOTH);
        lv_member.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        lv_member.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中");
        lv_member.getLoadingLayoutProxy(false, true).setReleaseLabel("放开以刷新");
        lv_member.setOnRefreshListener(this);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    phoneNumber=v.getText().toString();
                    if(phoneNumber.length()==11){
                        if(!isOpen){
                            pageNo=1;
                            isOpen=true;
                            getMemberList();
                        }

                    }else{
                        DialogUtils.showAlertDialog(MemberSearchActivity.this, "请输入正常的手机号码！");
                    }
                    return true;

                }

                return false;

            }

        });

    }

    private void setData() {
        memberBeanList.addAll(getMemberResponse.getDataList());
        if (memberAdapter == null) {
            memberAdapter = new MemberAdapter(this, memberBeanList);
            lv_member.setAdapter(memberAdapter);
            LogUtils.e("设置数据" + memberBeanList.size());
        } else {
            memberAdapter.setDate(memberBeanList);
            memberAdapter.notifyDataSetChanged();
        }
    }

    //获取数据
    public void getMemberList() {
        loadingDialog.show();
        GetMemberProtocol getMemberProtocol = new GetMemberProtocol();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("phoneNumber", phoneNumber);
        params.put("businessId", String.valueOf(merchantBean.getId()));
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", "100");
        String url = getMemberProtocol.getApiFun();

        MyVolley.uploadNoFile(MyVolley.POST, url, params, new MyVolley.VolleyCallback() {
            @Override
            public void dealWithJson(String address, String json) {
                isOpen=false;
                Gson gson = new Gson();
                getMemberResponse = gson.fromJson(json, GetMemberResponse.class);
                LogUtils.e("getMemberResponse:" + getMemberResponse.toString());
                loadingDialog.dismiss();
                if (getMemberResponse.code == 0) {
                    if (getMemberResponse.getDataList().size() > 0) {

                        if (getMemberResponse.getDataList().size() > 0) {
                            if (pageNo == 1) {
                                memberBeanList.clear();
                            }
                            setData();
                        } else {
                            DialogUtils.showAlertDialog(MemberSearchActivity.this, "没有更多数据了！");
                        }
                    } else {
                        v_empty.setVisibility(View.VISIBLE);
                        tv_empty.setVisibility(View.VISIBLE);
                        lv_member.setVisibility(View.GONE);
                    }

                } else {

                    DialogUtils.showAlertDialog(MemberSearchActivity.this,
                            getMemberResponse.msg);
                }

                if (isRefresh) {
                    isRefresh = false;
                    lv_member.onRefreshComplete();
                }
            }

            @Override
            public void dealWithError(String address, String error) {
                isOpen=false;
                loadingDialog.dismiss();
                DialogUtils.showAlertDialog(MemberSearchActivity.this, error);
                if (isRefresh) {
                    isRefresh = false;
                    lv_member.onRefreshComplete();
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
            if (lv_member.isHeaderShown()) {
                pageNo = 1;
                memberBeanList.clear();
                getMemberList();
            } else if (lv_member.isFooterShown()) {
                pageNo++;
                getMemberList();
            }
        }
    }
}