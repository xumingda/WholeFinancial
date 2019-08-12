package com.ciba.wholefinancial.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.CityCodeBean;
import com.ciba.wholefinancial.bean.TypeBean;
import com.ciba.wholefinancial.callback.OnUpdateCallBack;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class ListsAddAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<String> list;
    private Context mContext;
    private OnUpdateCallBack onUpdateCallBack;
    List<TypeBean> typeBeanList=new ArrayList<>();
    private Gson gson;
    String consumeFullMoney;
    String subMoney;

    public ListsAddAdapter(Context context, List<String> list, OnUpdateCallBack onUpdateCallBack,  List<TypeBean> typeBeanList){
        mContext=context;
        this.list=list;
        this.typeBeanList=typeBeanList;
        this.onUpdateCallBack=onUpdateCallBack;
        gson = new Gson();
    }

    public void setDate(List<String> list){
        this.list=list;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int pos, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_add_item, null);
            vh = new ViewHolder();
            vh.et_money = (EditText) view.findViewById(R.id.et_money);
            vh.et_subtract= (EditText) view.findViewById(R.id.et_subtract);
            vh.et_money.setTag(pos);
            vh.et_subtract.setTag(pos);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.et_money.addTextChangedListener(new TextSwitcher(vh));
        vh.et_subtract.addTextChangedListener(new TextSwitcherTwo(vh));
        return view;
    }
    class TextSwitcher implements TextWatcher {
        private ViewHolder mHolder;
        private CharSequence temp = null;
        public TextSwitcher(ViewHolder mHolder) {
            this.mHolder = mHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp=s;
            int position = (int) mHolder.et_money.getTag();//取tag值
            TypeBean typeBean=new TypeBean();
            consumeFullMoney=s.toString();
            typeBean.setConsumeFullMoney(consumeFullMoney);
            typeBean.setSubMoney(subMoney);
            typeBeanList.set(position,typeBean);
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(temp)) {
                  onUpdateCallBack.update(0);
                } else {
                    onUpdateCallBack.update(1);
                }
        }
    }
    class TextSwitcherTwo implements TextWatcher {
        private ViewHolder mHolder;
        private CharSequence temp = null;
        public TextSwitcherTwo(ViewHolder mHolder) {
            this.mHolder = mHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp=s;
            int position = (int) mHolder.et_subtract.getTag();//取tag值
            TypeBean typeBean=new TypeBean();
            subMoney=s.toString();
            typeBean.setConsumeFullMoney(consumeFullMoney);
            typeBean.setSubMoney(subMoney);
            typeBeanList.set(position,typeBean);
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(temp)) {
                onUpdateCallBack.update(0);
            } else {
                onUpdateCallBack.update(1);
            }
        }
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size() ;
    }


    class ViewHolder {
        EditText et_money;
        EditText et_subtract;

    }

}
