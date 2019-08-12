package com.ciba.wholefinancial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.MarketingBean;
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
public class MarketingAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<MarketingBean> marketingBeanList;
    private Context mContext;

//    private MyVideoThumbLoader mVideoThumbLoader;
    private Gson gson;
    public MarketingAdapter(Context context, List<MarketingBean> marketingBeanList){
        mContext=context;
        this.marketingBeanList=marketingBeanList;


//        mVideoThumbLoader = new MyVideoThumbLoader();// 初始化缩略图载入方法
        gson = new Gson();
    }

    public void setDate(List<MarketingBean> marketingBeanList){
        this.marketingBeanList=marketingBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return marketingBeanList.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.marketing_item, null);
            vh = new ViewHolder();
            vh.tv_title = (TextView) view.findViewById(R.id.tv_title);
            vh.tv_content = (TextView) view.findViewById(R.id.tv_content);
            vh.tv_time = (TextView) view.findViewById(R.id.tv_time);

            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        MarketingBean merchantBean=marketingBeanList.get(pos);
        if(merchantBean.getType()==1){
            vh.tv_title.setText("消费满"+merchantBean.getConsumeFullMoney()+"元减"+merchantBean.getSubMoney()+"元");
        }else if(merchantBean.getType()==3) {
            vh.tv_title.setText("消费满" + merchantBean.getConsumeFullCount() + "次折扣" + merchantBean.getCountDiscount() + "折");
        }else{
            vh.tv_title.setText("等级折扣" + merchantBean.getLevelDiscount() + "折" );
        }

        vh.tv_content.setText("活动时间："+merchantBean.getStartTime()+"至"+merchantBean.getEndTime());
        vh.tv_time.setText(merchantBean.getCreateTime());

        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return marketingBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_title;
        TextView tv_content;
        TextView tv_time;
    }

}
