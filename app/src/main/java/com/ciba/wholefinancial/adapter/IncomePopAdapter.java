package com.ciba.wholefinancial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciba.wholefinancial.R;

import java.util.List;

public class IncomePopAdapter extends BaseAdapter {
    private List<String> content;
    private Context context;
    private LayoutInflater mInflater;
    private  String title;

    public IncomePopAdapter(Context context, List<String> content ,String title){
        this.context = context;
        this.content = content;
        this.title=title;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return content == null ? 0 : content.size();
    }

    @Override
    public Object getItem(int position) {
        return content.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.popuwindow_income_item,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_spinner = (TextView) convertView.findViewById(R.id.tv_spinner);
//            viewHolder.check = (RelativeLayout) convertView.findViewById(R.id.check);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String spinnerText = content.get(position);
        viewHolder.tv_spinner.setText(spinnerText);
        if(spinnerText.equals(title)){
            viewHolder.tv_spinner.setTextColor(context.getResources().getColor(R.color.errorColor));
        }else{
            viewHolder.tv_spinner.setTextColor(context.getResources().getColor(R.color.text_color_black));
        }
        return convertView;
    }
    public static class ViewHolder{
        TextView tv_spinner;
//        public RelativeLayout check;
    }
}