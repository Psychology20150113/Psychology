package com.dcy.psychology.adapter;

import com.dcy.psychology.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class SlideAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private String[] mSlideArray;
	private int[] mIconArray = {
			R.drawable.icon_slide_two , R.drawable.icon_slide_three, R.drawable.icon_slide_four , 
			R.drawable.icon_slide_five , R.drawable.icon_slide_problem, R.drawable.icon_slide_one ,
			R.drawable.icon_slide_six, R.drawable.icon_slide_seven
	};
	
	public SlideAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		mSlideArray = context.getResources().getStringArray(R.array.slide_item_array);
	}
	
	@Override
	public int getCount() {
		return 4;
	}

	@Override
	public Object getItem(int position) {
		return mSlideArray[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder mHolder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_slide_layout, null);
			mHolder = new Holder();
			mHolder.iconView = (ImageView) convertView.findViewById(R.id.left_icon_iv);
			mHolder.contentText = (TextView) convertView.findViewById(R.id.slide_menu_tv);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder) convertView.getTag();
		}
		mHolder.contentText.setText(mSlideArray[position]);
		mHolder.iconView.setImageResource(mIconArray[position]);
		return convertView;
	}

	private class Holder{
		ImageView iconView;
		TextView contentText;
	}
}
