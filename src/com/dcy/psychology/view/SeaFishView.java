package com.dcy.psychology.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.GameFishBean;
import com.dcy.psychology.gsonbean.GameFishBean.Fish;

public class SeaFishView extends ViewGroup {
	private ArrayList<Fish> fishData;
	private ArrayList<ImageView> fishView = new ArrayList<ImageView>();
	
	private Context mContext;
	private DisplayMetrics dm;
	private ArrayList<String> mRightOrder = new ArrayList<String>(); 
	private View mSelectView;
	
	private boolean isOprationMode = false;
	public SeaFishView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		dm = mContext.getResources().getDisplayMetrics();
		setBackgroundResource(R.drawable.bg_game_sea);
	}

	public SeaFishView(Context context) {
		this(context, null);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(fishData == null)
			return;
		float ratio_y = 1080f/dm.heightPixels * (1080f/getHeight());
		float ratio_x = 1920f/dm.widthPixels * (1920f/getWidth());
		for(int i = 0 ; i < fishData.size() ; i ++){
			Fish dataItem = fishData.get(i);
			ImageView fishItem = fishView.get(i);
			fishItem.layout((int)(dataItem.getPointx()/ratio_x), (int)(dataItem.getPointy()/ratio_y),
					(int)((dataItem.getPointx() + dataItem.getWidth())/ratio_x), (int)((dataItem.getPointy() + dataItem.getHeight())/ratio_y));
		}
	}
	
	public void setData(GameFishBean bean){
		setData(bean, null);
	}
	
	public void setData(GameFishBean bean , ArrayList<String> customColorList){
		if(bean == null)
			return;
		this.fishData = bean.getFish();
		ArrayList<String> colorList = bean.getColors();
		for(int i = 0 ; i < fishData.size() ; i++){
			String randomColor = colorList.get((int)(Math.random() * colorList.size()));
			initItemFish(fishData.get(i), customColorList == null ? randomColor : customColorList.get(i));
		}
	}

	private void initItemFish(Fish item, String color) {
		int fishRes = 0;
		switch (item.getType()) {
		case 0:
			fishRes = R.drawable.icon_fish_one_selector;
			break;
		case 1:
			fishRes = R.drawable.icon_fish_two_selector;
			break;
		default:
			break;
		}
		ImageView fishItem = new ImageView(mContext);
		fishItem.setImageResource(fishRes);
		fishItem.setBackgroundColor(Color.parseColor(color));
		fishItem.setScaleType(ScaleType.FIT_XY);
		fishItem.setOnClickListener(fishClickListener);
		fishView.add(fishItem);
		this.addView(fishItem);
		mRightOrder.add(color);
	}
	
	public ArrayList<String> getRightColors(){
		return mRightOrder;
	}
	
	public ArrayList<String> getMineColors(){
		ArrayList<String> mineOrder = new ArrayList<String>();
		for(ImageView itemFish : fishView){
			if(itemFish.getTag() == null){
				mineOrder.add("#999999");
			}else {
				mineOrder.add((String)itemFish.getTag());
			}
		}
		return mineOrder;
	}
	
	public void changeMode(){
		isOprationMode = true;
		for(ImageView view : fishView)
			view.setBackgroundColor(Color.parseColor("#999999"));
	}
	
	private OnClickListener fishClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(!isOprationMode || v == mSelectView)
				return;
			if(mSelectView != null)
				mSelectView.setSelected(false);
			v.setSelected(true);
			mSelectView = v;
		}
	};
	
	public View getSelectView(){
		return mSelectView;
	}
}
