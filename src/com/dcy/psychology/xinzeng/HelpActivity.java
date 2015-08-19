package com.dcy.psychology.xinzeng;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.R;

public class HelpActivity extends BaseActivity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_layout);
		setTopTitle(R.string.help);
		findViewById(R.id.ll_about).setOnClickListener(this);
		findViewById(R.id.ll_howuse).setOnClickListener(this);
		findViewById(R.id.ll_relation).setOnClickListener(this);
		findViewById(R.id.ll_feedback).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Intent mIntent = null;
		switch (v.getId()) {
		case R.id.ll_about:
			mIntent = new Intent(this, PersonalInfo_PerfectActivity.class);
			break;
		case R.id.ll_howuse:
			mIntent = new Intent(this, DoctorWelcomeActivity.class);
			break;
		case R.id.ll_relation:
			mIntent = new Intent(this, RelationActivity.class);
			break;
		case R.id.ll_feedback:
			mIntent = new Intent(this, FeedbackActivity.class);
			break;
		default:
			break;
		}
		if(mIntent != null){
			startActivity(mIntent);
		}
	}
}