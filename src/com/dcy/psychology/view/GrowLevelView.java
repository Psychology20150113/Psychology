package com.dcy.psychology.view;

import java.util.ArrayList;
import java.util.Random;

import com.dcy.psychology.R;
import com.dcy.psychology.util.InfoShared;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class GrowLevelView extends LinearLayout{

	private Context mContext;
	
	private int btnWidth,btnHeight;
	private OnLevelClickListener mClickListener;
	
	private ScrollView mScrollView;
	private ArrayList<ImageView> flagList = new ArrayList<ImageView>();
	private ArrayList<Button> buttonList = new ArrayList<Button>();
	private LayoutParams leftParams;
	private LayoutParams rightParams;
	private LayoutParams birdLeftParams;
	private LayoutParams birdRightParams;
	
	private int savedPosition;
	
	public interface OnLevelClickListener{
		public void onLevelClick(int level);
	}
	
	public void setLevelClickListener(OnLevelClickListener listener){
		mClickListener = listener;
	}
	
	public GrowLevelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		btnWidth = dpToPx(110);
		btnHeight = dpToPx(50);
		mScrollView = new ScrollView(context);
		this.addView(mScrollView);
		initParams();
	}

	private void initParams() {
		int value_50 = dpToPx(50);
		leftParams = new LayoutParams(btnWidth, btnHeight);
		leftParams.gravity = Gravity.LEFT;
		leftParams.leftMargin = value_50;
		
		rightParams = new LayoutParams(btnWidth, btnHeight);
		rightParams.gravity = Gravity.RIGHT;
		rightParams.rightMargin = value_50;
		
		int birdWidth = dpToPx(59);
		int birdHeight = dpToPx(54);
		birdLeftParams = new LayoutParams(birdWidth, birdHeight);
		birdLeftParams.gravity = Gravity.LEFT;
		birdLeftParams.leftMargin = dpToPx(100);
		
		birdRightParams = new LayoutParams(birdWidth, birdHeight);
		birdRightParams.gravity = Gravity.RIGHT;
		birdRightParams.rightMargin = value_50;
		
	}

	public GrowLevelView(Context context) {
		this(context, null);
	}

	public void setInfo(int count , int themeIndex , boolean isSpecial){
		InfoShared info = new InfoShared(mContext);
		savedPosition = info.getInt(String.format(info.ThemeFormat, String.valueOf(isSpecial) , themeIndex));
		LinearLayout mContentLayout = new LinearLayout(mContext);
		LayoutParams contentParams = new LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(100)*count);
		contentParams.bottomMargin = dpToPx(100);
		mContentLayout.setLayoutParams(contentParams);
		mContentLayout.setOrientation(LinearLayout.VERTICAL);
		mContentLayout.setGravity(Gravity.BOTTOM);
		mContentLayout.setBackgroundResource(R.drawable.bg_grow_level);
		for(int i = count-1 ; i >= 0 ; i --){
			initBirdFlagView(mContentLayout, i);
			initButton(mContentLayout, i);
		}
		mScrollView.addView(mContentLayout);
		if(count > 0)
			setSavedBirdPosition(count);
		new Handler().postDelayed(new Runnable() {
			public void run() {
				mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		},10);
	}

	private void initBirdFlagView(LinearLayout mContentLayout, int i) {
		ImageView bird = new ImageView(mContext);
		bird.setLayoutParams(i % 2 == 0 ? birdLeftParams : birdRightParams);
		bird.setImageResource(R.drawable.icon_bird);
		bird.setVisibility(View.INVISIBLE);
		flagList.add(bird);
		mContentLayout.addView(bird);
	}

	private void initButton(LinearLayout mContentLayout, int i) {
		Button item = new Button(mContext);
		item.setLayoutParams(i % 2 == 0 ? leftParams : rightParams);
		item.setTag(i);
		if(i <= savedPosition){
			item.setBackgroundColor(Color.parseColor("#5ce5d5"));
			item.setOnClickListener(mButtonClick);
		}else {
			item.setBackgroundColor(Color.parseColor("#999999"));
			item.setClickable(false);
		}
		item.setText("ตฺ" + (i+1) + "นุ");
		item.setGravity(Gravity.CENTER);
		buttonList.add(item);
		mContentLayout.addView(item);
	}

	private void setSavedBirdPosition(int count){
		if(savedPosition > count - 1)
			savedPosition = count - 1;
		flagList.get(count - savedPosition - 1).setVisibility(View.VISIBLE);
	}
	
	public void addLevel(int count){
		if(savedPosition == count - 1)
			return;
		flagList.get(count - savedPosition - 1).setVisibility(View.INVISIBLE);
		flagList.get(count - savedPosition - 2).setVisibility(View.VISIBLE);
		buttonList.get(count - savedPosition - 2).setBackgroundColor(Color.parseColor("#5ce5d5"));
		buttonList.get(count - savedPosition - 2).setOnClickListener(mButtonClick);
		savedPosition ++;
	}
	
	private OnClickListener mButtonClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if(mClickListener != null)
				mClickListener.onLevelClick((Integer)view.getTag());
		}
	};

	private int dpToPx(int dp){
		return (int)(dp * mContext.getResources().getDisplayMetrics().density+0.5);
	}
	
}
