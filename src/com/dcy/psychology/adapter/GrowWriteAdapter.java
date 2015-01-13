package com.dcy.psychology.adapter;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.model.GrowWriteItem;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GrowWriteAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<GrowWriteItem> mDataList;
	
	private boolean canDelete = true;
	private boolean showCountString = false;
	
	public GrowWriteAdapter(Context context , ArrayList<GrowWriteItem> data) {
		mInflater = LayoutInflater.from(context);
		mDataList = data;
	}
	
	public void setCanDelete(boolean canDelete){
		this.canDelete = canDelete;
	}
	
	public void setShowCountString(boolean showCountString){
		this.showCountString = showCountString;
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
	public View getView(final int position, View viewHolder, ViewGroup parent) {
		Holder mHolder;
		if(viewHolder == null){
			viewHolder = mInflater.inflate(R.layout.item_grow_write_layout, null);
			mHolder = new Holder();
			mHolder.indexText = (TextView) viewHolder.findViewById(R.id.index_tv);
			mHolder.contentText = (TextView) viewHolder.findViewById(R.id.content_tv);
			mHolder.degreeText = (TextView) viewHolder.findViewById(R.id.degree_tv);
			mHolder.deleteView = (ImageView) viewHolder.findViewById(R.id.delete_item_iv);
			viewHolder.setTag(mHolder);
		}else {
			mHolder = (Holder) viewHolder.getTag();
		}
		GrowWriteItem item = mDataList.get(position);
		if(TextUtils.isEmpty(item.getIndexString())){
			mHolder.indexText.setText(showCountString ? String.format("µÚ%d´Î  ", position + 1) : 
				String.valueOf(position + 1));
		}else {
			mHolder.indexText.setText(item.getIndexString());
		}
		if(!TextUtils.isEmpty(item.getContent())){
			mHolder.contentText.setText(item.getContent());	
		}else {
			mHolder.contentText.setVisibility(View.GONE);
		}
		if(!TextUtils.isEmpty(item.getDegree())){
			mHolder.degreeText.setText(item.getDegree());
		}else {
			mHolder.degreeText.setVisibility(View.GONE);
		}
		if(canDelete){
			mHolder.deleteView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					mDataList.remove(position);
					notifyDataSetChanged();
				}
			});
		}else {
			mHolder.deleteView.setVisibility(View.GONE);
		}
		return viewHolder;
	}
	
	private class Holder{
		TextView indexText,contentText,degreeText;
		ImageView deleteView;
	}

}
