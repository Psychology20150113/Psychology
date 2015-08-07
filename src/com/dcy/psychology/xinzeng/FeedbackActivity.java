package com.dcy.psychology.xinzeng;

import android.os.Bundle;
import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.R;

public class FeedbackActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback_layout);
		setTopTitle(R.string.feedback);
	}
		
}