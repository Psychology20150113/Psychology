package com.dcy.psychology.adapter;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.model.IdAndName;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SimpleTextAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<IdAndName> dataList;
	
	public SimpleTextAdapter(Context context, ArrayList<IdAndName> dataList){
		mInflater = LayoutInflater.from(context);
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
		TextView mNameText;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_simple_text_layout, null);
			mNameText = (TextView) convertView.findViewById(R.id.tv_item_name);
			convertView.setTag(mNameText);
		} else {
			mNameText = (TextView) convertView.getTag();
		}
		mNameText.setText(dataList.get(position).name);
		return convertView;
	}

}
