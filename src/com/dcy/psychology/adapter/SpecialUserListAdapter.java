package com.dcy.psychology.adapter;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.SpecificUserBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpecialUserListAdapter extends BaseAdapter implements OnClickListener{
	private LayoutInflater mInflater;
	private ArrayList<SpecificUserBean> dataList;
	
	public SpecialUserListAdapter(Context mContext, ArrayList<SpecificUserBean> dataList){
		mInflater = LayoutInflater.from(mContext);
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
			convertView = mInflater.inflate(R.layout.item_show_specific_user_layout, null);
			mHolder = new Holder();
			mHolder.nameTv = (TextView) convertView.findViewById(R.id.tv_item_name);
			mHolder.achieveTv = (TextView) convertView.findViewById(R.id.tv_item_achieve);
			mHolder.speakTv = (TextView) convertView.findViewById(R.id.tv_item_speak);
			mHolder.attentionTv = (TextView) convertView.findViewById(R.id.tv_item_attention);
			mHolder.testTv = (TextView) convertView.findViewById(R.id.tv_item_test);
			mHolder.attentionTv.setOnClickListener(this);
			mHolder.testTv.setOnClickListener(this);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		SpecificUserBean item = dataList.get(position);
		mHolder.nameTv.setText(item.SpecificUserName);
		mHolder.achieveTv.setText(item.SpecificUserAchievement);
		mHolder.speakTv.setText(item.SpecificUserLifeMotto);
		return convertView;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_item_attention:
			
			break;
		case R.id.tv_item_test:
			break;
		default:
			break;
		}
	}
	
	private class Holder{
		TextView nameTv;
		TextView achieveTv;
		TextView speakTv;
		TextView attentionTv;
		TextView testTv;
	}
}
