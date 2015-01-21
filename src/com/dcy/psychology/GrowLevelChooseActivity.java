package com.dcy.psychology;

import java.util.ArrayList;

import com.dcy.psychology.gsonbean.GrowModelBean;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.GrowLevelView;
import com.dcy.psychology.view.GrowLevelView.OnLevelClickListener;
import com.google.gson.reflect.TypeToken;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class GrowLevelChooseActivity extends BaseActivity implements OnClickListener{
	private GrowLevelView mLevelView;
	private ArrayList<GrowModelBean> mModelList;
	
	private int themeIndex;
	private boolean isSpecial = false;
	private int mLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grow_level_choose_layout);
		setTopTitle(R.string.student_grow_one);
		setRightText(R.string.discussion);
		themeIndex = getIntent().getIntExtra(Constants.ThemeIndex, 0);
		isSpecial = getIntent().getBooleanExtra(Constants.IsSpecial, false);
		ArrayList<ArrayList<GrowModelBean>> jsonList = MyApplication.mGson.fromJson(Utils.loadRawString(this, isSpecial ? 
				R.raw.more_grow_train_lib : R.raw.grow_train_lib), 
				new TypeToken<ArrayList<ArrayList<GrowModelBean>>>(){}.getType());
		mModelList = jsonList.get(themeIndex);
		initView();
	}
	
	private void initView(){
		mLevelView = (GrowLevelView) findViewById(R.id.level_view);
		mLevelView.setInfo(mModelList.size() , themeIndex , isSpecial);
		mLevelView.setLevelClickListener(mLevelClickListener);
	}

	@Override
	public void onRightTextClick() {
		Intent mIntent = new Intent(this, DiscussListActivity.class);
		mIntent.putExtra("themeIndex", themeIndex);
		startActivity(mIntent);
	}
	
	private OnLevelClickListener mLevelClickListener = new OnLevelClickListener() {
		@Override
		public void onLevelClick(int level) {
			mLevel = level;
			GrowModelBean itemBean = mModelList.get(level);
			setTopTitle(itemBean.getTitle());
			Builder builder = new Builder(GrowLevelChooseActivity.this);
			builder.setMessage(itemBean.getComment());
			builder.setPositiveButton(R.string.start, mStartListener).show();
		}
	};
	
	private DialogInterface.OnClickListener mStartListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			GrowModelBean item = mModelList.get(mLevel);
			if(GrowModelBean.Type_Link.equals(item.getType())){
				InfoShared mShared = new InfoShared(GrowLevelChooseActivity.this);
				String key = String.format(mShared.ThemeFormat, String.valueOf(isSpecial) , themeIndex);
				if(mLevel == mShared.getInt(key)){
					mShared.putInt(key, mLevel + 1);
					mLevelView.addLevel(mModelList.size());
				}
				Intent mIntent = new Intent(GrowLevelChooseActivity.this, ShowListActivity.class);
				mIntent.putExtra(Constants.ThemeTitle, getString(isSpecial ?
						R.string.problem_introduce_two : R.string.student_grow_test));
				mIntent.putExtra(Constants.ThemeIndex, themeIndex);
				mIntent.putExtra(Constants.ListType, isSpecial ? Constants.SpecialTestType : Constants.TestType);
				startActivity(mIntent);
			}else {
				Intent mIntent = new Intent(GrowLevelChooseActivity.this, GrowDetailActivity.class);
				mIntent.putExtra(Constants.GrowModelBean, item);
				mIntent.putExtra(Constants.Level, mLevel);
				mIntent.putExtra(Constants.IsSpecial, isSpecial);
				mIntent.putExtra(Constants.ThemeIndex, themeIndex);
				startActivityForResult(mIntent , 0);
			}
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 1:
			if(data == null)
				return;
			mLevelView.addLevel(mModelList.size());
			break;

		default:
			break;
		}
	};
	
	public void onClick(View view) {
		switch (view.getId()) {
		default:
			break;
		}
	};
}
