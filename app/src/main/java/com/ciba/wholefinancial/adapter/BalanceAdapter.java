package com.ciba.wholefinancial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.BalanceBean;
import com.ciba.wholefinancial.bean.MemberBean;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class BalanceAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<BalanceBean> balanceBeanList;
    private Context mContext;

    private Gson gson;
    public BalanceAdapter(Context context, List<BalanceBean> balanceBeanList){
        mContext=context;
        this.balanceBeanList=balanceBeanList;

        gson = new Gson();
    }

    public void setDate(List<BalanceBean> balanceBeanList){
        this.balanceBeanList=balanceBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return balanceBeanList.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.balance_item, null);
            vh = new ViewHolder();
            vh.tv_type = (TextView) view.findViewById(R.id.tv_type);
            vh.tv_time = (TextView) view.findViewById(R.id.tv_time);
            vh.tv_money = (TextView) view.findViewById(R.id.tv_money);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        BalanceBean balanceBean=balanceBeanList.get(pos);
        if(balanceBean.getType()==0){
            vh.tv_type.setText("系统充值");
        }else{
            vh.tv_type.setText("系统消费");
        }
        if(balanceBean.getMoney().indexOf("-")!=-1){
            vh.tv_money.setText(balanceBean.getMoney());
            vh.tv_money.setTextColor(UIUtils.getColor(R.color.text_green));
        }else{
            vh.tv_money.setText(balanceBean.getMoney());
            vh.tv_money.setTextColor(UIUtils.getColor(R.color.errorColor));
        }
        vh.tv_time.setText(""+balanceBean.getCreateTime());
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return balanceBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_type;
        TextView tv_time;
        TextView tv_money;
    }

}
