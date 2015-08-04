package com.dcy.psychology.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.dcy.psychology.DoctorPersonalInfo;
import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.SpecificUserBean;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.Constants;

public class TalkingAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	private ArrayList<SpecificUserBean> dataList;
	private AsyncImageCache mAsyncImageCache;
	
	public TalkingAdapter(Context mContext, ArrayList<SpecificUserBean> dataList){
		this(mContext, dataList, true);
	}
	
	public TalkingAdapter(Context mContext, ArrayList<SpecificUserBean> dataList, boolean canOpration){
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
			mHolder.headerIv1 = (ImageView) convertView.findViewById(R.id.iv_st_header);
			mHolder.nameTv1 = (TextView) convertView.findViewById(R.id.tv_item_stname);
			mHolder.tv = (TextView)convertView.findViewById(R.id.tv_item_concern); 
			TextPaint tp = mHolder.tv.getPaint(); 
			tp.setFakeBoldText(true); 
			mHolder.time = (TextView)convertView.findViewById(R.id.tv_item_time);
			mHolder.surplus = (TextView)convertView.findViewById(R.id.tv_item_surplus);
			mHolder.headerIv2 = (ImageView) convertView.findViewById(R.id.iv_te_header);
			mHolder.nameTv2 = (TextView) convertView.findViewById(R.id.tv_item_tename);
			mHolder.infoLayout = convertView.findViewById(R.id.ll_item_talkabout);
			mHolder.infoLayout.setOnClickListener(lookInfoListener);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		SpecificUserBean item = dataList.get(position);
		mAsyncImageCache.displayImage(mHolder.headerIv2, R.drawable.ic_launcher, 
				new AsyncImageCache.NetworkImageGenerator(item.SpecificUserHeadUrl, item.SpecificUserHeadUrl));
		mAsyncImageCache.displayImage(mHolder.headerIv1, R.drawable.ic_launcher, 
				new AsyncImageCache.NetworkImageGenerator(item.SpecificUserHeadUrl, item.SpecificUserHeadUrl));
		mHolder.nameTv2.setText(item.SpecificUserName);
		mHolder.time.setText("20150710 11:00");
		mHolder.surplus.setText("剩余1小时");
		mHolder.tv.setText("106关注");
		mHolder.nameTv1.setText(item.SpecificUserName);
		mHolder.infoLayout.setTag(item.SpecificUserPhone);
		return convertView;
	}
	
	//������ת������ʦ����
	private OnClickListener lookInfoListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String phoneNum = (String)v.getTag();
			Intent mIntent = new Intent(mContext, DoctorPersonalInfo.class);
			mIntent.putExtra(Constants.PhoneNum, phoneNum);
			mContext.startActivity(mIntent);
		}
	};
	
	
	
	
	private class Holder{
		ImageView headerIv2;
		TextView nameTv2;
		TextView tv;
		TextView time;
		TextView surplus;
		ImageView headerIv1;
		TextView nameTv1;
		View infoLayout;
	}




	
}
