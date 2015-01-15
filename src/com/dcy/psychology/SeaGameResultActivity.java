package com.dcy.psychology;

import java.util.ArrayList;

import com.dcy.psychology.gsonbean.GameFishBean;
import com.dcy.psychology.view.SeaFishView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SeaGameResultActivity extends Activity {
	private ArrayList<String> rightColors;
	private ArrayList<String> mineColors;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_sea_result_layout);
		Intent mIntent = getIntent();
		rightColors = mIntent.getStringArrayListExtra("rightColors");
		mineColors = mIntent.getStringArrayListExtra("mineColors");
		GameFishBean bean = (GameFishBean) mIntent.getSerializableExtra("gamefishbean");
		((SeaFishView)findViewById(R.id.right_view)).setData(bean , rightColors);
		((SeaFishView)findViewById(R.id.mine_view)).setData(bean , mineColors);
	}
}
