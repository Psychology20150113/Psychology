package com.dcy.psychology.xinzeng;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.R;

public class help extends BaseActivity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_layout);
		setTopTitle(R.string.help);
		findViewById(R.id.btn_about).setOnClickListener(this);
		findViewById(R.id.btn_howuse).setOnClickListener(this);
		findViewById(R.id.btn_relation).setOnClickListener(this);
		findViewById(R.id.btn_feedback).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Intent mIntent = null;
		switch (v.getId()) {
		case R.id.btn_about:
			mIntent = new Intent(this, about.class);
			break;
		case R.id.btn_howuse:
			mIntent = new Intent(this, howuse.class);
			break;
		case R.id.btn_relation:
			mIntent = new Intent(this, relation.class);
			break;
		case R.id.btn_feedback:
			mIntent = new Intent(this, feedback.class);
			break;
		default:
			break;
		}
		if(mIntent != null){
			startActivity(mIntent);
		}
	}
}