package com.dcy.psychology.view;

import com.dcy.psychology.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

public class CornerImageView extends ImageView {

    private int type;  
    private static final int TYPE_CIRCLE = 0;  
    private static final int TYPE_ROUND = 1; 
    
    private static final int BODER_RADIUS_DEFAULT = 10;  
    private int mBorderRadius;  
    private Paint mBitmapPaint;  
    private int mRadius;  
    private Matrix mMatrix;  
    private BitmapShader mBitmapShader;  
    private int mWidth;  
    private RectF mRoundRect;  
    
	public CornerImageView(Context context){
		this(context, null);
	}
	
	public CornerImageView(Context context, AttributeSet attrs){
		super(context, attrs);
		mMatrix = new Matrix();
		mBitmapPaint = new Paint();
		mBitmapPaint.setAntiAlias(true);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CornerImageView);
		
		mBorderRadius = a.getDimensionPixelSize(
				R.styleable.CornerImageView_borderRadius, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
								BODER_RADIUS_DEFAULT, getResources()
										.getDisplayMetrics()));// 榛樿涓�10dp  
		type = a.getInt(R.styleable.CornerImageView_type, TYPE_ROUND);
		a.recycle();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (type == TYPE_CIRCLE){
			mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
			mRadius = mWidth / 2;
			setMeasuredDimension(mWidth, mWidth);
		}
		
	}
	
	private void setUpShader(){
		Drawable drawable = getDrawable();
		if (drawable == null){
			return;
		}

		Bitmap bmp = drawableToBitamp(drawable);
		mBitmapShader = new BitmapShader(bmp, TileMode.CLAMP, TileMode.CLAMP);
		float scale = 1.0f;
		if (type == TYPE_CIRCLE){
			int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
			scale = mWidth * 1.0f / bSize;

		} else if (type == TYPE_ROUND){
			Log.e("TAG",
					"b'w = " + bmp.getWidth() + " , " + "b'h = "
							+ bmp.getHeight());
			if (!(bmp.getWidth() == getWidth() && bmp.getHeight() == getHeight())){
				scale = Math.max(getWidth() * 1.0f / bmp.getWidth(),
						getHeight() * 1.0f / bmp.getHeight());
			}

		}
		mMatrix.setScale(scale, scale);
		mBitmapShader.setLocalMatrix(mMatrix);
		mBitmapPaint.setShader(mBitmapShader);
	}

	
	@Override
	protected void onDraw(Canvas canvas){
		Log.e("TAG", "onDraw");
		if (getDrawable() == null){
			return;
		}
		setUpShader();

		if (type == TYPE_ROUND){
			canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
					mBitmapPaint);
		} else{
			canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
			// drawSomeThing(canvas);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);

		// 鍦嗚鍥剧墖鐨勮寖鍥�  
		if (type == TYPE_ROUND)
			mRoundRect = new RectF(0, 0, w, h);
	}
	
	private static final String STATE_INSTANCE = "state_instance";
	private static final String STATE_TYPE = "state_type";
	private static final String STATE_BORDER_RADIUS = "state_border_radius";

	@Override
	protected Parcelable onSaveInstanceState(){
		Bundle bundle = new Bundle();
		bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
		bundle.putInt(STATE_TYPE, type);
		bundle.putInt(STATE_BORDER_RADIUS, mBorderRadius);
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state){
		if (state instanceof Bundle){
			Bundle bundle = (Bundle) state;
			super.onRestoreInstanceState(((Bundle) state)
					.getParcelable(STATE_INSTANCE));
			this.type = bundle.getInt(STATE_TYPE);
			this.mBorderRadius = bundle.getInt(STATE_BORDER_RADIUS);
		} else{
			super.onRestoreInstanceState(state);
		}

	}

	public void setBorderRadius(int borderRadius){
		int pxVal = dp2px(borderRadius);
		if (this.mBorderRadius != pxVal){
			this.mBorderRadius = pxVal;
			invalidate();
		}
	}

	public void setType(int type){
		if (this.type != type){
			this.type = type;
			if (this.type != TYPE_ROUND && this.type != TYPE_CIRCLE){
				this.type = TYPE_CIRCLE;
			}
			requestLayout();
		}

	}

	public int dp2px(int dpVal){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, getResources().getDisplayMetrics());
	}
	
	/** 
     * drawable杞琤itmap 
     *  
     * @param drawable 
     * @return 
     */  
    private Bitmap drawableToBitamp(Drawable drawable){ 
    	
        if (drawable instanceof BitmapDrawable){  
            BitmapDrawable bd = (BitmapDrawable) drawable;  
            return bd.getBitmap();  
        }  
        int w = drawable.getIntrinsicWidth();  
        int h = drawable.getIntrinsicHeight();  
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);  
        Canvas canvas = new Canvas(bitmap);  
        drawable.setBounds(0, 0, w, h);  
        drawable.draw(canvas);  
        return bitmap;  
    }
}
