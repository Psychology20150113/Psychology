package com.dcy.psychology;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.adapter.GameSeaLevelChooseAdapter;
import com.dcy.psychology.gsonbean.GameFishBean;
import com.dcy.psychology.gsonbean.GameFishBean.Fish;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.SeaChooseLayout;
import com.dcy.psychology.view.SeaFishView;
import com.dcy.psychology.view.dialog.SimpleMessageDialog;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SeaGameActivity extends Activity implements OnItemClickListener
		,OnClickListener{
	private final int ReadTime = 7;
	
	private SeaFishView mFishView;
	private SeaChooseLayout mChooseLayout;
	private int mCountDownTime = ReadTime;
	private int mPrepareCountDownTime = 3;
	private GameFishBean bean;
	private ArrayList<GameFishBean> allList;
	private ImageView mGameRuleView;
	private ImageView mCountDownView;
	private ImageView mCompleteView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_sea_layout);
		allList = MyApplication.mGson.fromJson(Utils.loadRawString(this, 
				R.raw.game_sea_info), new TypeToken<ArrayList<GameFishBean>>(){}.getType());
		initView();
	}

	private void initView() {
		mFishView = (SeaFishView) findViewById(R.id.fish_view);
		mCountDownView = (ImageView)findViewById(R.id.countdown_iv);
		mChooseLayout = (SeaChooseLayout) findViewById(R.id.choose_view);
		mChooseLayout.setColorChooseListener(mListener);
		GridView levelGridView = (GridView) findViewById(R.id.level_gv);
		levelGridView.setAdapter(new GameSeaLevelChooseAdapter(this));
		levelGridView.setOnItemClickListener(this);
		mGameRuleView = (ImageView) findViewById(R.id.game_rule_iv);
		mGameRuleView.setOnClickListener(this);
		mCompleteView = (ImageView) findViewById(R.id.complete_iv);
		mCompleteView.setOnClickListener(this);
	}
	
	private int[] countdownRes = {R.drawable.icon_countdown_01, R.drawable.icon_countdown_02,
			R.drawable.icon_countdown_03};
	private Handler mCountDownHandler = new Handler();
	private Runnable mCountDownTask = new Runnable() {
		@Override
		public void run() {
			if(mPrepareCountDownTime >= 0){
				if(mPrepareCountDownTime == 0){
					mCountDownView.setImageResource(R.drawable.icon_start);
				}else {
					mCountDownView.setImageResource(countdownRes[mPrepareCountDownTime - 1]);
				}
				mCountDownHandler.postDelayed(mCountDownTask, 1000);
				mPrepareCountDownTime--;	
			}else {
				if(mCountDownTime == ReadTime){
					mFishView.setData(bean);
					mChooseLayout.setData(bean.getColors());
				}
				if(mCountDownTime >=0){
					if(mCountDownTime == 0){
						mCountDownView.setImageResource(R.drawable.icon_go);
					}else if(mCountDownTime <= 3){
						mCountDownView.setImageResource(countdownRes[mCountDownTime - 1]);
						mCountDownView.setVisibility(View.VISIBLE);
					}else {
						mCountDownView.setVisibility(View.GONE);
					}
					mCountDownHandler.postDelayed(mCountDownTask, 1000);
					mCountDownTime--;
				}else {
					mCountDownView.setVisibility(View.GONE);
					mCompleteView.setVisibility(View.VISIBLE);
					mFishView.changeMode();
				} 
			}
		}
	};
	
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
	
	public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
		mGameRuleView.setVisibility(View.GONE);
		parent.setVisibility(View.GONE);
		bean = allList.get(position);
		mCountDownHandler.post(mCountDownTask);
	};

	private void turnToResult(){
		Intent mIntent = new Intent(this , SeaGameResultActivity.class);
		mIntent.putStringArrayListExtra("rightColors", mFishView.getRightColors());
		mIntent.putStringArrayListExtra("mineColors", mFishView.getMineColors());
		mIntent.putExtra("gamefishbean", bean);
		startActivity(mIntent);
		finish();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.complete_iv:
			turnToResult();
			break;
		case R.id.game_rule_iv:
			new SimpleMessageDialog(this, getString(R.string.game_rule), getString(R.string.game_rule_detail)).show();
			break;
		default:
			break;
		}
	}
}
