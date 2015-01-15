package com.dcy.psychology.view;

import java.util.ArrayList;

import com.dcy.psychology.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SeaChooseLayout extends LinearLayout {
	private Context mContext;
	private DisplayMetrics dm;
	private OnClickListener mListener;
	private LayoutParams mLayoutParams;
	
	public SeaChooseLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		dm = mContext.getResources().getDisplayMetrics();
		mLayoutParams = new LayoutParams((int)(38 * dm.density), (int)(35 * dm.density));
	}

	public SeaChooseLayout(Context context) {
		this(context, null);
	}
	
	public void setData(ArrayList<String> colors){
		for(String color : colors){
			ImageView item = new ImageView(mContext);
			item.setLayoutParams(mLayoutParams);
			item.setImageResource(R.drawable.icon_color_choose);
			item.setBackgroundColor(Color.parseColor(color));
			item.setTag(color);
			item.setOnClickListener(colorChooseListener);
			this.addView(item);
		}
	}
	
	public void setColorChooseListener(OnClickListener listener){
		mListener = listener;
	}
	
	private OnClickListener colorChooseListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(mListener != null)
				mListener.onClick(v);
		}
	};
	
}
