package com.dcy.psychology.view;

import com.dcy.psychology.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PageIndicatorView extends LinearLayout {
	private int mCount;
	private LayoutParams mLayoutParams;
	private boolean isSingle = false;
	
	public PageIndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = getResources().getDimensionPixelSize(
				R.dimen.margin_1x);
	}

	public PageIndicatorView(Context context) {
		this(context, null);
	}

	public void setCount(int count) {
		mCount = count;
		generateView();
	}
	
	public void setCount(int count, boolean isSingle){
		this.isSingle = isSingle;
		setCount(count);
	}

	private void generateView() {
		this.removeAllViews();
		ImageView mImageView;
		for (int i = 0; i < mCount; i++) {
			mImageView = new ImageView(getContext());
			mImageView.setLayoutParams(mLayoutParams);
			mImageView.setImageResource(isSingle ? R.drawable.banner_page_indicator_selector : 
				R.drawable.page_indicator_selector);
			this.addView(mImageView);
		}
	}

	public void updateIndicator(int currentIndex) {
		if (currentIndex >= mCount)
			return;
		for (int i = 0; i < mCount; i++) {
			if(isSingle){
				this.getChildAt(i).setSelected(i == currentIndex ? true : false);
			}else {
				this.getChildAt(i).setSelected(i <= currentIndex ? true : false);
			}
		}
	}
}
