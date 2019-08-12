package com.ciba.wholefinancial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.BusinessUserBean;
import com.google.gson.Gson;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class BusinessUserAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<BusinessUserBean> businessUserBeanList;
    private Context mContext;

    private Gson gson;
    public BusinessUserAdapter(Context context, List<BusinessUserBean> businessUserBeanList){
        mContext=context;
        this.businessUserBeanList=businessUserBeanList;

        gson = new Gson();
    }

    public void setDate(List<BusinessUserBean> businessUserBeanList){
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
            vh.tv_create_time = (TextView) view.findViewById(R.id.tv_time);
            vh.tv_content = (TextView) view.findViewById(R.id.tv_content);
            vh.tv_title = (TextView) view.findViewById(R.id.tv_title);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
//        MessageBean messageBean=messageBeanList.get(pos);
//        vh.tv_create_time.setText(messageBean.getCreateTime());
//        vh.tv_content.setText(messageBean.getContent());

        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return businessUserBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_create_time;
        TextView tv_content;
        TextView tv_title;
    }

}
