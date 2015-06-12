package com.dcy.psychology.adapter;

import java.util.ArrayList;

import com.dcy.psychology.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

public class IntroducePageAdapter extends PagerAdapter {
	private int[] resId = {R.drawable.bg_introduce_one, R.drawable.bg_introduce_two, R.drawable.bg_introduce_three};
	private ArrayList<View> mIntroduceView;
	
	public IntroducePageAdapter(Context mContext) {
		mIntroduceView = new ArrayList<View>();
		LayoutParams mMatchParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		for(int i = 0; i < resId.length; i ++){
			ImageView itemView = new ImageView(mContext);
			itemView.setLayoutParams(mMatchParams);
			itemView.setScaleType(ScaleType.FIT_XY);
			itemView.setImageResource(resId[i]);
			mIntroduceView.add(itemView);
		}
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mIntroduceView.get(position));
		return mIntroduceView.get(position);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mIntroduceView.get(position));
	}
	
	@Override
	public int getCount() {
		return resId.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
