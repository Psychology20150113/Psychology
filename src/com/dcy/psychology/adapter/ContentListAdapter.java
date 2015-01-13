package com.dcy.psychology.adapter;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.GrowPictureBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContentListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<GrowPictureBean> mGrowPicList;
	
	private String[] data;
	private boolean isStringArray = false;
	
	public ContentListAdapter(Context context, ArrayList<GrowPictureBean> growPicList) {
		mInflater = LayoutInflater.from(context);
		mGrowPicList = growPicList;
	}
	
	public ContentListAdapter(Context context, String[] data){
		mInflater = LayoutInflater.from(context);
		this.data = data;
		isStringArray = true;
	}
	
	@Override
	public int getCount() {
		if(isStringArray){
			return data.length;
		}else {
			return mGrowPicList.size();
		}
	}

	@Override
	public Object getItem(int arg0) {
		if(isStringArray){
			return data[arg0];
		}else {
			return mGrowPicList.get(arg0);
		}
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View viewHolder, ViewGroup parent) {
		TextView textView;
		if(viewHolder == null){
			viewHolder = mInflater.inflate(R.layout.item_text_list_layout, null);
			textView = (TextView) viewHolder.findViewById(R.id.content_tv);
			viewHolder.setTag(textView);
		}else {
			textView = (TextView)viewHolder.getTag();
		}
		if(isStringArray){
			textView.setText(data[position]);
		}else {
			GrowPictureBean item = mGrowPicList.get(position);
			textView.setText(item.getTitle());
		}
		return viewHolder;
	}

}
