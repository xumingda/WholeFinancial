package com.ciba.wholefinancial.adapter;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.GoodsBean;
import com.ciba.wholefinancial.bean.RightMenu;
import com.ciba.wholefinancial.callback.OnDeleteCallBack;
import com.ciba.wholefinancial.util.PictureOption;
import com.ciba.wholefinancial.weiget.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class RightItemAdapter extends BaseAdapter {

	private List<GoodsBean> dataList;
	private Context contex;
	private LayoutInflater Inflater;
	private ImageLoader imageLoader;
	private OnDeleteCallBack onDeleteCallBack;
	public RightItemAdapter(Context context, List<RightMenu> data, int pos,OnDeleteCallBack onDeleteCallBack) {
		// TODO Auto-generated constructor stub
		this.contex = context;
		this.dataList = data.get(pos).getRightMenuItem();
		this.Inflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init((ImageLoaderConfiguration.createDefault(context)));
		this.onDeleteCallBack=onDeleteCallBack;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			convertView = Inflater.inflate(R.layout.goods_add_item,null);
			holder = new ViewHolder(convertView);

			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		final GoodsBean goodsBean=dataList.get(position);

		holder.tv_goodsPrice.setText(goodsBean.getGoodsPrice());
		holder.tv_goodsName.setText(goodsBean.getGoodsName());
		imageLoader.displayImage(goodsBean.getGoodsPic(), holder.iv_goods, PictureOption.getSimpleOptions());
		holder.tv_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onDeleteCallBack.deleteSuccessCallBack(goodsBean.getGoodsId());
			}
		});
		return convertView;
	}

	class ViewHolder{
		TextView tv_delete;
		TextView tv_goodsPrice;
		TextView tv_goodsName;
		RoundImageView iv_goods;
		public ViewHolder(View convertView) {
			tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
			tv_goodsPrice = (TextView) convertView.findViewById(R.id.tv_goodsPrice);
			tv_goodsName = (TextView) convertView.findViewById(R.id.tv_goodsName);
			iv_goods= (RoundImageView) convertView.findViewById(R.id.iv_goods);
			// TODO Auto-generated constructor stub
		}
		
	}
}
