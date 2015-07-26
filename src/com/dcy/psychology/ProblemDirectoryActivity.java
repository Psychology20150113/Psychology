package com.dcy.psychology;

import com.dcy.psychology.R;
import com.dcy.psychology.util.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ProblemDirectoryActivity extends BaseActivity implements OnClickListener{
	private int problemIndex;
	private String[] problemNameArray;
	private String[] problemIntroduceArray;
	
	private TextView introduceNameTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_problem_layout);
		problemNameArray = mResources.getStringArray(R.array.problem_array);
		problemIntroduceArray = mResources.getStringArray(R.array.problem_introduce_array);
		problemIndex = getIntent().getIntExtra("problem_index", 0);
		initView();
	}
	
	private void initView(){
		setTopTitle(problemNameArray[problemIndex]);
		introduceNameTv = (TextView) findViewById(R.id.problem_introduce_one_tv);
		introduceNameTv.setOnClickListener(this);
		findViewById(R.id.problem_introduce_two_tv).setOnClickListener(this);
		findViewById(R.id.problem_introduce_three_tv).setOnClickListener(this);
		findViewById(R.id.problem_introduce_four_tv).setOnClickListener(this);
		introduceNameTv.setText(problemIntroduceArray[problemIndex]);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.problem_introduce_one_tv:
			
			break;
		case R.id.problem_introduce_two_tv:
			Intent mIntent = new Intent(ProblemDirectoryActivity.this, ShowListActivity.class);
			mIntent.putExtra(Constants.ThemeTitle, getString(R.string.problem_introduce_two));
			mIntent.putExtra(Constants.ThemeIndex, 0);
			mIntent.putExtra(Constants.ListType, Constants.SpecialTestType);
			startActivity(mIntent);
			break;
		case R.id.problem_introduce_three_tv:
			Intent growIntent = new Intent(this, GrowLevelChooseActivity.class);
			growIntent.putExtra(Constants.ThemeIndex, 0);
			growIntent.putExtra(Constants.IsSpecial, true);
			startActivity(growIntent);
			break;
		case R.id.problem_introduce_four_tv:
			
			break;
		default:
			break;
		}
	}
}
