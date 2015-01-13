package com.dcy.psychology.view;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.GameFishBean;
import com.dcy.psychology.gsonbean.GameFishBean.Fish;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SeaFishView extends ViewGroup {
	private ArrayList<Fish> fishData;
	private ArrayList<ImageView> fishView = new ArrayList<ImageView>();
	private Context mContext;
	
	public SeaFishView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public SeaFishView(Context context) {
		this(context, null);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(fishData == null)
			return;
		for(int i = 0 ; i < fishData.size() ; i ++){
			Fish dataItem = fishData.get(i);
			ImageView fishItem = fishView.get(i);
			fishItem.layout(dataItem.getPointx(), dataItem.getPointy(),
					dataItem.getPointx() + dataItem.getWidth(), dataItem.getPointy() + dataItem.getHeight());
		}
	}
	
	public void layoutView(GameFishBean bean){
		if(bean == null)
			return;
		this.fishData = bean.getFish();
		for(Fish item : fishData){
			int fishRes = 0;
			switch (item.getType()) {
			case 0:
				fishRes = R.drawable.icon_fish_one;
				break;
			case 1:
				fishRes = R.drawable.icon_fish_two;
				break;
			default:
				break;
			}
			ImageView fishItem = new ImageView(mContext);
			fishItem.setImageResource(fishRes);
			fishView.add(fishItem);
			this.addView(fishItem);
		}
		invalidate();
	}
}
