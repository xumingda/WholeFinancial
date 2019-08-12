package com.ciba.wholefinancial.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.MessageBean;
import com.ciba.wholefinancial.bean.OrderBean;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class OrderAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<OrderBean> orderBeanList;
    private Context mContext;

    private Gson gson;
    public OrderAdapter(Context context,List<OrderBean> orderBeanList){
        mContext=context;
        this.orderBeanList=orderBeanList;

        gson = new Gson();
    }

    public void setDate(List<OrderBean> orderBeanList){
        this.orderBeanList=orderBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return orderBeanList.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.order_item, null);
            vh = new ViewHolder();
            vh.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            vh.tv_state = (TextView) view.findViewById(R.id.tv_state);
            vh.tv_price = (TextView) view.findViewById(R.id.tv_price);
            vh.tv_time = (TextView) view.findViewById(R.id.tv_time);
            vh.tv_code= (TextView) view.findViewById(R.id.tv_code);
            vh.tv_name = (TextView) view.findViewById(R.id.tv_user_name);
            vh.tv_address= (TextView) view.findViewById(R.id.tv_address);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        OrderBean orderBean=orderBeanList.get(pos);
        if(orderBean.getPhoneNumber()!=null&&orderBean.getPhoneNumber().length()>10){
            vh.tv_phone.setText(orderBean.getPhoneNumber().substring(0,3)+"****"+orderBean.getPhoneNumber().substring(7,11));
        }else{
            vh.tv_phone.setText(orderBean.getPhoneNumber());
        }

        vh.tv_price.setText("¥"+orderBean.getNeedMoney());
        vh.tv_time.setText(orderBean.getCreateTime());
        //0未支付，1已支付，2已取消
        if(orderBean.getStatus()==0){
            vh.tv_state.setBackground(UIUtils.getDrawable(R.drawable.shape_order_btn));
            vh.tv_state.setText("未支付");
            vh.tv_state.setTextColor(UIUtils.getColor(R.color.tab_background));
        }else if(orderBean.getStatus()==1){
            vh.tv_state.setBackground(UIUtils.getDrawable(R.drawable.shape_order_green));
            vh.tv_state.setText("已支付");
            vh.tv_state.setTextColor(UIUtils.getColor(R.color.tab_background));
        }else{
            vh.tv_state.setBackground(UIUtils.getDrawable(R.drawable.shape_order_gray));
            vh.tv_state.setText("已取消");
            vh.tv_state.setTextColor(UIUtils.getColor(R.color.text_color_gray));
        }
        if(orderBean.getEatType()==1){
            vh.tv_code.setText("外卖");
            vh.tv_code.setTextColor(UIUtils.getColor(R.color.errorColor));
            vh.tv_name.setVisibility(View.VISIBLE);
            vh.tv_address.setVisibility(View.VISIBLE);
            vh.tv_name.setText(orderBean.getContact());
            vh.tv_address.setText(orderBean.getContactAddress());
        }else if(orderBean.getEatType()==2){
            vh.tv_code.setText("扫码支付");
            vh.tv_code.setTextColor(UIUtils.getColor(R.color.errorColor));
            vh.tv_name.setVisibility(View.GONE);
            vh.tv_address.setVisibility(View.GONE);
        }
        else{
            vh.tv_name.setVisibility(View.GONE);
            vh.tv_address.setVisibility(View.GONE);
            vh.tv_code.setText(orderBean.getCode());
            vh.tv_code.setTextColor(UIUtils.getColor(R.color.text_black));
        }

        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return orderBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_time;
        TextView tv_state;
        TextView tv_phone;
        TextView tv_price;
        TextView tv_code;
        TextView tv_name;
        TextView tv_address;
    }

}
