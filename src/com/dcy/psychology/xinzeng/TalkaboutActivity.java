package com.dcy.psychology.xinzeng;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.R;

public class TalkaboutActivity extends BaseActivity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_talkabout_layout);
		setTopTitle(R.string.apply);
		findViewById(R.id.btn_submit).setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		Intent mIntent = null;
			mIntent = new Intent(this, SubmitsuccessActivity.class);
		
			startActivity(mIntent);
		
	}
}