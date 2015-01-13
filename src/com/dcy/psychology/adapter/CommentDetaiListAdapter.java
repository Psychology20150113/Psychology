package com.dcy.psychology.adapter;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.CommentDetailBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentDetaiListAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private ArrayList<CommentDetailBean> dataList;
	
	public CommentDetaiListAdapter(Context mContext) {
		mInflater = LayoutInflater.from(mContext);
	}
	
	public CommentDetaiListAdapter(Context mContext , ArrayList<CommentDetailBean> dataList) {
		this.dataList = dataList;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setData(ArrayList<CommentDetailBean> dataList){
		this.dataList = dataList;
	}
	
	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder mHolder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_comment_detail_layout, null);
			mHolder = new Holder();
			mHolder.nameText = (TextView) convertView.findViewById(R.id.item_comment_name);
			mHolder.contentText = (TextView) convertView.findViewById(R.id.item_comment_content);
			mHolder.timeText = (TextView) convertView.findViewById(R.id.item_comment_time);
			convertView.setTag(mHolder);
		}else {
			mHolder = (Holder) convertView.getTag();
		}
		CommentDetailBean item = dataList.get(position);
		mHolder.nameText.setText(item.getReviewUserLoginName());
		mHolder.contentText.setText(item.getReviewContent());
		mHolder.timeText.setText(item.getReviewDate());
		return convertView;
	}
	
	private class Holder{
		TextView nameText;
		TextView contentText;
		TextView timeText;
	}
	
	
}
