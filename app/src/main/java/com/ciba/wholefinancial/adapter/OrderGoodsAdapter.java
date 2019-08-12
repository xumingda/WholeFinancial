package com.ciba.wholefinancial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.GoodsBean;
import com.ciba.wholefinancial.bean.OrderGoodsBean;
import com.ciba.wholefinancial.util.PictureOption;
import com.ciba.wholefinancial.weiget.RoundImageView;
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
public class OrderGoodsAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<OrderGoodsBean> goodsBeanList;
    private Context mContext;
    private ImageLoader imageLoader;
    private Gson gson;
    public OrderGoodsAdapter(Context context, List<OrderGoodsBean> goodsBeanList){
        mContext=context;
        this.goodsBeanList=goodsBeanList;

        gson = new Gson();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(context)));
    }

    public void setDate(List<OrderGoodsBean> goodsBeanList){
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
            view = LayoutInflater.from(mContext).inflate(R.layout.order_goods_item, null);
            vh = new ViewHolder();
            vh.tv_goodsPrice = (TextView) view.findViewById(R.id.tv_goodsPrice);
            vh.tv_goodsName = (TextView) view.findViewById(R.id.tv_goodsName);
            vh.iv_goods= (RoundImageView) view.findViewById(R.id.iv_goods);
            vh.tv_goodsUnit= (TextView) view.findViewById(R.id.tv_goodsUnit);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        OrderGoodsBean goodsBean=goodsBeanList.get(pos);
        vh.tv_goodsPrice.setText("¥"+goodsBean.getGoodsPrice()+"X"+goodsBean.getCount());
        vh.tv_goodsName.setText(goodsBean.getGoodsName());
        vh.tv_goodsUnit.setText("/"+goodsBean.getGoodsUnit());
        imageLoader.displayImage(goodsBean.getGoodsPic(), vh.iv_goods, PictureOption.getSimpleOptions());
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return goodsBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_goodsPrice;
        TextView tv_goodsName;
        TextView tv_goodsUnit;
        RoundImageView iv_goods;
    }

}
