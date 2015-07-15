package com.dcy.psychology.xinzeng;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.SlideMainActivity;

public class submitsuccess extends BaseActivity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submitsuccess);
		setTopTitle(R.string.apply);
		findViewById(R.id.goback).setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		Intent mIntent = null;
			mIntent = new Intent(this, SlideMainActivity.class);
		
			startActivity(mIntent);
		
	}
}