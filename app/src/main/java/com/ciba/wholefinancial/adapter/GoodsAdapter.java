package com.ciba.wholefinancial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.BusinessLoginBean;
import com.ciba.wholefinancial.bean.GoodsBean;
import com.google.gson.Gson;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class GoodsAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<GoodsBean> goodsBeanList;
    private Context mContext;

    private Gson gson;
    public GoodsAdapter(Context context, List<GoodsBean> goodsBeanList){
        mContext=context;
        this.goodsBeanList=goodsBeanList;

        gson = new Gson();
    }

    public void setDate(List<GoodsBean> goodsBeanList){
        this.goodsBeanList=goodsBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return goodsBeanList.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.goods_item, null);
            vh = new ViewHolder();
            vh.tv_id = (TextView) view.findViewById(R.id.tv_id);
            vh.tv_goodsName = (TextView) view.findViewById(R.id.tv_goodsName);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        GoodsBean goodsBean=goodsBeanList.get(pos);
        vh.tv_id.setText(goodsBean.getGoodsId());
        vh.tv_goodsName.setText(goodsBean.getGoodsName());
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return goodsBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_id;
        TextView tv_goodsName;
    }

}
