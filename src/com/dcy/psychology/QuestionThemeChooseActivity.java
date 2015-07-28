package com.dcy.psychology;

import com.dcy.psychology.R;
import com.dcy.psychology.util.ThoughtReadingUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class QuestionThemeChooseActivity extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.question_choose_layout);
		findViewById(R.id.theme_one_btn).setOnClickListener(this);
		findViewById(R.id.theme_two_btn).setOnClickListener(this);
		findViewById(R.id.theme_three_btn).setOnClickListener(this);
		findViewById(R.id.theme_four_btn).setOnClickListener(this);
		findViewById(R.id.theme_five_btn).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		Intent mIntent = new Intent(QuestionThemeChooseActivity.this, ThoughtReadingActivity.class);
		mIntent.putExtra(ThoughtReadingUtils.ThemeTitle, ((Button)view).getText());
		mIntent.putExtra(ThoughtReadingUtils.IsThoughtReadingMode, true);
		switch (view.getId()) {
		case R.id.theme_one_btn:
			mIntent.putExtra(ThoughtReadingUtils.ThemeIndex, 0);
			break;
		case R.id.theme_two_btn:
			mIntent.putExtra(ThoughtReadingUtils.ThemeIndex, 1);
			break;
		case R.id.theme_three_btn:
			mIntent.putExtra(ThoughtReadingUtils.ThemeIndex, 2);
			break;
		case R.id.theme_four_btn:
			mIntent.putExtra(ThoughtReadingUtils.ThemeIndex, 9);
			break;
		case R.id.theme_five_btn:
			mIntent.putExtra(ThoughtReadingUtils.ThemeIndex, 10);
			break;
		default:
			break;
		}
		startActivity(mIntent);
	}
}
