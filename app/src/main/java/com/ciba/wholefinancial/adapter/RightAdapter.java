package com.ciba.wholefinancial.adapter;

import java.util.List;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.activity.AddGoodsActivity;
import com.ciba.wholefinancial.activity.MenuManagementActivity;
import com.ciba.wholefinancial.bean.RightMenu;
import com.ciba.wholefinancial.callback.OnDeleteCallBack;
import com.ciba.wholefinancial.util.UIUtils;
import com.ciba.wholefinancial.weiget.CostomListView;


public class RightAdapter extends BaseAdapter{

	private List<RightMenu> dataList;
	private Context contex;
	private LayoutInflater inflater;
	private RightItemAdapter itemAdapter;
	private OnDeleteCallBack onDeleteCallBack;
	
	public RightAdapter(Context context, List<RightMenu> data, OnDeleteCallBack onDeleteCallBack) {
		// TODO Auto-generated constructor stub
		this.dataList =data;
		this.contex = context;
		this.inflater = LayoutInflater.from(context);
		this.onDeleteCallBack=onDeleteCallBack;
	}

	public void setDate(List<RightMenu> data){
		this.dataList =data;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.right_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}		
		itemAdapter = new RightItemAdapter(contex,dataList,position,onDeleteCallBack);
		holder.cListView.setAdapter(itemAdapter);
		holder.cListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Intent intent=new Intent(contex, AddGoodsActivity.class);
				intent.putExtra("GoodsBean",dataList.get(position).getRightMenuItem().get(i));
				UIUtils.startActivityForResultNextAnim(intent,1);
			}
		});
		RightMenu menu = dataList.get(position);
		holder.textView.setText(menu.getRightTitle());
		
		return convertView;
	}

	class ViewHolder{

		CostomListView cListView;
		TextView textView;
		public ViewHolder(View convertView) {
			// TODO Auto-generated constructor stub
			textView = (TextView) convertView.findViewById(R.id.right_lv);
			cListView = (CostomListView) convertView.findViewById(R.id.right_cos_list);
		}
		
	}
}
