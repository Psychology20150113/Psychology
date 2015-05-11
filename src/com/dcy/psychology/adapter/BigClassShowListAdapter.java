package com.dcy.psychology.adapter;

import java.util.ArrayList;

import com.dcy.psychology.OnlinePicListActivity;
import com.dcy.psychology.PlamPictureDetailActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.ClassBean;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.Constants;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BigClassShowListAdapter extends BaseAdapter {
	private String[] classArray;
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<ClassBean> mDateList;
	private AsyncImageCache mCache;
	
	public BigClassShowListAdapter(Context context, ArrayList<ClassBean> dateList){
		classArray = context.getResources().getStringArray(R.array.class_type_array);
		mInflater = LayoutInflater.from(context);
		this.mDateList = dateList;
		mCache = AsyncImageCache.from(context);
		this.mContext = context;
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
			mHolder.openClassLayout = (LinearLayout) convertView.findViewById(R.id.ll_item_open_class);
			mHolder.openClassLayout.setOnClickListener(moreClickListener);
			mHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_thumb);
			mHolder.titleView = (TextView) convertView.findViewById(R.id.item_tv_title);
			mHolder.subTitleView = (TextView) convertView.findViewById(R.id.item_tv_subtitle);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		mHolder.categoryTitle.setText(classArray[position]);
		mHolder.moreTv.setTag(position);
		mHolder.openClassLayout.setTag(position);
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
			Intent mIntent = null;
			switch (v.getId()) {
			case R.id.tv_item_more:
				mIntent = new Intent(mContext, OnlinePicListActivity.class);
				mIntent.putExtra(Constants.ClassCategoryId, 1);
				break;
			case R.id.ll_item_open_class:
				int classId = mDateList.get(position).getClassID();
				mIntent = new Intent(mContext, PlamPictureDetailActivity.class);
				mIntent.putExtra(Constants.OnlineClassId, classId);
				break;
			default:
				break;
			}
			if(mIntent != null){
				mContext.startActivity(mIntent);
			}
		}
	};

	private class Holder {
		TextView categoryTitle;
		TextView moreTv;
		LinearLayout openClassLayout;
		ImageView imageView;
		TextView titleView;
		TextView subTitleView;
	}
}
