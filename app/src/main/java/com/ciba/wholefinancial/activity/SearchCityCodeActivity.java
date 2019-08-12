package com.ciba.wholefinancial.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.adapter.CityCodeAdapter;
import com.ciba.wholefinancial.base.BaseActivity;
import com.ciba.wholefinancial.base.MyVolley;
import com.ciba.wholefinancial.bean.CityCodeBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.SalesmanBean;
import com.ciba.wholefinancial.protocol.GetCodeProtocol;
import com.ciba.wholefinancial.protocol.LoginProtocol;
import com.ciba.wholefinancial.request.GetCodeRequest;
import com.ciba.wholefinancial.request.LoginRequest;
import com.ciba.wholefinancial.response.GetCodeResponse;
import com.ciba.wholefinancial.response.LoginResponse;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.ListDataSave;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.SPUtils;
import com.ciba.wholefinancial.util.SharedPrefrenceUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class SearchCityCodeActivity extends BaseActivity {

    private LayoutInflater mInflater;
    private View rootView;
    private ListView listView;
    private CityCodeAdapter cityCodeAdapter;
    private ListDataSave listDataSave;
    private EditText mEditText;
    private ImageView mImageView;
    private TextView mTextView;
    private  List<CityCodeBean> cityCodeBeanList;
    private List<CityCodeBean> templist;
    private View view_back;
    @Override
    protected View initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = View.inflate(this, R.layout.activity_search_city_code, null);
        if (mInflater == null) {
            mInflater = (LayoutInflater) UIUtils.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        setContentView(rootView);
        initDate();
        return rootView;
    }

    private void initDate() {
        listDataSave=new ListDataSave(this,"whole");
        listView=rootView.findViewById(R.id.list_code);

        cityCodeBeanList=listDataSave.getDataList("cityCodeBeanList");
        if(cityCodeAdapter==null&&cityCodeBeanList!=null&&cityCodeBeanList.size()>0){
            cityCodeAdapter=new CityCodeAdapter(SearchCityCodeActivity.this,cityCodeBeanList);
            listView.setAdapter(cityCodeAdapter);
        }
        view_back=(View)findViewById(R.id.view_back);
        mTextView = (TextView) findViewById(R.id.textview);
        mEditText = (EditText) findViewById(R.id.edittext);
        mImageView = (ImageView) findViewById(R.id.imageview);

        //设置删除图片的点击事件
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把EditText内容设置为空
                mEditText.setText("");
                //把ListView隐藏
                cityCodeAdapter.setDate(cityCodeBeanList);
                cityCodeAdapter.notifyDataSetChanged();
            }
        });
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.animprv_in, R.anim.animprv_out);
            }
        });
        //EditText添加监听
        mEditText.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}//文本改变之前执行

            @Override
            //文本改变的时候执行
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //如果长度为0
                if (s.length() == 0) {
                    //隐藏“删除”图片
                    mImageView.setVisibility(View.GONE);
                } else {//长度不为0
                    //显示“删除图片”
                    mImageView.setVisibility(View.VISIBLE);
                    //显示ListView
                    showListView();
                }
            }

            public void afterTextChanged(Editable s) { }//文本改变之后执行
        });

        mTextView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //如果输入框内容为空，提示请输入搜索内容
                if(TextUtils.isEmpty(mEditText.getText().toString().trim())){
//                    ToastUtils.showToast(context,"请输入您要搜索的内容");
                }else {
                    //判断cursor是否为空
//                    if (cursor != null) {
//                        int columnCount = cursor.getCount();
//                        if (columnCount == 0) {
//                            ToastUtils.showToast(context, "对不起，没有你要搜索的内容");
//                        }
//                    }
                }

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(templist!=null&&templist.size()>0){
                    //创建Intent对象
                    Intent intent=new Intent();
                    //将求和的结果放进intent中
                    intent.putExtra("cityCode", templist.get(i).getCityCode());
                    intent.putExtra("cityName", templist.get(i).getCityName());
                    //返回结果
                    setResult(100,intent);
                    //关闭当前界面
                    finish();
                }else{
                    //创建Intent对象
                    Intent intent=new Intent();
                    //将求和的结果放进intent中
                    intent.putExtra("cityCode", cityCodeBeanList.get(i).getCityCode());
                    intent.putExtra("cityName", cityCodeBeanList.get(i).getCityName());
                    //返回结果
                    setResult(100,intent);
                    //关闭当前界面
                    finish();
                }


            }
        });

    }

    private void showListView() {
        listView.setVisibility(View.VISIBLE);
        templist=new ArrayList<>();
        //获得输入的内容
        String str = mEditText.getText().toString().trim();
        //获取数据库对象
        for (int i=0;i<cityCodeBeanList.size();i++){
            if(cityCodeBeanList.get(i).getCityName().indexOf(str)!=-1){
                templist.add(cityCodeBeanList.get(i));
            }
        }
        cityCodeAdapter=new CityCodeAdapter(SearchCityCodeActivity.this,templist);
        listView.setAdapter(cityCodeAdapter);
    }

}
