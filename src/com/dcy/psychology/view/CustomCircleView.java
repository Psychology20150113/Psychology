package com.dcy.psychology.view;

import java.util.ArrayList;

import com.dcy.psychology.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

public class CustomCircleView extends View{

	private Context mContext;
	private DisplayMetrics dm;
	private Paint mPaint;
	private Paint mTextPaint;
	private final int outerWidth = 20;
	private float outerRatio;
	private final int innerWidth = 10;
	private float innerRatio;
	private boolean haveData = false;
	
	public CustomCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		dm = getResources().getDisplayMetrics();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(50* dm.density);
		mTextPaint.setColor(Color.BLUE);
	}

	public CustomCircleView(Context context) {
		this(context, null);
	}
	
	public void setData(float outerRatio, float innerRatio){
		if(outerRatio < 0 || innerRatio < 0)
			return;
		this.outerRatio = outerRatio;
		this.innerRatio = innerRatio;
		haveData = true;
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(haveData){
			RectF mOuterRectF = new RectF((outerWidth/2)*dm.density, (outerWidth/2)*dm.density, 
					getWidth() - (outerWidth/2)*dm.density, getWidth() - (outerWidth/2)*dm.density);
			mPaint.setColor(Color.BLUE);
			mPaint.setStrokeWidth(outerWidth * dm.density);
			canvas.drawArc(mOuterRectF, 0, 360*outerRatio, false, mPaint);
			
			float innerLength = getWidth() - outerWidth*dm.density - (innerWidth/2)*dm.density;
			RectF mInnerRectF = new RectF(outerWidth*dm.density + (innerWidth/2)*dm.density, 
					outerWidth*dm.density + (innerWidth/2)*dm.density, innerLength, innerLength);
			mPaint.setColor(Color.RED);
			mPaint.setStrokeWidth(innerWidth * dm.density);
			canvas.drawArc(mInnerRectF, 0, 360*innerRatio, false, mPaint);
			
			String content = (int)(innerRatio * 100) + "%";
			int center = getWidth()/2;
			canvas.drawText(content, center - mTextPaint.measureText(content)/2, 
					center + getFrontHeight(mTextPaint)/2, mTextPaint);
		}
		
	}
	
	private float getFrontHeight(Paint paint){
		FontMetrics fm = paint.getFontMetrics();
		return (float)Math.ceil(fm.descent - fm.ascent);
	}
}
