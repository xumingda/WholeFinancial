package com.ciba.wholefinancial.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.response.GetReceivablesStatisticsDescResponse;
import com.ciba.wholefinancial.response.GetReceivablesStatisticsResponse;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class StatisticsMerchantAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<GetReceivablesStatisticsResponse.ReceivablesStatisticsBean> receivablesStatisticsDescBeanList;
    private Context mContext;

    private Gson gson;
    public StatisticsMerchantAdapter(Context context, List<GetReceivablesStatisticsResponse.ReceivablesStatisticsBean> receivablesStatisticsDescBeanList){
        mContext=context;
        this.receivablesStatisticsDescBeanList=receivablesStatisticsDescBeanList;

        gson = new Gson();
    }

    public void setDate(List<GetReceivablesStatisticsResponse.ReceivablesStatisticsBean> receivablesStatisticsDescBeanList){
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
            view = LayoutInflater.from(mContext).inflate(R.layout.statistics_merchant_item, null);
            vh = new ViewHolder();
            vh.tv_money = (TextView) view.findViewById(R.id.tv_money);
            vh.tv_payType = (TextView) view.findViewById(R.id.tv_payType);
            vh.iv_paytype= (ImageView) view.findViewById(R.id.tv_paytype);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        GetReceivablesStatisticsResponse.ReceivablesStatisticsBean receivablesStatisticsDescBean=receivablesStatisticsDescBeanList.get(pos);
        vh.tv_money.setText("$"+receivablesStatisticsDescBean.getMoney());
        //0现金收款，1余额收款，2微信收款，3支付宝收款，4线上收款
        switch (receivablesStatisticsDescBean.getPayType()){
            case 0: {
                vh.tv_payType.setText("现金收款");
                vh.iv_paytype.setImageDrawable(UIUtils.getDrawable(R.mipmap.img_zu));
                break;
            }
            case 1: {
                vh.tv_payType.setText("余额收款");
                vh.iv_paytype.setImageDrawable(UIUtils.getDrawable(R.mipmap.img_balance));
                break;
            }
            case 2: {
                vh.tv_payType.setText("微信收款");
                vh.iv_paytype.setImageDrawable(UIUtils.getDrawable(R.mipmap.img_weixn));
                break;
            }
            case 3: {
                vh.tv_payType.setText("支付宝收款");
                vh.iv_paytype.setImageDrawable(UIUtils.getDrawable(R.mipmap.img_zhifubao));
                break;
            }
            case 4: {
                vh.tv_payType.setText("线上收款");
                vh.iv_paytype.setImageDrawable(UIUtils.getDrawable(R.mipmap.img_online));
                break;
            }
        }

        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return receivablesStatisticsDescBeanList.size() ;
    }

    class ViewHolder {
        TextView tv_money;
        ImageView iv_paytype;
        TextView tv_payType;

    }

}
