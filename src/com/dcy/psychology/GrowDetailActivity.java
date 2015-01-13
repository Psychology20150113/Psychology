package com.dcy.psychology;

import com.dcy.psychology.gsonbean.GrowModelBean;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.view.GrowDetailView;

import android.content.Intent;
import android.os.Bundle;

public class GrowDetailActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grow_detail_layout);
		GrowModelBean bean = (GrowModelBean) getIntent().getSerializableExtra(Constants.GrowModelBean);
		setTopTitle(bean.getTitle());
		Intent mIntent = getIntent();
		GrowDetailView detailView = (GrowDetailView) findViewById(R.id.detail_view);
		detailView.saveCompeLevel(mIntent.getBooleanExtra(Constants.IsSpecial, false), 
				mIntent.getIntExtra(Constants.ThemeIndex, 0), mIntent.getIntExtra(Constants.Level, 0));
		detailView.setContent(bean);
	}
}
