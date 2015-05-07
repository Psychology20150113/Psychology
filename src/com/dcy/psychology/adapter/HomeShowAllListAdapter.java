package com.dcy.psychology.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcy.psychology.R;
import com.dcy.psychology.util.Constants;

public class HomeShowAllListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	
	private int[] picResArray = {R.drawable.icon_home_pic_text_01, R.drawable.icon_home_pic_text_02,
			R.drawable.icon_home_pic_text_03, R.drawable.icon_home_pic_text_04,
			R.drawable.icon_home_pic_text_05, R.drawable.icon_home_pic_text_06,
			R.drawable.icon_home_pic_text_07, R.drawable.icon_home_pic_text_08};
	
	public HomeShowAllListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return Constants.HomePageTestTitle.length;
	}

	@Override
	public Object getItem(int position) {
		return Constants.HomePageTestTitle[position];
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
			mHolder.mLabelView = (TextView) convertView.findViewById(R.id.item_label_tv);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder) convertView.getTag();
		}
		mHolder.mImageView.setImageResource(picResArray[position]);
		mHolder.mTextView.setText(Constants.HomePageTestTitle[position]);
		mHolder.mLabelView.setText(R.string.test);
		return convertView;
	}

	private class Holder{
		TextView mTextView;
		TextView mLabelView;
		ImageView mImageView;
	}
}
