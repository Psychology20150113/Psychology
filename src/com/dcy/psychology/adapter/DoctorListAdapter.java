package com.dcy.psychology.adapter;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.DoctorListBean;
import com.dcy.psychology.util.AsyncImageCache;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DoctorListAdapter extends BaseAdapter {
	private ArrayList<DoctorListBean> mDataList;
	private LayoutInflater mInflater;
	private AsyncImageCache imageLoader;
	
	public DoctorListAdapter(Context mContext , ArrayList<DoctorListBean> dataList , AsyncImageCache cache) {
		mDataList = dataList;
		mInflater = LayoutInflater.from(mContext);
		imageLoader = cache;
	}
	
	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mDataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View viewHolder, ViewGroup group) {
		Holder mHolder = null;
		if(viewHolder == null){
			viewHolder = mInflater.inflate(R.layout.item_doctor_list_layout, null);
			mHolder = new Holder();
			mHolder.nameText = (TextView) viewHolder.findViewById(R.id.item_doctor_list_tv);
			mHolder.docView = (ImageView) viewHolder.findViewById(R.id.item_doctor_list_iv);
			viewHolder.setTag(mHolder);
		}else {
			mHolder = (Holder) viewHolder.getTag();
		}
		DoctorListBean item = mDataList.get(position);
		mHolder.nameText.setText(item.getDoctorName());
		imageLoader.displayImage(mHolder.docView, R.drawable.chat_edit, 
				new AsyncImageCache.NetworkImageGenerator(item.getDoctorHeadUrl(), item.getDoctorHeadUrl()));
		return viewHolder;
	}
	
	private class Holder{
		public TextView nameText;
		public ImageView docView;
	}

}
