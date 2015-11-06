package com.dcy.psychology.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcy.psychology.DoctorPersonalInfo;
import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.ApplyInfoBean;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.Constants;

public class TalkingAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<ApplyInfoBean> dataList;
	private AsyncImageCache mAsyncImageCache;
	
	public TalkingAdapter(Context mContext, ArrayList<ApplyInfoBean> dataList){
		mInflater = LayoutInflater.from(mContext);
		this.dataList = dataList;
		this.mContext = mContext;
		mAsyncImageCache = AsyncImageCache.from(mContext);
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
			convertView = mInflater.inflate(R.layout.item_show_talkabout, null);
			mHolder = new Holder();
			mHolder.headerIv = (ImageView) convertView.findViewById(R.id.iv_header);
			mHolder.nameTv = (TextView) convertView.findViewById(R.id.tv_item_name);
			mHolder.achieve = (TextView)convertView.findViewById(R.id.tv_item_achieve); 
			mHolder.contentTv = (TextView)convertView.findViewById(R.id.tv_item_content);
			mHolder.goChatIv = (ImageView) convertView.findViewById(R.id.iv_goto_chat);
			mHolder.timeInfoTv = (TextView)convertView.findViewById(R.id.tv_ptime);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		ApplyInfoBean item = dataList.get(position);
		mAsyncImageCache.displayImage(mHolder.headerIv, R.drawable.ic_launcher, 
				new AsyncImageCache.NetworkImageGenerator(item.userheadurl, item.userheadurl));
		mHolder.achieve.setText(item.UserAchievement);
		mHolder.nameTv.setText(item.username);
		mHolder.contentTv.setText(item.question);
		return convertView;
	}
	
	private class Holder{
		TextView achieve;
		ImageView headerIv;
		TextView nameTv;
		TextView timeInfoTv;
		ImageView goChatIv;
		TextView contentTv;
	}
}
