package com.dcy.psychology;

import com.dcy.psychology.gsonbean.GrowModelBean;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.view.GrowDetailView;

import android.content.Intent;
import android.os.Bundle;

public class GrowDetailActivity extends BaseActivity {
	private GrowModelBean bean;
	private int themeIndex;
	private int level;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grow_detail_layout);
		Intent mIntent = getIntent();
		bean = (GrowModelBean) mIntent.getSerializableExtra(Constants.GrowModelBean);
		themeIndex = mIntent.getIntExtra(Constants.ThemeIndex, 0);
		level = mIntent.getIntExtra(Constants.Level, 0);
		setTopTitle(bean.getTitle());
		setRightView(R.drawable.icon_history);
		GrowDetailView detailView = (GrowDetailView) findViewById(R.id.detail_view);
		detailView.saveCompeLevel(mIntent.getBooleanExtra(Constants.IsSpecial, false), 
				themeIndex, level);
		detailView.setContent(bean);
	}
	
	@Override
	public void onRightViewClick() {
		Intent mIntent = new Intent(this, GrowHistoryActivity.class);
		mIntent.putExtra(Constants.GrowModelBean, bean);
		mIntent.putExtra(Constants.ThemeIndex, themeIndex);
		mIntent.putExtra(Constants.Level, level);
		startActivity(mIntent);
	}
}
