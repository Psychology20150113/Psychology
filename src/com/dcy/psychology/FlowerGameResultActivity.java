package com.dcy.psychology;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.util.FlowerGameUtils;
import com.dcy.psychology.view.FlowerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FlowerGameResultActivity extends Activity implements
		OnClickListener {
	private FlowerView mFlowerLeft;
	private FlowerView mFlowerRight;
	private TextView mPointText;

	private ArrayList<Integer> mAnswerOrder;
	private ArrayList<Integer> mMineOrder;
	private int mPetalShape;
	private int mColorLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Intent mIntent = getIntent();
		mAnswerOrder = mIntent
				.getIntegerArrayListExtra(FlowerGameUtils.AnswerOrder);
		mMineOrder = mIntent
				.getIntegerArrayListExtra(FlowerGameUtils.MineOrder);
		mPetalShape = mIntent.getIntExtra(FlowerGameUtils.PetalShape, 0);
		mColorLevel = mIntent.getIntExtra(FlowerGameUtils.ColorLevel, 0);
		setContentView(R.layout.game_flower_result);
		initView();
	}

	private void initView() {
		mFlowerLeft = (FlowerView) findViewById(R.id.flower_result_left);
		mFlowerRight = (FlowerView) findViewById(R.id.flower_result_right);
		mFlowerLeft.setCustomOrder(mAnswerOrder);
		mFlowerRight.setCustomOrder(mMineOrder);
		mFlowerLeft.setPetalShape(mPetalShape);
		mFlowerRight.setPetalShape(mPetalShape);
		mFlowerLeft.setFlowerLevel(mColorLevel);
		mFlowerRight.setFlowerLevel(mColorLevel);
		mFlowerLeft.showFlower();
		mFlowerRight.showFlower();
		mPointText = (TextView) findViewById(R.id.flower_result_tv);
		mPointText.setText(checkResult());
		findViewById(R.id.replay_btn).setOnClickListener(this);
		findViewById(R.id.end_btn).setOnClickListener(this);
	}

	private String checkResult() {
		int mRightNum = 0;
		for (int i = 0; i < 4; i++) {
			if (mAnswerOrder.get(i) == mMineOrder.get(i))
				mRightNum++;
		}
		return String.format(getString(R.string.point), mRightNum * 100 / 4);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.replay_btn:
			startActivity(new Intent(this,FlowerGameChooseActivity.class));
			break;
		case R.id.end_btn:
			break;
		default:
			break;
		}
		finish();
	}
}
