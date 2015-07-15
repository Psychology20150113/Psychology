package com.dcy.psychology.xinzeng;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.R;

public class about extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		setTopTitle(R.string.about);
	}
		
}