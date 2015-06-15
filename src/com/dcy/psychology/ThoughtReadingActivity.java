package com.dcy.psychology;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

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
	private boolean isDNATest = false;
	
	private ProgressBar mProgressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thought_reading_layout);
		mResources = getResources();
		Intent mIntent = getIntent();
		themeTitle = mIntent.getStringExtra(ThoughtReadingUtils.ThemeTitle);
		isDNATest = mIntent.getBooleanExtra(ThoughtReadingUtils.DNATest, false);
		initView();
		gsonbean = (GrowQuestionBean) mIntent.getSerializableExtra(ThoughtReadingUtils.GrowBeanData);
		if(gsonbean != null){
			getQuestionList();
			updateQuestionList();
			if(isDNATest){
				mPageIndicator.setVisibility(View.GONE);
			} else {
				mPageIndicator.setCount(mQuestionViewList.size());
				mPageIndicator.updateIndicator(mCurrentPage);
			}
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
		mProgressBar = (ProgressBar) findViewById(R.id.pb_progress);
		if(isDNATest){
			findViewById(R.id.ll_progress).setVisibility(View.VISIBLE);
			findViewById(R.id.ll_opration).setVisibility(View.GONE);
			mViewPager.setOnTouchListener(mTouchListener);
		}
	}

	private void updateQuestionList() {
		mQuestionViewList.removeAll(mQuestionViewList);
		for (int i = 0 ; i < mQuestionModelList.size() ; i ++) {
			QuestionModel model = mQuestionModelList.get(i);
			QuestionView view = new QuestionView(this);
			if(isDNATest){
				view.setIsDna();
				view.setOnCheckedListener(mItemCheckedListener);
			}
			view.setQuestionType(model.getQuestionType());
			if(isThoughtReading){
				view.setDate( i + 1, model.getQuestionTitle(), model.getOptionList());
			}else {
				view.setDate( i + 1, model.getQuestionTitle(), model.getOptionList(), model.getPoint());
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
	
	private OnCheckedChangeListener mItemCheckedListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			mCurrentPage ++;
			updateView();
		}
	};
	
	private OnTouchListener mTouchListener = new OnTouchListener() {
		private float xPos;
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				xPos = event.getX();
				break;
			case MotionEvent.ACTION_MOVE:
				if(event.getX() < xPos && !mQuestionViewList.get(mCurrentPage).hasChooseAnswer()){
					return true;
				}
				break;
			default:
				break;
			}
			return false;
		}
	};
	
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
		if(isDNATest){
			mProgressBar.setProgress(mCurrentPage * 100 / 60);
		} else {
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
					if(isDNATest){
						ArrayList<Integer> pointList = new ArrayList<Integer>();
						for(QuestionView item : mQuestionViewList){
							pointList.add(item.getPoint());
						}
						Intent resultIntent = new Intent();
						resultIntent.putIntegerArrayListExtra(ThoughtReadingUtils.PointResult, pointList);
						setResult(100, resultIntent);
						finish();
						return;
					}
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
			if(isDNATest){
				QuestionView currentView = mQuestionViewList.get(mCurrentPage);
				if(!currentView.hasChooseAnswer()){
					Toast.makeText(this, R.string.please_choose, Toast.LENGTH_SHORT).show();
					return;
				}
			}
			mCurrentPage++;
			updateView();
			break;
		default:
			break;
		}
	}
}
