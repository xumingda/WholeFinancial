package com.ciba.wholefinancial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.BusinessLoginBean;
import com.google.gson.Gson;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class BusinessLoginAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<BusinessLoginBean> businessUserBeanList;
    private Context mContext;

    private Gson gson;
    public BusinessLoginAdapter(Context context, List<BusinessLoginBean> businessUserBeanList){
        mContext=context;
        this.businessUserBeanList=businessUserBeanList;

        gson = new Gson();
    }

    public void setDate(List<BusinessLoginBean> businessUserBeanList){
        this.businessUserBeanList=businessUserBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return businessUserBeanList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.business_login_item, null);
            vh = new ViewHolder();
            vh.tv_time = (TextView) view.findViewById(R.id.tv_time);
            vh.tv_tellphone = (TextView) view.findViewById(R.id.tv_tellphone);
            vh.tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        BusinessLoginBean businessLoginBean=businessUserBeanList.get(pos);
        vh.tv_time.setText(businessLoginBean.getCreateTime());
        vh.tv_tellphone.setText(businessLoginBean.getAccount());
        vh.tv_name.setText(businessLoginBean.getName());
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return businessUserBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_time;
        TextView tv_tellphone;
        TextView tv_name;
    }

}
