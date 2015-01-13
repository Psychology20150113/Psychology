package com.dcy.psychology;

import com.dcy.psychology.util.FlowerGameUtils;
import com.dcy.psychology.view.FlowerChooseBtn;
import com.dcy.psychology.view.FlowerChooseBtn.ChangeBackGroundListener;
import com.dcy.psychology.view.FlowerView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FlowerGameActivity extends Activity implements OnClickListener {
	private FlowerView mLookFlower;
	private TextView mCountDownText;
	private View mOprationView;
	private FlowerView mOprationFlower;
	private FlowerChooseBtn mChooseBtn;
	private Button mCommitBtn;

	private int mCountDownTime = 5;
	private int mOprationTime = 5;
	private int mPetalShape = 0;
	private int mColorLevel = 0;

	private ChangeBackGroundListener mBackGroundListener = new ChangeBackGroundListener() {
		// change color and update order
		@SuppressLint("NewApi")
		@Override
		public void change(Drawable changeDrawable, int position) {
			ImageView mSelectPetal = mOprationFlower.getSelectPetal();
			if (mSelectPetal != null)
				mSelectPetal.setBackground(changeDrawable);
			mOprationFlower.updateSelectOrder(position);
		}
	};

	private Handler mCountDownHandler = new Handler();
	private Runnable mCountDownRunnable = new Runnable() {
		@Override
		public void run() {
			if (mCountDownTime >= 0) {
				mCountDownText.setVisibility(View.GONE);
				if (mCountDownTime < 4 && mCountDownTime > 0) {
					mCountDownText.setText(String.valueOf(mCountDownTime));
					mCountDownText.setVisibility(View.VISIBLE);
				} else if (mCountDownTime == 0) {
					mCountDownText.setText(R.string.opration_start);
					mCountDownText.setVisibility(View.VISIBLE);
				}
				mCountDownHandler.postDelayed(mCountDownRunnable, 1000);
				mCountDownTime--;
			} else {
				mCountDownText.setVisibility(View.GONE);
				if (mOprationTime >= 0) {
					if (mOprationTime > 0 && mOprationTime < 4) {
						mCountDownText.setText(String.valueOf(mOprationTime));
						mCountDownText.setVisibility(View.VISIBLE);
					} else if (mOprationTime == 0) {
						mCountDownText.setText(R.string.time_over);
						mCountDownText.setVisibility(View.VISIBLE);
					}
					mCountDownHandler.postDelayed(mCountDownRunnable, 1000);
					mOprationTime--;
				} else {
					turnToResult();
				}
				mLookFlower.setVisibility(View.GONE);
				mOprationView.setVisibility(View.VISIBLE);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.game_flower);
		setLevel();
		initView();
		mCountDownHandler.postDelayed(mCountDownRunnable, 2000);
	}

	private void setLevel() {
		mPetalShape = (int) (Math.random() * FlowerGameUtils.flowerShape.length);
		Intent mIntent = getIntent();
		mCountDownTime += (2 - mIntent
				.getIntExtra(FlowerGameUtils.TimeLevel, 0)) * 5;
		mOprationTime = mCountDownTime;
		mColorLevel = mIntent.getIntExtra(FlowerGameUtils.ColorLevel, 0);
	}

	private void initView() {
		mLookFlower = (FlowerView) findViewById(R.id.flower_look_view);
		mLookFlower.setPetalShape(mPetalShape);
		mLookFlower.setFlowerLevel(mColorLevel);
		mLookFlower.showFlower();
		mCountDownText = (TextView) findViewById(R.id.count_down_tv);
		mCountDownText.setText(R.string.prepare);
		mOprationView = findViewById(R.id.flower_opration_view);
		mOprationFlower = (FlowerView) findViewById(R.id.flower_left_view);
		mOprationFlower.setOprationMode(true);
		mOprationFlower.setPetalShape(mPetalShape);
		mOprationFlower.setFlowerLevel(mColorLevel);
		mOprationFlower.showFlower();
		mChooseBtn = (FlowerChooseBtn) findViewById(R.id.flower_chosse_btn);
		mChooseBtn.setFlowerLevel(mColorLevel);
		mChooseBtn.showButtons();
		mChooseBtn.setChangeBackGroundListener(mBackGroundListener);
		mCommitBtn = (Button) findViewById(R.id.flower_commit_btn);
		mCommitBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.flower_commit_btn:
			turnToResult();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		mCountDownHandler.removeCallbacks(mCountDownRunnable);
	}

	private void turnToResult() {
		mCountDownHandler.removeCallbacks(mCountDownRunnable);
		Intent mIntent = new Intent(FlowerGameActivity.this,
				FlowerGameResultActivity.class);
		mIntent.putIntegerArrayListExtra(FlowerGameUtils.AnswerOrder,
				mLookFlower.getBackGroundOrder());
		mIntent.putIntegerArrayListExtra(FlowerGameUtils.MineOrder,
				mOprationFlower.getBackGroundOrder());
		mIntent.putExtra(FlowerGameUtils.ColorLevel, mColorLevel);
		mIntent.putExtra(FlowerGameUtils.PetalShape, mPetalShape);
		startActivity(mIntent);
		finish();
	}

}
