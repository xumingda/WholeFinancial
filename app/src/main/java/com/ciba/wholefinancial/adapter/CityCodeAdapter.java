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
import com.ciba.wholefinancial.bean.CityCodeBean;
import com.ciba.wholefinancial.bean.OrderBean;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class CityCodeAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<CityCodeBean> cityCodeBeanList;
    private Context mContext;

    private Gson gson;
    public CityCodeAdapter(Context context, List<CityCodeBean> cityCodeBeanList){
        mContext=context;
        this.cityCodeBeanList=cityCodeBeanList;

        gson = new Gson();
    }

    public void setDate(List<CityCodeBean> cityCodeBeanList){
        this.cityCodeBeanList=cityCodeBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return cityCodeBeanList.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.city_code_item, null);
            vh = new ViewHolder();
            vh.tv_city = (TextView) view.findViewById(R.id.tv_city);
            vh.tv_code= (TextView) view.findViewById(R.id.tv_code);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        CityCodeBean cityCodeBean=cityCodeBeanList.get(pos);
        vh.tv_city.setText(cityCodeBean.getCityName());
        vh.tv_code.setText(cityCodeBean.getCityCode().substring(0,6));



        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return cityCodeBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_code;
        TextView tv_city;

    }

}
