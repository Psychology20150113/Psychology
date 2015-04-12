package com.dcy.psychology.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dcy.psychology.R;
import com.umeng.analytics.MobclickAgent;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

	private int mSelectPosition = -1;
	private View outAnimView;
	private ListView mListView;
	
	public interface OnPlatClickListener{
		public void itemClick(int themeId , int itemId);
	}
	
	public void setOnPlatClickListener(OnPlatClickListener listener){
		this.mListener = listener;
	}
	
	public StudentGrowPlatformAdapter(Context context, ListView mListView) {
		mInflater = LayoutInflater.from(context);
		Resources resources = context.getResources();
		mTitleArray = resources.getStringArray(R.array.grow_platform_array);
		mLabelArray = resources.getStringArray(R.array.label_platform_array);
		this.mContext = context;
		this.mListView = mListView;
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
			holder.mChooseView = viewHolder.findViewById(R.id.choose_ll);
			viewHolder.setTag(holder);
		}else {
			holder = (Holder) viewHolder.getTag();
		}
		holder.mLabelView.setText(mLabelArray[position]);
		holder.mTitleView.setText(mTitleArray[position]);
		holder.picView.setBackgroundResource(animationRes[position]);
		holder.picView.setTag(position);
		holder.picView.setOnClickListener(showDialogListener);
		holder.mChooseView.setVisibility(mSelectPosition == position ? View.VISIBLE : View.GONE);
		((AnimationDrawable)holder.picView.getBackground()).start();
		return viewHolder;
	}

	private OnClickListener showDialogListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			/*final int position = (Integer)v.getTag();
			Builder mBuilder = new Builder(mContext);
			View dialogView = mInflater.inflate(R.layout.dialog_student_platform, null);
			((TextView)dialogView.findViewById(R.id.platform_title_tv)).setText(mTitleArray[position]);
			setListener(position , dialogView);	
			mBuilder.setView(dialogView);
			mDialog = mBuilder.create();
			mDialog.show();*/
			int position = (Integer) v.getTag();
			Map<String, String> mThemeMap = new HashMap<String, String>();
			mThemeMap.put("platform", mTitleArray[position]);
			MobclickAgent.onEvent(mContext, "click_student_platform", mThemeMap);
			if(position == mSelectPosition)
				return;
			if(mSelectPosition >= mListView.getFirstVisiblePosition() && mSelectPosition <= mListView.getLastVisiblePosition()){
				outAnimView = mListView.getChildAt(mSelectPosition - mListView.getFirstVisiblePosition()).findViewById(R.id.choose_ll);
				Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_to_left);
				animation.setAnimationListener(mAnimationListener);
				outAnimView.startAnimation(animation);
			}
			final View view = ((RelativeLayout)v.getParent()).findViewById(R.id.choose_ll);
			view.setVisibility(View.VISIBLE);
			setListener((Integer)v.getTag(), view);
			view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_from_right));
			mSelectPosition = (Integer)v.getTag();
		}
	};
	
	private AnimationListener mAnimationListener = new AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			outAnimView.setVisibility(View.GONE);
		}
	};
	
	private void setListener(final int position, View dialogView) {
		dialogView.findViewById(R.id.student_grow_ll)
			.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//mDialog.dismiss();
				if(mListener != null)
					mListener.itemClick(position, 0);
			}
		});
		dialogView.findViewById(R.id.student_test_ll)
			.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//mDialog.dismiss();
				if(mListener != null)
					mListener.itemClick(position, 1);
			}
		});
		dialogView.findViewById(R.id.student_pic_ll)
			.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//mDialog.dismiss();
				if(mListener != null)
					mListener.itemClick(position, 2);
			}
		});
	}
	
	private class Holder{
		TextView mLabelView;
		TextView mTitleView;
		ImageView picView;
		View mChooseView;
	}

}
