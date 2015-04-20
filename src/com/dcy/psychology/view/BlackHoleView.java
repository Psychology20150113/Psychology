package com.dcy.psychology.view;

import com.dcy.psychology.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class BlackHoleView extends View {
	private DisplayMetrics dm;
	private Context mContext;
	private Paint mPaint;
	private float angle = 90;
	private float radius;
	private String content;
	private Runnable finishRunnable;
	
	public BlackHoleView(Context context) {
		this(context, null);
	}
	
	public BlackHoleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		dm = context.getResources().getDisplayMetrics();
		mContext = context;
		mPaint = new Paint();
		mPaint.setTextSize(18*dm.density);
		mPaint.setColor(Color.WHITE);
	}

	public void setFinishRunnable(Runnable runnable){
		finishRunnable = runnable;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(TextUtils.isEmpty(content)){
			radius = getWidth() > getHeight() ? getHeight()/2 : getWidth()/2;
			return;
		}
		if(radius < 5 * dm.density){
			radius = getWidth() > getHeight() ? getHeight()/2 : getWidth()/2;
			angle = 90;
			content = null;
			if(finishRunnable != null){
				finishRunnable.run();
			}
			return;
		}
		canvas.drawText(content, (int)(getWidth()/2 + radius * Math.cos(angle * Math.PI / 180)), 
				(int)(getHeight()/2 + radius * Math.sin(angle * Math.PI / 180)), mPaint);
		radius -= 1 * dm.density;
		angle += 2;
		postDelayed(new Runnable() {
			@Override
			public void run() {
				invalidate();
			}
		}, 5);
	}

	public void initText(String content){
		this.content = content;
		invalidate();
	}
}
