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

import com.ciba.wholefinancial.R;
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
public class ListsDiscountAddAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<String> list;
    private Context mContext;
    private OnUpdateCallBack onUpdateCallBack;
    List<TypeBean> typeBeanList=new ArrayList<>();
    private Gson gson;
    String countDiscount;
    String consumeFullCount;
    String validDay;
    public ListsDiscountAddAdapter(Context context, List<String> list, OnUpdateCallBack onUpdateCallBack, List<TypeBean> typeBeanList){
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
            view = LayoutInflater.from(mContext).inflate(R.layout.list_discount_add_item, null);
            vh = new ViewHolder();
            vh.et_count = (EditText) view.findViewById(R.id.et_count);
            vh.et_discount= (EditText) view.findViewById(R.id.et_discount);
            vh.et_time= (EditText) view.findViewById(R.id.et_time);
            vh.et_count.setTag(pos);
            vh.et_discount.setTag(pos);
            vh.et_time.setTag(pos);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.et_count.addTextChangedListener(new TextSwitcher(vh));
        vh.et_discount.addTextChangedListener(new TextSwitcherTwo(vh));
        vh.et_time.addTextChangedListener(new TextSwitcherThree(vh));
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
            int position = (int) mHolder.et_count.getTag();//取tag值
            TypeBean typeBean=new TypeBean();
            consumeFullCount=s.toString();
            typeBean.setConsumeFullCount(consumeFullCount);
            typeBean.setCountDiscount(countDiscount);
            typeBean.setValidDay(validDay);
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
            int position = (int) mHolder.et_discount.getTag();//取tag值
            TypeBean typeBean=new TypeBean();
            countDiscount=s.toString();
            typeBean.setConsumeFullCount(consumeFullCount);
            typeBean.setCountDiscount(countDiscount);
            typeBean.setValidDay(validDay);
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
    class TextSwitcherThree implements TextWatcher {
        private ViewHolder mHolder;
        private CharSequence temp = null;
        public TextSwitcherThree(ViewHolder mHolder) {
            this.mHolder = mHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp=s;
            int position = (int) mHolder.et_time.getTag();//取tag值
            TypeBean typeBean=new TypeBean();
            validDay=s.toString();
            typeBean.setConsumeFullCount(consumeFullCount);
            typeBean.setCountDiscount(countDiscount);
            typeBean.setValidDay(validDay);
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
        EditText et_count;
        EditText et_discount;
        EditText et_time;
    }

}
