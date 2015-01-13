package com.dcy.psychology.adapter;

import java.util.ArrayList;

import com.dcy.psychology.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StudentGrowPlatformAdapter extends BaseAdapter {
	private String[] mTitleArray;
	private String[] mLabelArray;
	private LayoutInflater mInflater;
	private OnPlatClickListener mListener;
	
	private Context mContext;
	private AlertDialog mDialog;
	private int[] animationRes = {R.drawable.grow_confident_anim, R.drawable.grow_person_anim, 
			R.drawable.grow_love_anim, R.drawable.grow_time_anim};
	
	public interface OnPlatClickListener{
		public void itemClick(int themeId , int itemId);
	}
	
	public void setOnPlatClickListener(OnPlatClickListener listener){
		this.mListener = listener;
	}
	
	public StudentGrowPlatformAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		Resources resources = context.getResources();
		mTitleArray = resources.getStringArray(R.array.grow_platform_array);
		mLabelArray = resources.getStringArray(R.array.label_platform_array);
		this.mContext = context;
	}
	
	@Override
	public int getCount() {
		return mTitleArray.length;
	}

	@Override
	public Object getItem(int position) {
		return mTitleArray[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View viewHolder, ViewGroup group) {
		Holder holder;
		if(viewHolder == null){
			viewHolder = mInflater.inflate(R.layout.item_student_platform, null);
			holder = new Holder();
			holder.mLabelView = (TextView) viewHolder.findViewById(R.id.item_label_tv);
			holder.mTitleView = (TextView) viewHolder.findViewById(R.id.item_platform_name_tv);
			holder.picView = (ImageView) viewHolder.findViewById(R.id.item_platform_show_iv);
			viewHolder.setTag(holder);
		}else {
			holder = (Holder) viewHolder.getTag();
		}
		holder.mLabelView.setText(mLabelArray[position]);
		holder.mTitleView.setText(mTitleArray[position]);
		holder.picView.setBackgroundResource(animationRes[position]);
		holder.picView.setTag(position);
		holder.picView.setOnClickListener(showDialogListener);
		((AnimationDrawable)holder.picView.getBackground()).start();
		return viewHolder;
	}

	private OnClickListener showDialogListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			final int position = (Integer)v.getTag();
			Builder mBuilder = new Builder(mContext);
			View dialogView = mInflater.inflate(R.layout.dialog_student_platform, null);
			((TextView)dialogView.findViewById(R.id.platform_title_tv)).setText(mTitleArray[position]);
			setListener(position , dialogView);	
			mBuilder.setView(dialogView);
			mDialog = mBuilder.create();
			mDialog.show();
		}
	};
	
	private void setListener(final int position, View dialogView) {
		dialogView.findViewById(R.id.student_grow_ll)
			.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mDialog.dismiss();
				if(mListener != null)
					mListener.itemClick(position, 0);
			}
		});
		dialogView.findViewById(R.id.student_test_ll)
			.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mDialog.dismiss();
				if(mListener != null)
					mListener.itemClick(position, 1);
			}
		});
		dialogView.findViewById(R.id.student_pic_ll)
			.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mDialog.dismiss();
				if(mListener != null)
					mListener.itemClick(position, 2);
			}
		});
	}
	
	private class Holder{
		TextView mLabelView;
		TextView mTitleView;
		ImageView picView;
	}

}
