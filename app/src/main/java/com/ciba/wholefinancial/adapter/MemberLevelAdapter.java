package com.ciba.wholefinancial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.MemberLevelBean;
import com.google.gson.Gson;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class MemberLevelAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<MemberLevelBean> memberLevelBeanList;
    private Context mContext;

    private Gson gson;
    public MemberLevelAdapter(Context context, List<MemberLevelBean> memberLevelBeanList){
        mContext=context;
        this.memberLevelBeanList=memberLevelBeanList;

        gson = new Gson();
    }

    public void setDate( List<MemberLevelBean> memberLevelBeanList){
        this.memberLevelBeanList=memberLevelBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return memberLevelBeanList.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.member_level_item, null);
            vh = new ViewHolder();
            vh.tv_condition = (TextView) view.findViewById(R.id.tv_condition);
            vh.tv_discount = (TextView) view.findViewById(R.id.tv_discount);
            vh.tv_level_name = (TextView) view.findViewById(R.id.tv_levelName);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        MemberLevelBean memberLevelBean=memberLevelBeanList.get(pos);
        vh.tv_level_name.setText(memberLevelBean.getLevelName());
        vh.tv_discount.setText(memberLevelBean.getRate()+"折");
        vh.tv_condition.setText("消费次数"+memberLevelBean.getCount()+"次或消费 金额¥"+memberLevelBean.getMoney());
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return memberLevelBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_level_name;
        TextView tv_condition;
        TextView tv_discount;
    }

}
