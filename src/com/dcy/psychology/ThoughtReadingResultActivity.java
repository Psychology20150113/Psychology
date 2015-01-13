package com.dcy.psychology;

import java.util.ArrayList;
import java.util.HashMap;

import com.dcy.psychology.model.QuestionModel;
import com.dcy.psychology.util.ThoughtReadingUtils;
import com.dcy.psychology.util.ThoughtReadingUtils.QuestionResultAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ThoughtReadingResultActivity extends Activity implements OnClickListener{
	private ArrayList<HashMap<String, String>> mAnswerList;
	private ArrayList<QuestionModel> mQuestionModels;
	
	private ListView mResultView;
	private TextView mRightCountText;
	
	private int themeIndex;
	private String themeName;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qustion_result_layout);
		setTitle(R.string.question_statistics);
		Intent mIntent = getIntent();
		mAnswerList = (ArrayList<HashMap<String, String>>) mIntent.getSerializableExtra(ThoughtReadingUtils.QuestionAnswer);
		mQuestionModels = (ArrayList<QuestionModel>) mIntent.getSerializableExtra(ThoughtReadingUtils.QuestionModelList);
		themeIndex = mIntent.getIntExtra(ThoughtReadingUtils.ThemeIndex, 0);
		themeName = mIntent.getStringExtra(ThoughtReadingUtils.ThemeTitle);
		initView();
	}
	
	private void initView(){
		mResultView = (ListView) findViewById(R.id.question_answer_lv);
		QuestionResultAdapter mResultAdapter = new QuestionResultAdapter(this, mAnswerList);
		mResultAdapter.setQuestionModels(mQuestionModels);
		mResultView.setAdapter(mResultAdapter);
		mRightCountText = (TextView) findViewById(R.id.question_right_count_tv);
		mRightCountText.setText(String.format(getString(R.string.question_right_count), getRightCount()));
		findViewById(R.id.question_end_btn).setOnClickListener(this);
		findViewById(R.id.question_replay_btn).setOnClickListener(this);
	}
	
	private int getRightCount(){
		int count = 0;
		for(HashMap<String, String> itemMap: mAnswerList){
			if(itemMap.get(ThoughtReadingUtils.RightAnswerKey).equals(itemMap.get(ThoughtReadingUtils.MineAnswerKey)))
				count++;
		}
		return count;
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.question_end_btn:
			break;
		case R.id.question_replay_btn:
			Intent mIntent = new Intent(this,ThoughtReadingActivity.class);
			mIntent.putExtra(ThoughtReadingUtils.ThemeTitle, themeName);
			mIntent.putExtra(ThoughtReadingUtils.ThemeTitle, themeIndex);
			startActivity(mIntent);
			break;
		default:
			break;
		}
		finish();
	}
}
