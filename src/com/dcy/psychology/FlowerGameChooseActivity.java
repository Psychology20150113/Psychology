package com.dcy.psychology;

import com.dcy.psychology.util.FlowerGameUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class FlowerGameChooseActivity extends Activity implements
		OnClickListener {
	private Spinner mTimeLevel;
	private Spinner mColorLevel;

	private Resources mResources;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mResources = getResources();
		setContentView(R.layout.game_flower_chooselevel);
		initView();
		findViewById(R.id.flower_choose_start).setOnClickListener(this);
	}

	private void initView() {
		mTimeLevel = (Spinner) findViewById(R.id.time_level_sp);
		mColorLevel = (Spinner) findViewById(R.id.color_level_sp);
		ArrayAdapter<String> mLevelAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				mResources.getStringArray(R.array.level_choose));
		mTimeLevel.setAdapter(mLevelAdapter);
		ArrayAdapter<String> mColorLevelAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1,
				mResources.getStringArray(R.array.color_level_choose));
		mColorLevel.setAdapter(mColorLevelAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.flower_choose_start:
			Intent mIntent = new Intent(FlowerGameChooseActivity.this,
					FlowerGameActivity.class);
			mIntent.putExtra(FlowerGameUtils.TimeLevel,
					mTimeLevel.getSelectedItemPosition());
			mIntent.putExtra(FlowerGameUtils.ColorLevel,
					mColorLevel.getSelectedItemPosition());
			startActivity(mIntent);
			finish();
			break;

		default:
			break;
		}
	}
}
