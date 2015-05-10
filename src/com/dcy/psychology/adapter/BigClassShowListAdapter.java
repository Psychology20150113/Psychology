package com.dcy.psychology.adapter;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.ClassBean;
import com.dcy.psychology.util.AsyncImageCache;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BigClassShowListAdapter extends BaseAdapter {
	private String[] classArray;
	private LayoutInflater mInflater;
	private ArrayList<ClassBean> mDateList;
	private AsyncImageCache mCache;
	
	public BigClassShowListAdapter(Context context, ArrayList<ClassBean> dateList){
		classArray = context.getResources().getStringArray(R.array.class_type_array);
		mInflater = LayoutInflater.from(context);
		this.mDateList = dateList;
		mCache = AsyncImageCache.from(context);
	}
	
	@Override
	public int getCount() {
		return classArray.length;
	}

	@Override
	public Object getItem(int position) {
		return mDateList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder mHolder;
		if(convertView == null){
			mHolder = new Holder();
			convertView = mInflater.inflate(R.layout.item_category_open_class, null);
			mHolder.categoryTitle = (TextView) convertView.findViewById(R.id.tv_item_title);
			mHolder.moreTv = (TextView) convertView.findViewById(R.id.tv_item_more);
			mHolder.moreTv.setOnClickListener(moreClickListener);
			mHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_thumb);
			mHolder.titleView = (TextView) convertView.findViewById(R.id.item_tv_title);
			mHolder.subTitleView = (TextView) convertView.findViewById(R.id.item_tv_subtitle);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		mHolder.categoryTitle.setText(classArray[position]);
		mHolder.moreTv.setTag(position);
		ClassBean itemBean = mDateList.get(position);
		mCache.displayImage(mHolder.imageView, R.drawable.ic_launcher, 
				new AsyncImageCache.NetworkImageGenerator(itemBean.getClassSmallImgUrl(), itemBean.getClassSmallImgUrl()));
		mHolder.titleView.setText(itemBean.getClassTitleName());
		mHolder.subTitleView.setText(itemBean.getClassSummary());
		return convertView;
	}
	
	private OnClickListener moreClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			
		}
	};

	private class Holder {
		TextView categoryTitle;
		TextView moreTv;
		ImageView imageView;
		TextView titleView;
		TextView subTitleView;
	}
}
