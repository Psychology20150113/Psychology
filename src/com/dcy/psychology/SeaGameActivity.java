package com.dcy.psychology;

import java.util.ArrayList;

import com.dcy.psychology.gsonbean.GameFishBean;
import com.dcy.psychology.gsonbean.GameFishBean.Fish;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.SeaChooseLayout;
import com.dcy.psychology.view.SeaFishView;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SeaGameActivity extends Activity {
	private SeaFishView mFishView;
	private SeaChooseLayout mChooseLayout;
	private int mCountDownTime = 3;
	private int mOprationTime = 3;
	private GameFishBean bean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_sea_layout);
		mFishView = (SeaFishView) findViewById(R.id.fish_view);
		mChooseLayout = (SeaChooseLayout) findViewById(R.id.choose_view);
		ArrayList<GameFishBean> list = MyApplication.mGson.fromJson(Utils.loadRawString(this, R.raw.game_sea_info), new TypeToken<ArrayList<GameFishBean>>(){}.getType());
		bean = list.get(0);
		mFishView.setData(bean);
		mChooseLayout.setData(bean.getColors());
		mChooseLayout.setColorChooseListener(mListener);
		mCountDownHandler.post(mCountDownTask);
	}
	
	private Handler mCountDownHandler = new Handler();
	private Runnable mCountDownTask = new Runnable() {
		@Override
		public void run() {
			if(mCountDownTime >= 0){
				mCountDownHandler.postDelayed(mCountDownTask, 1000);
				if(mCountDownTime == 0 )
					mFishView.changeMode();
				mCountDownTime--;	
			}else {
				if(mOprationTime >=0){
					mCountDownHandler.postDelayed(mCountDownTask, 1000);
					mOprationTime--;
				}else {
					turnToResult();
				} 
			}
		}
	};
	
	private void turnToResult(){
		Intent mIntent = new Intent(this , SeaGameResultActivity.class);
		mIntent.putStringArrayListExtra("rightColors", mFishView.getRightColors());
		mIntent.putStringArrayListExtra("mineColors", mFishView.getMineColors());
		mIntent.putExtra("gamefishbean", bean);
		startActivity(mIntent);
		finish();
	}
	
	private OnClickListener mListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String color = (String) v.getTag();
			View mSelectView = mFishView.getSelectView();
			if(mSelectView == null)
				return;
			mSelectView.setBackgroundColor(Color.parseColor(color));
			mSelectView.setTag(color);
		}
	};
}
