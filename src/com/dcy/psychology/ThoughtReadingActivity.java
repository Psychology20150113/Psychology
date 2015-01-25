package com.dcy.psychology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.baidu.vi.MFE;
import com.dcy.psychology.gsonbean.GrowQuestionBean;
import com.dcy.psychology.gsonbean.GrowQuestionBean.AnswerBean;
import com.dcy.psychology.gsonbean.GrowQuestionBean.OptionBean;
import com.dcy.psychology.model.QuestionModel;
import com.dcy.psychology.util.ThoughtReadingUtils;
import com.dcy.psychology.util.ThoughtReadingUtils.QuestionAdapter;
import com.dcy.psychology.util.ThoughtReadingUtils.QuestionType;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.PageIndicatorView;
import com.dcy.psychology.view.QuestionView;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ThoughtReadingActivity extends Activity implements OnClickListener{

	private Resources mResources;
	private int mCurrentPage = 0;

	private ViewPager mViewPager;
	private PageIndicatorView mPageIndicator;
	private Button mAloneButton;
	private Button mNextButton;
	private View mButtonLayout;
	private QuestionAdapter mAdapter;
	private ArrayList<QuestionView> mQuestionViewList = new ArrayList<QuestionView>();
	private ArrayList<QuestionModel> mQuestionModelList;
	
	private String themeTitle;
	private int themeIndex;
	private boolean isThoughtReading;
	
	private int growThemeIndex;
	private int growGroupIndex;
	private GrowQuestionBean gsonbean;
	
	private boolean isSpecial = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thought_reading_layout);
		mResources = getResources();
		Intent mIntent = getIntent();
		themeTitle = mIntent.getStringExtra(ThoughtReadingUtils.ThemeTitle);
		initView();
		gsonbean = (GrowQuestionBean) mIntent.getSerializableExtra(ThoughtReadingUtils.GrowBeanData);
		if(gsonbean != null){
			getQuestionList();
			updateQuestionList();
			mPageIndicator.setCount(mQuestionViewList.size());
			mPageIndicator.updateIndicator(mCurrentPage);
			isThoughtReading = false;
		}else {
			themeIndex = mIntent.getIntExtra(ThoughtReadingUtils.ThemeIndex, 0);
			isThoughtReading = mIntent.getBooleanExtra(ThoughtReadingUtils.IsThoughtReadingMode, false);
			growThemeIndex = mIntent.getIntExtra(ThoughtReadingUtils.GrowThemeIndex, 0);
			growGroupIndex = mIntent.getIntExtra(ThoughtReadingUtils.GrowGroupIndex, 0);
			isSpecial = mIntent.getBooleanExtra(ThoughtReadingUtils.IsSpecialMode, false);
			new MyJsonTask().execute();
		}
	}

	private void initView() {
		((TextView)findViewById(R.id.title_tv)).setText(themeTitle);
		mViewPager = (ViewPager) findViewById(R.id.qiestion_vp);
		mAdapter = new QuestionAdapter(mQuestionViewList);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(mPageListener);
		mPageIndicator = (PageIndicatorView) findViewById(R.id.page_indicator);
		mAloneButton = (Button) findViewById(R.id.button_alone);
		findViewById(R.id.button_left).setOnClickListener(this);
		mNextButton = (Button) findViewById(R.id.button_right);
		mButtonLayout = findViewById(R.id.button_layout);
		mAloneButton.setOnClickListener(this);
		mNextButton.setOnClickListener(this);
	}

	private void updateQuestionList() {
		mQuestionViewList.removeAll(mQuestionViewList);
		for (QuestionModel model : mQuestionModelList) {
			QuestionView view = new QuestionView(this);
			view.setQuestionType(model.getQuestionType());
			if(isThoughtReading){
				view.setDate(model.getQuestionTitle(), model.getOptionList());
			}else {
				view.setDate(model.getQuestionTitle(), model.getOptionList(), model.getPoint());
			}
			mQuestionViewList.add(view);
		}
		mAdapter.notifyDataSetChanged();
	}

	private class MyJsonTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			//mQuestionModelList = ThoughtReadingUtils.getRandomList(rawString);
			if(isThoughtReading){
				mQuestionModelList = ThoughtReadingUtils.getThemeList(
						Utils.loadRawString(ThoughtReadingActivity.this , R.raw.questionlibrary), themeIndex);
			}else {
				ArrayList<ArrayList<GrowQuestionBean>> allList = MyApplication.mGson.fromJson(
						Utils.loadRawString(ThoughtReadingActivity.this, isSpecial ? R.raw.more_grow_questionlib : R.raw.growquestionlib), 
						new TypeToken<ArrayList<ArrayList<GrowQuestionBean>>>(){}.getType());
				gsonbean = allList.get(growThemeIndex).get(growGroupIndex);
				getQuestionList();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			updateQuestionList();
			mPageIndicator.setCount(mQuestionViewList.size());
			mPageIndicator.updateIndicator(mCurrentPage);
		}
	}
	
	private void getQuestionList() {
		mQuestionModelList = new ArrayList<QuestionModel>();
		for(OptionBean optionItem : gsonbean.getQuestion()){
			QuestionModel item = new QuestionModel();
			item.setQuestionId(optionItem.getId());
			item.setQuestionType(QuestionType.Type_Grow);
			item.setQuestionTitle(optionItem.getTitle());
			item.setOptionList(optionItem.getOption());
			item.setPoint(optionItem.getPoint());
			mQuestionModelList.add(item);
		}
	}
	
	private ArrayList<HashMap<String, String>> getAnswerMap(){
		ArrayList<HashMap<String, String>> mAnswerMap = new ArrayList<HashMap<String,String>>();
		for(int i = 0 ; i < mQuestionViewList.size() ; i++){
			HashMap<String, String> itemMap = new HashMap<String, String>();
			String mineAnswer = mQuestionViewList.get(i).getAnswerString();
			if(TextUtils.isEmpty(mineAnswer))
				mineAnswer = getString(R.string.no_answer);
			itemMap.put(ThoughtReadingUtils.MineAnswerKey, mineAnswer.replace(" ", ","));
			itemMap.put(ThoughtReadingUtils.RightAnswerKey, mQuestionModelList.get(i).getAnswer().trim());
			mAnswerMap.add(itemMap);
		}
		return mAnswerMap;
	}
	
	private OnPageChangeListener mPageListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int postion) {
			mCurrentPage = postion;
			updateView();
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	
	private void updateView(){
		mViewPager.setCurrentItem(mCurrentPage);
		mPageIndicator.updateIndicator(mCurrentPage);
		if(mCurrentPage == 0){
			mAloneButton.setVisibility(View.VISIBLE);
			mButtonLayout.setVisibility(View.GONE);
		}else if(mCurrentPage == mQuestionViewList.size() - 1){
			mAloneButton.setVisibility(View.GONE);
			mButtonLayout.setVisibility(View.VISIBLE);
			mNextButton.setText("Íê³É");
		}else {
			mAloneButton.setVisibility(View.GONE);
			mButtonLayout.setVisibility(View.VISIBLE);
			mNextButton.setText(R.string.next_question);
		}
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button_left:
			mCurrentPage--;
			updateView();
			break;
		case R.id.button_right:
			if(mCurrentPage == mQuestionViewList.size() - 1){
				if(isThoughtReading){
					Intent mIntent = new Intent(this, ThoughtReadingResultActivity.class);
					mIntent.putExtra(ThoughtReadingUtils.QuestionAnswer, getAnswerMap());
					mIntent.putExtra(ThoughtReadingUtils.QuestionModelList, mQuestionModelList);
					mIntent.putExtra(ThoughtReadingUtils.ThemeTitle, themeTitle);
					mIntent.putExtra(ThoughtReadingUtils.ThemeIndex, themeIndex);
					startActivity(mIntent);
					finish();
					return;
				}else {
					if(gsonbean.getAnswer() == null){
						return;
					}
					int pointTotal = 0;
					for(QuestionView item : mQuestionViewList){
						pointTotal += item.getPoint();
					}
					for(AnswerBean answerItem : gsonbean.getAnswer()){
						if(pointTotal >= answerItem.getStartPoint() && pointTotal <= answerItem.getEndPoint()){
							Builder builder = new Builder(this);
							builder.setTitle(R.string.test_evaluate).
							setMessage(answerItem.getComment()).
							setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									finish();
									return;
								}
							}).show();
						}
					}
				}
			}
			mCurrentPage++;
			updateView();
			break;
		case R.id.button_alone:
			mCurrentPage++;
			updateView();
			break;
		default:
			break;
		}
	}
}
