package com.dcy.psychology.view;

import java.util.ArrayList;

import com.dcy.psychology.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class CustomCircleView extends View{

	private int width;
	private int height;
	private Context mContext;
	private Resources mResources;
	
	private float radius;
	private ArrayList<ImageView> itemList = new ArrayList<ImageView>();
	
	private final int ItemNum = 5;
	
	public CustomCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mResources = getResources();
		radius = mResources.getDimensionPixelSize(R.dimen.petal_width);
	}

	public CustomCircleView(Context context) {
		this(context, null);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getWidth();
		height = getHeight();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
	}
	
	private void initList(){
		for(int i = 0 ; i < 5 ; i ++){
			ImageView mImageView = new ImageView(mContext);
			
		}
	}
}
