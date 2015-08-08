package com.dcy.psychology.xinzeng;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.SlideMainActivity;

public class SubmitsuccessActivity extends BaseActivity implements OnClickListener{
	private TextView tvsubmit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submitsuccess_layout);
		setTopTitle(R.string.apply);
		findViewById(R.id.btn_goback).setOnClickListener(this);
		tvsubmit= (TextView)findViewById(R.id.tv_submitsuccess); 
		TextPaint tp = tvsubmit.getPaint(); 
		tp.setFakeBoldText(true);
		
	}
	
	@Override
	public void onClick(View v) {
		Intent mIntent = null;
			mIntent = new Intent(this, SlideMainActivity.class);
		
			startActivity(mIntent);
		
	}
}