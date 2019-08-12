package com.ciba.wholefinancial.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.BusinessShopBean;
import com.ciba.wholefinancial.bean.TableCodeBean;
import com.ciba.wholefinancial.util.LogUtils;
import com.google.gson.Gson;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class TableCodeAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<TableCodeBean> tableCodeBeanList;
    private Context mContext;

    private Gson gson;
    public TableCodeAdapter(Context context, List<TableCodeBean> tableCodeBeanList){
        mContext=context;
        this.tableCodeBeanList=tableCodeBeanList;

        gson = new Gson();
    }

    public void setDate(List<TableCodeBean> tableCodeBeanList){
        this.tableCodeBeanList=tableCodeBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return tableCodeBeanList.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.table_code_item, null);
            vh = new ViewHolder();
            vh.tv_name = (TextView) view.findViewById(R.id.tv_name);
            vh.tv_code = (TextView) view.findViewById(R.id.tv_code);

            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        TableCodeBean tableCodeBean=tableCodeBeanList.get(pos);
        vh.tv_name.setText(tableCodeBean.getShopName());
        vh.tv_code.setText(""+tableCodeBean.getCode());

        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return tableCodeBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_name;
        TextView tv_code;


    }

}
