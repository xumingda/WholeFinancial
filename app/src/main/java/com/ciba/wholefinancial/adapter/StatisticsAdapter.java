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
import com.ciba.wholefinancial.bean.OrderBean;
import com.ciba.wholefinancial.response.GetReceivablesStatisticsDescResponse;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class StatisticsAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<GetReceivablesStatisticsDescResponse.ReceivablesStatisticsDescBean> receivablesStatisticsDescBeanList;
    private Context mContext;

    private Gson gson;
    public StatisticsAdapter(Context context, List<GetReceivablesStatisticsDescResponse.ReceivablesStatisticsDescBean> receivablesStatisticsDescBeanList){
        mContext=context;
        this.receivablesStatisticsDescBeanList=receivablesStatisticsDescBeanList;

        gson = new Gson();
    }

    public void setDate(List<GetReceivablesStatisticsDescResponse.ReceivablesStatisticsDescBean> receivablesStatisticsDescBeanList){
        this.receivablesStatisticsDescBeanList=receivablesStatisticsDescBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return receivablesStatisticsDescBeanList.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.statistics_item, null);
            vh = new ViewHolder();
            vh.tv_commission = (TextView) view.findViewById(R.id.tv_commission);
            vh.tv_orderMoney= (TextView) view.findViewById(R.id.tv_orderMoney);
            vh.tv_rate = (TextView) view.findViewById(R.id.tv_rate);
            vh.tv_time = (TextView) view.findViewById(R.id.tv_time);
            vh.tv_shop_name = (TextView) view.findViewById(R.id.tv_shop_name);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        GetReceivablesStatisticsDescResponse.ReceivablesStatisticsDescBean receivablesStatisticsDescBean=receivablesStatisticsDescBeanList.get(pos);
        vh.tv_shop_name.setText(receivablesStatisticsDescBean.getBusinessName());
        vh.tv_time.setText(receivablesStatisticsDescBean.getCreateTime());
        vh.tv_commission.setText(receivablesStatisticsDescBean.getCommission());
        vh.tv_rate.setText(receivablesStatisticsDescBean.getCommissionRate());
        vh.tv_orderMoney.setText(receivablesStatisticsDescBean.getOrderMoney());
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return receivablesStatisticsDescBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_time;
        TextView tv_commission;
        TextView tv_orderMoney;
        TextView tv_rate;
        TextView tv_shop_name;
    }

}
