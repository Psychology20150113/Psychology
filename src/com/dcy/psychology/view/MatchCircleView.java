package com.dcy.psychology.view;

import java.util.ArrayList;

import com.dcy.psychology.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

public class MatchCircleView extends View{

	private Context mContext;
	private DisplayMetrics dm;
	private Paint mPaint;
	private final int OuterWidth = 3;
	private final String OuterColor = "#ed823b";
	private float ratio;
	private final int InnerWidth = 1;
	private final String InnerColor = "#b5b5b6";
	private boolean haveData = false;
	private String matchFormat;
	
	public MatchCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		dm = getResources().getDisplayMetrics();
		matchFormat = getResources().getString(R.string.match_format);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);
	}

	public MatchCircleView(Context context) {
		this(context, null);
	}
	
	public void setData(float ratio){
		if(ratio < 0)
			return;
		this.ratio = ratio;
		haveData = true;
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(haveData){
			RectF mOuterRectF = new RectF((OuterWidth/2)*dm.density, (OuterWidth/2)*dm.density, 
					getWidth() - (OuterWidth/2)*dm.density, getWidth() - (OuterWidth/2)*dm.density);
			mPaint.setColor(Color.parseColor(OuterColor));
			mPaint.setStrokeWidth(OuterWidth * dm.density);
			canvas.drawArc(mOuterRectF, -90 + 180 * (1 - ratio), 360 * ratio, false, mPaint);
			mPaint.setColor(Color.parseColor(InnerColor));
			canvas.drawArc(mOuterRectF, 270 - 180 * (1 - ratio), 360 * (1 - ratio), false, mPaint);
//			float innerLength = getWidth() - 70*dm.density - (InnerWidth/2)*dm.density;
//			RectF mInnerRectF = new RectF(70*dm.density + (InnerWidth/2)*dm.density, 
//					70*dm.density + (InnerWidth/2)*dm.density, innerLength, innerLength);
			mPaint.setStrokeWidth(InnerWidth * dm.density);
			canvas.drawCircle(getWidth()/2, getHeight()/2, 42*dm.density, mPaint);
		}
	}
	
	private float getFrontHeight(Paint paint){
		FontMetrics fm = paint.getFontMetrics();
		return (float)Math.ceil(fm.descent - fm.ascent);
	}
}
