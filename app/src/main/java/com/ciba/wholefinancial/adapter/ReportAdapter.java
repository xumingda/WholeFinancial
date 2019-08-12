package com.ciba.wholefinancial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.MerchantBean;
import com.ciba.wholefinancial.bean.ReportBean;
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
public class ReportAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<ReportBean> reportBeanList;
    private Context mContext;

    private Gson gson;
    public ReportAdapter(Context context, List<ReportBean> reportBeanList){
        mContext=context;
        this.reportBeanList=reportBeanList;

        gson = new Gson();
    }

    public void setDate(List<ReportBean> reportBeanList){
        this.reportBeanList=reportBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return reportBeanList.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.report_item, null);
            vh = new ViewHolder();
            vh.tv_create_time = (TextView) view.findViewById(R.id.tv_create_time);
            vh.tv_content = (TextView) view.findViewById(R.id.tv_content);

            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        ReportBean reportBean=reportBeanList.get(pos);
        vh.tv_create_time.setText(reportBean.getCreateTime());
        vh.tv_content.setText(reportBean.getContent());


        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return reportBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_create_time;
        TextView tv_content;

    }

}
