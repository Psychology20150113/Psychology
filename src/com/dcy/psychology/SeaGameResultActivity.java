package com.dcy.psychology;

import java.util.ArrayList;

import com.dcy.psychology.gsonbean.GameFishBean;
import com.dcy.psychology.view.SeaFishView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SeaGameResultActivity extends Activity implements OnClickListener{
	private ArrayList<String> rightColors;
	private ArrayList<String> mineColors;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_sea_result_layout);
		Intent mIntent = getIntent();
		rightColors = mIntent.getStringArrayListExtra("rightColors");
		mineColors = mIntent.getStringArrayListExtra("mineColors");
		initView(mIntent);
	}

	private void initView(Intent mIntent) {
		GameFishBean bean = (GameFishBean) mIntent.getSerializableExtra("gamefishbean");
		DisplayMetrics dm = getResources().getDisplayMetrics();
		float ratio_xy = ((float)dm.widthPixels) / dm.heightPixels;
		float widthPixel = dm.widthPixels - 30 * dm.density;
		SeaFishView rightView = (SeaFishView)findViewById(R.id.right_view);
		rightView.getLayoutParams().width = (int)(widthPixel * 1 / 2);
		rightView.getLayoutParams().height = (int)(widthPixel * 1 / 2 / ratio_xy);
		rightView.setData(bean , rightColors);
		SeaFishView mineView = (SeaFishView)findViewById(R.id.mine_view);
		mineView.getLayoutParams().width = (int)(widthPixel * 1 / 2);
		mineView.getLayoutParams().height = (int)(widthPixel * 1 / 2 / ratio_xy);
		mineView.setData(bean , mineColors);
		findViewById(R.id.complete_iv).setOnClickListener(this);
		((TextView)findViewById(R.id.result_tv)).setText(checkResult());
	}
	
	private String checkResult() {
		if(mineColors == null || rightColors == null)
			return "";
		int mRightNum = 0;
		for (int i = 0; i < mineColors.size(); i++) {
			if (mineColors.get(i).equals(rightColors.get(i)))
				mRightNum++;
		}
		return String.format(getString(R.string.point), mRightNum * 100 / mineColors.size());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.complete_iv:
			startActivity(new Intent(this, SeaGameActivity.class));
			finish();
			break;

		default:
			break;
		}
	}
}
