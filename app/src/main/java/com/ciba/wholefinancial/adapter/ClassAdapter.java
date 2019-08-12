package com.ciba.wholefinancial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.ClassBean;
import com.ciba.wholefinancial.bean.GoodsBean;
import com.ciba.wholefinancial.callback.OnDeleteCallBack;
import com.google.gson.Gson;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class ClassAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<ClassBean> classBeanList;
    private Context mContext;
    private OnDeleteCallBack onDeleteCallBack;
    private Gson gson;
    public ClassAdapter(Context context,List<ClassBean> classBeanList,OnDeleteCallBack onDeleteCallBack){
        mContext=context;
        this.classBeanList=classBeanList;
        this.onDeleteCallBack=onDeleteCallBack;
        gson = new Gson();
    }

    public void setDate(List<ClassBean> classBeanList){
        this.classBeanList=classBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return classBeanList.get(arg0);
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
            vh.tv_delete = (TextView) view.findViewById(R.id.tv_delete);
            vh.tv_goodsName = (TextView) view.findViewById(R.id.tv_goodsName);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final ClassBean classBean=classBeanList.get(pos);
        vh.tv_id.setText(classBean.getClassId()+"");
        vh.tv_goodsName.setText(classBean.getClassName());
        vh.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteCallBack.deleteSuccessCallBack(classBean.getClassId());
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return classBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_id;
        TextView tv_goodsName;
        TextView tv_delete;
    }

}
