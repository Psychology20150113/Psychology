package com.dcy.psychology.adapter;

import java.io.IOException;
import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.GrowPictureBean;
import com.dcy.psychology.util.Constants;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeShowAllListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<GrowPictureBean> dataList;
	private AssetManager manager;
	
	public HomeShowAllListAdapter(Context context , ArrayList<GrowPictureBean> dataList) {
		mInflater = LayoutInflater.from(context);
		manager = context.getAssets();
		this.dataList = dataList;
	}
	
	@Override
	public int getCount() {
		return dataList.size() + Constants.HomePageTestTitle.length;
	}

	@Override
	public Object getItem(int position) {
		if(position >= dataList.size()){
			return Constants.HomePageTestTitle[position - dataList.size()];
		}else {
			return dataList.get(position);
		}
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
			convertView = mInflater.inflate(R.layout.item_home_pic_layout, null);
			mHolder.mImageView = (ImageView) convertView.findViewById(R.id.item_thumb_iv);
			mHolder.mTextView = (TextView) convertView.findViewById(R.id.item_text_tv);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder) convertView.getTag();
		}
		if(position >= dataList.size()){
			mHolder.mImageView.setImageResource(R.drawable.icon_homepic_default);
			mHolder.mTextView.setText(Constants.HomePageTestTitle[position - dataList.size()]);
		}else {
			GrowPictureBean item = dataList.get(position);
			try {
				mHolder.mImageView.setImageBitmap(BitmapFactory.decodeStream(manager.open(item.getPicture())));
			} catch (IOException e) {
				e.printStackTrace();
			}
			mHolder.mTextView.setText(item.getTitle());
		}
		return convertView;
	}

	private class Holder{
		TextView mTextView;
		ImageView mImageView;
	}
}
