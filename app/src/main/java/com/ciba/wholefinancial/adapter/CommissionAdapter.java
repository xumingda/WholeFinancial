package com.ciba.wholefinancial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.CommissionBean;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.util.PictureOption;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class CommissionAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<CommissionBean> commissionBeanList;
    private Context mContext;
    private ImageLoader imageLoader;

    private Gson gson;
    public CommissionAdapter(Context context, List<CommissionBean> commissionBeanList){
        mContext=context;
        this.commissionBeanList=commissionBeanList;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(mContext)));

//        mVideoThumbLoader = new MyVideoThumbLoader();// 初始化缩略图载入方法
        gson = new Gson();
    }

    public void setDate(List<CommissionBean> commissionBeanList){
        this.commissionBeanList=commissionBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return commissionBeanList.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.commission_item, null);
            vh = new ViewHolder();
            vh.tv_date = (TextView) view.findViewById(R.id.tv_date);
            vh.tv_shop_name = (TextView) view.findViewById(R.id.tv_shop_name);
            vh.tv_price = (TextView) view.findViewById(R.id.tv_price);
            vh.tv_rate = (TextView) view.findViewById(R.id.tv_rate);
            vh.tv_add = (TextView) view.findViewById(R.id.tv_add);
            vh.tv_table_number= (TextView) view.findViewById(R.id.tv_table_number);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        CommissionBean commissionBean=commissionBeanList.get(pos);
        vh.tv_shop_name.setText(commissionBean.getBusinessName());
        vh.tv_table_number.setText("桌牌号："+commissionBean.getCode()+"号");
        vh.tv_date.setText(commissionBean.getCreateTime());
        vh.tv_price.setText("¥"+commissionBean.getOrderMoney());
        vh.tv_rate.setText(commissionBean.getCommissionRate()+"%");
        vh.tv_add.setText("+"+commissionBean.getCommission());
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return commissionBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_add;
        TextView tv_rate;
        TextView tv_price;
        TextView tv_date;
        TextView tv_shop_name;
        TextView tv_table_number;
    }

}
