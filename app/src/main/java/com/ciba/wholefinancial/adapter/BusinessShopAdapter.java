package com.ciba.wholefinancial.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.activity.ReceiptCodeActivity;
import com.ciba.wholefinancial.activity.UpdateShopActivity;
import com.ciba.wholefinancial.bean.BusinessLoginBean;
import com.ciba.wholefinancial.bean.BusinessShopBean;
import com.ciba.wholefinancial.bean.OrderBean;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.UIUtils;
import com.google.gson.Gson;

import java.util.List;


/**
 * 类描述：
 * 创建人：许明达
 * 创建时间：2016/04/14 10:49
 * 修改备注：
 */
public class BusinessShopAdapter extends BaseAdapter{

    private String TAG="ShowAdapter";
    private List<BusinessShopBean> businessShopBeanList;
    private Context mContext;

    private Gson gson;
    public BusinessShopAdapter(Context context, List<BusinessShopBean> businessShopBeanList){
        mContext=context;
        this.businessShopBeanList=businessShopBeanList;

        gson = new Gson();
    }

    public void setDate(List<BusinessShopBean> businessShopBeanList){
        this.businessShopBeanList=businessShopBeanList;
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return businessShopBeanList.get(arg0);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.shop_item, null);
            vh = new ViewHolder();
            vh.tv_name = (TextView) view.findViewById(R.id.tv_name);
            vh.tv_code = (TextView) view.findViewById(R.id.tv_code);
            vh.btn_copy= (Button) view.findViewById(R.id.btn_copy);
            vh.rl= (RelativeLayout) view.findViewById(R.id.rl);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final BusinessShopBean businessShopBean=businessShopBeanList.get(pos);
        vh.tv_name.setText(businessShopBean.getShopName());
        vh.tv_code.setText(""+businessShopBean.getCodeCount());
        vh.btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, ReceiptCodeActivity.class);
                intent.putExtra("businessShopId",businessShopBean.getId());
                UIUtils.startActivityForResultNextAnim(intent,1);
            }
        });
        vh.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, UpdateShopActivity.class);
                intent.putExtra("BusinessShopBean",businessShopBean);
                UIUtils.startActivityForResultNextAnim(intent,1);
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return businessShopBeanList.size() ;
    }


    class ViewHolder {
        TextView tv_name;
        TextView tv_code;
        RelativeLayout rl;
        Button btn_copy;
    }
    public static void CopyToClipboard(Context context,String text){
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //clip.getText(); // 粘贴
        clip.setText(text); // 复制
        Toast.makeText(context,"复制成功！",Toast.LENGTH_SHORT).show();
    }
}
