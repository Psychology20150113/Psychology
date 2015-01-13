package com.dcy.psychology.view;

import com.dcy.psychology.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class BlackHoleView extends View {
	private DisplayMetrics dm;
	private Context mContext;
	private Paint mPaint;
	
	private final int RowNum = 3;
	private Bitmap[] mBitmapArray;
	
	
	public BlackHoleView(Context context) {
		this(context, null);
	}
	
	public BlackHoleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		dm = context.getResources().getDisplayMetrics();
		mContext = context;
	}



	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

}
