package com.dcy.psychology.adapter;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.ArticleBean;
import com.dcy.psychology.util.AsyncImageCache;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ArticleListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<ArticleBean> dataList;
	private AsyncImageCache mAsyncImageCache;
	
	public ArticleListAdapter(Context context, ArrayList<ArticleBean> dataList) {
		mInflater = LayoutInflater.from(context);
		mAsyncImageCache = AsyncImageCache.from(context);
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
			mHolder = new Holder();
			convertView = mInflater.inflate(R.layout.item_home_pic_layout, null);
			mHolder.mImageView = (ImageView) convertView.findViewById(R.id.item_thumb_iv);
			mHolder.mTitleView = (TextView) convertView.findViewById(R.id.item_text_tv);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		ArticleBean itemBean = dataList.get(position);
		mAsyncImageCache.displayImage(mHolder.mImageView, R.drawable.ic_launcher, 
				new AsyncImageCache.NetworkImageGenerator(itemBean.getArticleSmallImgUrl(), itemBean.getArticleSmallImgUrl()));
		mHolder.mTitleView.setText(itemBean.getArticleName());
		return convertView;
	}
	
	private class Holder{
		TextView mTitleView;
		ImageView mImageView;
	}
}
