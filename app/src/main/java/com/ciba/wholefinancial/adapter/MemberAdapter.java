package com.ciba.wholefinancial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.MemberBean;
import com.ciba.wholefinancial.bean.TableCodeBean;
import com.ciba.wholefinancial.util.LogUtils;
import com.google.gson.Gson;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class MemberAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<MemberBean> memberBeanList;
    private Context mContext;

    private Gson gson;
    public MemberAdapter(Context context,List<MemberBean> memberBeanList){
        mContext=context;
        this.memberBeanList=memberBeanList;

        gson = new Gson();
    }

    public void setDate(List<MemberBean> memberBeanList){
        this.memberBeanList=memberBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return memberBeanList.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.member_item, null);
            vh = new ViewHolder();
            vh.tv_name = (TextView) view.findViewById(R.id.tv_num);
            vh.tv_time = (TextView) view.findViewById(R.id.tv_time);
            vh.tv_balance= (TextView) view.findViewById(R.id.tv_balance);
            vh.tv_level= (TextView) view.findViewById(R.id.tv_level);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        MemberBean memberBean=memberBeanList.get(pos);
        vh.tv_balance.setText(" ￥"+memberBean.getBalance());
        vh.tv_time.setText(""+memberBean.getCreateTime());
        vh.tv_name.setText(memberBean.getPhoneNumber());
        vh.tv_level.setText(memberBean.getLevelName());
        LogUtils.e("设置数据后"+memberBeanList.size());
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return memberBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_name;
        TextView tv_time;
        TextView tv_balance;
        TextView tv_level;

    }

}
