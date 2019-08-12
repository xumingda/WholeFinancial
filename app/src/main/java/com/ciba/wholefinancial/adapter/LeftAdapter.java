package com.ciba.wholefinancial.adapter;

import java.util.List;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.ClassBean;
import com.ciba.wholefinancial.bean.LeftMenu;
import com.ciba.wholefinancial.util.UIUtils;

public class LeftAdapter extends BaseAdapter{

	private List<ClassBean> classBeanList;
	private Context contex;
	private LayoutInflater inflater;
	private int currentItem = 0;//当前item

	
	public LeftAdapter(Context context,List<ClassBean> classBeanList,int pos) {
		// TODO Auto-generated constructor stub
		this.contex = context;
		this.classBeanList = classBeanList;
		this.currentItem = pos;
		this.inflater = LayoutInflater.from(context);
	}
	
	public LeftAdapter(Context context,List<ClassBean> classBeanList) {
		// TODO Auto-generated constructor stub
		this.contex = context;
		this.classBeanList = classBeanList;
		this.inflater = LayoutInflater.from(context);
	}
	public void setDate(List<ClassBean> classBeanList){
		this.classBeanList = classBeanList;
	}

	public void setSelect(int pos){
		this.currentItem = pos;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return classBeanList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.left_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		ClassBean menu = classBeanList.get(position);
		holder.textView.setText(menu.getClassName());
		
		if(currentItem == position){//如果当前条目等于position
			holder.ll.setBackgroundColor(UIUtils.getColor(R.color.tab_select));
			holder.v_fire.setVisibility(View.VISIBLE);
			holder.textView.setTextColor(UIUtils.getColor(R.color.text_black));
		}else{
			holder.ll.setBackground(UIUtils.getDrawable(R.drawable.shape_white));//设置选择状态的背景颜色
			holder.v_fire.setVisibility(View.GONE);
			holder.textView.setTextColor(UIUtils.getColor(R.color.grayColor));
		}



		
		return convertView;
	}
	
	class ViewHolder{

		TextView textView;
		View v_fire;
		LinearLayout ll;
		public ViewHolder(View convertView) {
			// TODO Auto-generated constructor stub
			ll=(LinearLayout) convertView.findViewById(R.id.ll);
			v_fire= (View) convertView.findViewById(R.id.v_fire);
			textView = (TextView) convertView.findViewById(R.id.left_lv);
		}
		
	}

}
