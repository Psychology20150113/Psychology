package com.dcy.psychology.adapter;

import java.io.IOException;
import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.GrowPictureBean;
import com.dcy.psychology.gsonbean.GrowQuestionBean;
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

public class HomeListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<GrowPictureBean> dataList;
	private AssetManager manager;
	private int startPosition = 0;
	
	public HomeListAdapter(Context context , ArrayList<GrowPictureBean> dataList){
		mInflater = LayoutInflater.from(context);
		manager = context.getAssets();
		this.dataList = dataList;
	}
	
	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public Object getItem(int position) {
		if(startPosition + position > dataList.size() - 1 + Constants.HomePageTestTitle.length){
			return dataList.get(startPosition + position - dataList.size());
		}
		if(position == 0){
			return dataList.get(startPosition + position - startPosition / getCount());
		}else if(position == 2){
			return dataList.get(startPosition + position - startPosition / getCount() - 1);
		}else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void notifyDataSetChanged() {
		startPosition += getCount();
		if(startPosition >= (dataList.size() + Constants.HomePageTestTitle.length))
			startPosition = 0;
		super.notifyDataSetChanged();
	}
	
	public int getPageIndex(){
		return startPosition/getCount();
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
		if(position == 1){
			mHolder.mImageView.setImageResource(R.drawable.icon_pic_text_default);
			mHolder.mTextView.setText(Constants.HomePageTestTitle[startPosition/getCount()]);
		}else {
			GrowPictureBean item = (GrowPictureBean) getItem(position);
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
