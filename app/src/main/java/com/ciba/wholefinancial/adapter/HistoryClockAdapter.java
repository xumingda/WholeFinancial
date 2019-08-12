package com.ciba.wholefinancial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.ClockBean;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class HistoryClockAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<ClockBean> clockBeanList;
    private Context mContext;

    private Gson gson;
    public HistoryClockAdapter(Context context, List<ClockBean> clockBeanList){
        mContext=context;
        this.clockBeanList=clockBeanList;

        gson = new Gson();
    }

    public void setDate(List<ClockBean> clockBeanList){
        this.clockBeanList=clockBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return clockBeanList.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.history_clock_item, null);
            vh = new ViewHolder();
            vh.tv_time = (TextView) view.findViewById(R.id.tv_time);
            vh.tv_attend = (TextView) view.findViewById(R.id.tv_attend);
            vh.view=(View)view.findViewById(R.id.view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        ClockBean clockBean=clockBeanList.get(pos);
        vh.tv_time.setText(clockBean.getCreateTime());
        //0 等待考勤，1 正常考勤，2 超时考勤，3 修正考勤，4 缺卡，5 考勤不通过
        if(clockBean.getStatus()==1){
            vh.view.setVisibility(View.VISIBLE);
            vh.tv_attend.setText("已打卡");
            vh.tv_attend.setTextColor(UIUtils.getColor(R.color.blue_attend));
        }else if(clockBean.getStatus()==4){
            vh.view.setVisibility(View.INVISIBLE);
            vh.tv_attend.setText("缺卡");
            vh.tv_attend.setTextColor(UIUtils.getColor(R.color.errorColor));
        }else if(clockBean.getStatus()==2){
            vh.view.setVisibility(View.VISIBLE);
            vh.tv_attend.setText("超时考勤");
            vh.tv_attend.setTextColor(UIUtils.getColor(R.color.blue_attend));
        }else if(clockBean.getStatus()==3){
            vh.view.setVisibility(View.VISIBLE);
            vh.tv_attend.setText("修正考勤");
            vh.tv_attend.setTextColor(UIUtils.getColor(R.color.blue_attend));
        }else if(clockBean.getStatus()==5){
            vh.view.setVisibility(View.VISIBLE);
            vh.tv_attend.setText("考勤不通过");
            vh.tv_attend.setTextColor(UIUtils.getColor(R.color.blue_attend));
        }
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return clockBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_attend;
        TextView tv_time;
        View view;
    }

}
