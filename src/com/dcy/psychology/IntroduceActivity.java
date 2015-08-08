package com.dcy.psychology;

import com.dcy.psychology.R;
import com.dcy.psychology.adapter.IntroducePageAdapter;
import com.dcy.psychology.view.PageIndicatorView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class IntroduceActivity extends Activity implements OnClickListener{
	private TextView mGoView;
	private PageIndicatorView indicatorView;
	private int mCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_introduce_layout);
		initView();
	}

	private void initView() {
		ViewPager pager = (ViewPager) findViewById(R.id.introduce_vp);
		IntroducePageAdapter mAdapter = new IntroducePageAdapter(this);
		pager.setAdapter(mAdapter);
		pager.setOnPageChangeListener(mPageListener);
		indicatorView = (PageIndicatorView) findViewById(R.id.page_indicator);
		mCount = mAdapter.getCount();
		indicatorView.setCount(mCount);
		indicatorView.updateIndicator(0);
		mGoView = (TextView) findViewById(R.id.go_btn);
		mGoView.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}
	
	private OnPageChangeListener mPageListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int position) {
			indicatorView.updateIndicator(position);
			if(position == mCount - 1){
				mGoView.setVisibility(View.VISIBLE);
			} else {
				mGoView.setVisibility(View.GONE);
			}
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
}
