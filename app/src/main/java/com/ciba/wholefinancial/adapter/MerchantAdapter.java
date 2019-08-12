package com.ciba.wholefinancial.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.util.PictureOption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class MerchantAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<MerchantBean> merchantBeanList;
    private Context mContext;
    private ImageLoader imageLoader;
    //0代表普通，1代表待审核
    private int type;
//    private MyVideoThumbLoader mVideoThumbLoader;
    private Gson gson;
    public MerchantAdapter(Context context, List<MerchantBean> merchantBeanList,int type){
        mContext=context;
        this.merchantBeanList=merchantBeanList;
        this.type=type;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init((ImageLoaderConfiguration.createDefault(mContext)));

//        mVideoThumbLoader = new MyVideoThumbLoader();// 初始化缩略图载入方法
        gson = new Gson();
    }

    public void setDate(List<MerchantBean> merchantBeanList){
        this.merchantBeanList=merchantBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return merchantBeanList.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.merchant_item, null);
            vh = new ViewHolder();
            vh.tv_address = (TextView) view.findViewById(R.id.tv_address);
            vh.tv_shop_name = (TextView) view.findViewById(R.id.tv_shop_name);
            vh.tv_tellphone = (TextView) view.findViewById(R.id.tv_tellphone);
            vh.tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
            vh.iv_shop = (ImageView) view.findViewById(R.id.iv_shop);
            vh.tv_state= (TextView) view.findViewById(R.id.tv_state);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        MerchantBean merchantBean=merchantBeanList.get(pos);
        vh.tv_shop_name.setText(merchantBean.getStoreName());
        vh.tv_tellphone.setText(merchantBean.getAccountNumber());
        vh.tv_user_name.setText(merchantBean.getContact());
        vh.tv_address.setText(merchantBean.getStoreStreet());
        imageLoader.displayImage(merchantBean.getStoreEntrancePic(), vh.iv_shop, PictureOption.getSimpleOptions());
        if(type==1){
            vh.tv_state.setVisibility(View.VISIBLE);
        }else{
            vh.tv_state.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return merchantBeanList.size() ;
    }


    class ViewHolder {
        ImageView iv_shop;
        TextView tv_user_name;
        TextView tv_tellphone;
        TextView tv_address;
        TextView tv_shop_name;
        TextView tv_state;
    }

}
