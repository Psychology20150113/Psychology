package com.dcy.psychology.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.model.ChatItemModel;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<ChatItemModel> mDataList;
	private SimpleDateFormat mFormat;
	private int padding;
	
	public ChatAdapter(Context mContext ,ArrayList<ChatItemModel> dataList){
		mInflater = LayoutInflater.from(mContext);
		mDataList = dataList;
		mFormat = new SimpleDateFormat("yyyy--MM--dd");
		padding = (int)(50 * mContext.getResources().getDisplayMetrics().density);
	}
	
	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder mHolder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_chat_layout, null);
			mHolder = new Holder();
			mHolder.mContentView = (TextView)convertView.findViewById(R.id.chat_content_tv);
			mHolder.mLeftView = (ImageView) convertView.findViewById(R.id.item_chat_left_iv);
			mHolder.mRightView = (ImageView) convertView.findViewById(R.id.item_chat_right_iv);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder) convertView.getTag();
		}
		ChatItemModel item = mDataList.get(position);
		mHolder.mContentView.setText(item.getContext());
		if(!item.isMine()){
			mHolder.mContentView.setGravity(Gravity.LEFT);
			mHolder.mContentView.setPadding(0, 0, padding, 0);
			mHolder.mLeftView.setVisibility(View.VISIBLE);
			mHolder.mRightView.setVisibility(View.GONE);
		}else {
			mHolder.mContentView.setGravity(Gravity.RIGHT);
			mHolder.mContentView.setPadding(padding, 0, 0, 0);
			mHolder.mLeftView.setVisibility(View.GONE);
			mHolder.mRightView.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	
	private class Holder{
		TextView mContentView;
		ImageView mLeftView,mRightView;
	}
}
