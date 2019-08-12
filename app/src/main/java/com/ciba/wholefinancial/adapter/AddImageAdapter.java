package com.ciba.wholefinancial.adapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;


import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.activity.LoginActivity;
import com.ciba.wholefinancial.util.DialogUtils;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.UIUtils;

import java.util.List;


public class AddImageAdapter extends BaseAdapter {

    private String TAG = "ClientListAdapter";
    private List<Bitmap> bitmapList;
    private Context mContext;
    private PopupWindow popupWindow;
    private View view;
    private List<String> pics;
    public AddImageAdapter(Context context, List<Bitmap> bitmapList, PopupWindow popupWindow, View view, List<String> pics) {
        mContext = context;
        this.popupWindow = popupWindow;
        this.view = view;
        this.bitmapList = bitmapList;
        this.pics = pics;
    }

    public void setDate(List<Bitmap> bitmapList, List<String> pics) {
        this.bitmapList = bitmapList;
        this.pics = pics;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return bitmapList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int pos, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.add_image_item, null);
            vh = new ViewHolder();
            vh.iv_image = (ImageView) view.findViewById(R.id.iv_image);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        if (pos < bitmapList.size() - 1) {
            vh.iv_image.setImageBitmap(bitmapList.get(pos));
            vh.iv_image.setScaleType(ImageView.ScaleType.FIT_XY);
        } else{
            vh.iv_image.setImageDrawable(UIUtils.getResources().getDrawable(R.mipmap.img_add));
        }

        ImageViewListener imageViewListener = new ImageViewListener(pos);
        vh.iv_image.setOnClickListener(imageViewListener);
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return bitmapList.size();
    }


    class ViewHolder {
        ImageView iv_image;
    }

    /**
     * 点击事件
     */
    @SuppressLint("NewApi")
    private class ImageViewListener implements View.OnClickListener {

        /**
         * 选择的某项
         */
        public int position = 0;

        public ImageViewListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
                LogUtils.e("有");
                if (bitmapList.size() == 1) {

                    popupWindow.setAnimationStyle(R.style.AnimBottom);
                    popupWindow.showAtLocation(view,
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    if (position == bitmapList.size() - 1) {
                        if(bitmapList.size()<4){
                            popupWindow.setAnimationStyle(R.style.AnimBottom);
                            popupWindow.showAtLocation(view,
                                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        }else{
                            DialogUtils.showAlertDialog(mContext,
                                    "只能添加3张图片！");
                        }

                    } else {
                        if (pics.size() > 0) {
                            pics.remove(0);
                        }
                        if (position < bitmapList.size() - 1) {
                            bitmapList.remove(position);
                        }
                        notifyDataSetChanged();
                    }
                }


        }
    }

}
