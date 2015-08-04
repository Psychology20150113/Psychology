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
import com.dcy.psychology.MessageInfoActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.model.MessageModel;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.Constants;

public class MessageAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<MessageModel> dataList;
	private AsyncImageCache mAsyncImageCache;
	
	public MessageAdapter(Context mContext, ArrayList<MessageModel> dataList){
		this(mContext, dataList, true);
	}
	
	public MessageAdapter(Context mContext, ArrayList<MessageModel> dataList, boolean canOpration){
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
			convertView = mInflater.inflate(R.layout.item_message_layout, null);
			mHolder = new Holder();
			mHolder.infoLayout = convertView.findViewById(R.id.ll_item_message);
			mHolder.infoLayout.setOnClickListener(lookInfoListener);
			mHolder.headerIv = (ImageView) convertView.findViewById(R.id.iv_header);
			mHolder.nameTv=(TextView) convertView.findViewById(R.id.tv_item_name);
			mHolder.achieveTv = (TextView) convertView.findViewById(R.id.tv_item_achieve);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		MessageModel item = dataList.get(position);
		mAsyncImageCache.displayImage(mHolder.headerIv, R.drawable.ic_launcher, 
				new AsyncImageCache.NetworkImageGenerator(item.SpecificUserHeadUrl, item.SpecificUserHeadUrl));
		mHolder.achieveTv.setText(item.SpecificUserAchievement);
		mHolder.nameTv.setText(item.SpecificUserName);
		mHolder.infoLayout.setTag(item.SpecificUserPhone);
		return convertView;
	}
	//触摸跳转到查看心晴师详情
	private OnClickListener lookInfoListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String phoneNum = (String)v.getTag();
			Intent mIntent = new Intent(mContext, MessageInfoActivity.class);
			mIntent.putExtra(Constants.PhoneNum, phoneNum);
			mContext.startActivity(mIntent);
		}
	};
	
	
	private class Holder{
		ImageView headerIv;
		TextView nameTv;
		TextView achieveTv;
		View infoLayout;
	}
}
