package com.dcy.psychology;

import com.dcy.psychology.adapter.StudentGrowPlatformAdapter;
import com.dcy.psychology.adapter.StudentGrowPlatformAdapter.OnPlatClickListener;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.ThoughtReadingUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class StudentGrowActivity extends BaseActivity implements OnClickListener{
	private ListView mPlatformLV;
	private StudentGrowPlatformAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_grow_layout);
		initView();
	}
	
	private void initView(){
		setTopTitle(R.string.platform_one);
		mPlatformLV = (ListView) findViewById(R.id.platform_lv);
		mAdapter = new StudentGrowPlatformAdapter(this, mPlatformLV);
		mPlatformLV.setAdapter(mAdapter);
		mAdapter.setOnPlatClickListener(mPlatClickListener);
	}
	
	private OnPlatClickListener mPlatClickListener = new OnPlatClickListener() {
		
		@Override
		public void itemClick(int themeId, int itemId) {
			switch (itemId) {
			case 0:
				Intent growIntent = new Intent(StudentGrowActivity.this, GrowLevelChooseActivity.class);
				growIntent.putExtra(Constants.ThemeIndex, themeId);
				startActivity(growIntent);
				break;
			case 1:
				Intent mIntent = new Intent(StudentGrowActivity.this, ShowListActivity.class);
				mIntent.putExtra(Constants.ThemeTitle, getString(R.string.student_grow_test));
				mIntent.putExtra(Constants.ThemeIndex, themeId);
				mIntent.putExtra(Constants.ListType, Constants.TestType);
				startActivity(mIntent);
				break;
			case 2:
				Intent picIntent = new Intent(StudentGrowActivity.this , ShowListActivity.class);
				picIntent.putExtra(Constants.ThemeTitle, getString(R.string.student_grow_pic));
				picIntent.putExtra(Constants.ThemeIndex, themeId);
				picIntent.putExtra(Constants.ListType, Constants.PicType);
				startActivity(picIntent);
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	public void onClick(View view) {
	}
}
