package com.dcy.psychology.view;

import com.dcy.psychology.R;
import com.dcy.psychology.util.FlowerGameUtils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class FlowerChooseBtn extends LinearLayout {
	private int mFlowerLevel = 0;

	private Context mContext;
	private Resources mResources;
	private ChangeBackGroundListener mBackGroundListener;

	// while click button change the petal color and change the order
	public interface ChangeBackGroundListener {
		public void change(Drawable changeDrawable, int position);
	}

	public FlowerChooseBtn(Context context) {
		this(context, null);
	}

	public FlowerChooseBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(VERTICAL);
		this.setGravity(Gravity.CENTER);
		mContext = context;
		mResources = mContext.getResources();
	}

	public void setFlowerLevel(int level) {
		mFlowerLevel = level;
	}

	public int getFlowerLevel() {
		return mFlowerLevel;
	}

	public void setChangeBackGroundListener(
			ChangeBackGroundListener backGroundListener) {
		mBackGroundListener = backGroundListener;
	}

	public void showButtons() {
		int mButtonNum = FlowerGameUtils.levelFlower[mFlowerLevel].length;
		int rowNums;
		if (mButtonNum % 3 == 0) {
			rowNums = mButtonNum / 3;
		} else {
			rowNums = mButtonNum / 3 + 1;
		}
		LinearLayout mRowLayout;
		ImageButton mImageButton;
		LayoutParams mButtonParams = new LayoutParams(
				mResources.getDimensionPixelSize(R.dimen.petal_width),
				mResources.getDimensionPixelSize(R.dimen.petal_height));
		mButtonParams.setMargins(5, 5, 5, 5);
		for (int i = 0; i < rowNums; i++) {
			mRowLayout = new LinearLayout(mContext);
			mRowLayout.setOrientation(HORIZONTAL);
			for (int j = 0; j < 3; j++) {
				mImageButton = new ImageButton(mContext);
				mImageButton.setTag(i * 3 + j);
				mImageButton.setLayoutParams(mButtonParams);
				mImageButton
						.setBackgroundResource(FlowerGameUtils.levelFlower[mFlowerLevel][i
								* 3 + j]);
				mImageButton.setOnClickListener(mFlowerChooseBtnListener);
				mRowLayout.addView(mImageButton);
				if (mButtonNum == i * 3 + j + 1)
					break;
			}
			this.addView(mRowLayout);
		}
	}

	private OnClickListener mFlowerChooseBtnListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mBackGroundListener != null)
				mBackGroundListener.change(v.getBackground(),
						(Integer) v.getTag());
		}
	};
}
