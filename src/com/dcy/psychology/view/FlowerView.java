package com.dcy.psychology.view;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.util.FlowerGameUtils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class FlowerView extends FrameLayout {
	private ImageView[] petalArray;
	private Context mContext;
	private LayoutInflater mInflater;
	private ObjectAnimator mPetalAnimator;
	private final int AnimatorDuration = 2000;

	private int petalShape = 0;
	private int mFlowerLevel = 0;
	private boolean mOprationMode = false;
	private boolean mCustonMode = false;
	private int mSelectPetal = 0;

	private ArrayList<Integer> mSelectBackGrounds = new ArrayList<Integer>();

	public FlowerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	public FlowerView(Context context) {
		this(context, null);
	}

	public void setPetalShape(int shape) {
		petalShape = shape;
	}

	public int getPetalShape() {
		return petalShape;
	}

	public void setFlowerLevel(int level) {
		mFlowerLevel = level;
	}

	public int getFlowerLevel() {
		return mFlowerLevel;
	}

	public void setOprationMode(boolean opration) {
		mOprationMode = opration;
	}

	private int getRandomNum() {
		return (int) (Math.random() * FlowerGameUtils.levelFlower[mFlowerLevel].length);
	}

	public void showFlower() {
		View view = mInflater.inflate(R.layout.flower_item, null);
		petalArray = new ImageView[4];
		petalArray[0] = (ImageView) view.findViewById(R.id.petal_one_iv);
		petalArray[1] = (ImageView) view.findViewById(R.id.petal_two_iv);
		petalArray[2] = (ImageView) view.findViewById(R.id.petal_three_iv);
		petalArray[3] = (ImageView) view.findViewById(R.id.petal_four_iv);
		int mBackgroundRandom;
		for (int i = 0; i < 4; i++) {
			petalArray[i].setTag(i);
			// petalArray[i].setBackgroundColor(Color.parseColor("#00ff00"));
			petalArray[i]
					.setImageResource(FlowerGameUtils.flowerShape[petalShape][i]);
			mBackgroundRandom = getRandomNum();
			petalArray[i].setBackgroundResource(getBackgroundResId(i,
					mBackgroundRandom));
			if (!mCustonMode) {
				if (mOprationMode) {
					petalArray[i].setOnClickListener(mPetalListener);
					mSelectBackGrounds.add(-1);
				} else {
					mSelectBackGrounds.add(mBackgroundRandom);
				}
			}
			/*
			 * petalArray[i].setPivotX(0);
			 * petalArray[i].setPivotY(mResources.getDimensionPixelSize
			 * (R.dimen.petal_height)/2); mPetalAnimator =
			 * ObjectAnimator.ofFloat(petalArray[i], "rotation", 0 , i *
			 * 360/petalNum); mPetalAnimator.setDuration(AnimatorDuration);
			 * mPetalAnimator.start();
			 */
		}
		addView(view);
	}

	private int getBackgroundResId(int position, int mBackgroundRandom) {
		if (mOprationMode)
			return R.drawable.flower_default_bg;
		if (mCustonMode && mSelectBackGrounds.get(position) == -1)
			return R.drawable.flower_default_bg;
		return FlowerGameUtils.levelFlower[mFlowerLevel][mCustonMode ? mSelectBackGrounds
				.get(position) : mBackgroundRandom];
	}

	private OnClickListener mPetalListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mSelectPetal = (Integer) v.getTag();
		}
	};

	public ImageView getSelectPetal() {
		return mOprationMode ? petalArray[mSelectPetal] : null;
	}

	public ArrayList<Integer> getBackGroundOrder() {
		return mSelectBackGrounds;
	}

	public void updateSelectOrder(int position) {
		mSelectBackGrounds.remove(mSelectPetal);
		mSelectBackGrounds.add(mSelectPetal, position);
	}

	public void setCustomOrder(ArrayList<Integer> customOrder) {
		if (customOrder == null)
			return;
		mSelectBackGrounds = customOrder;
		mCustonMode = true;
	}
}
