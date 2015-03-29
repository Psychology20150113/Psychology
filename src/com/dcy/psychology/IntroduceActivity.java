package com.dcy.psychology;

import com.dcy.psychology.adapter.IntroducePageAdapter;
import com.dcy.psychology.view.PageIndicatorView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class IntroduceActivity extends Activity implements OnClickListener{
	private Button mButton;
	private PageIndicatorView indicatorView;
	private final int Count = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_introduce_layout);
		initView();
	}

	private void initView() {
		ViewPager pager = (ViewPager) findViewById(R.id.introduce_vp);
		pager.setAdapter(new IntroducePageAdapter(this));
		pager.setOnPageChangeListener(mPageListener);
		indicatorView = (PageIndicatorView) findViewById(R.id.page_indicator);
		indicatorView.setCount(Count);
		indicatorView.updateIndicator(0);
		mButton = (Button) findViewById(R.id.go_btn);
		mButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		startActivity(new Intent(this, SlideMainActivity.class));
		finish();
	}
	
	private OnPageChangeListener mPageListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int position) {
			indicatorView.updateIndicator(position);
			if(position == Count - 1){
				mButton.setVisibility(View.VISIBLE);
			} else {
				mButton.setVisibility(View.GONE);
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
