package com.dcy.psychology;

import java.util.ArrayList;

import com.dcy.psychology.adapter.HomeShowAllListAdapter;
import com.dcy.psychology.gsonbean.GrowPictureBean;
import com.dcy.psychology.gsonbean.GrowQuestionBean;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.ThoughtReadingUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

public class HomePagePicActivity extends BaseActivity implements OnItemClickListener{
	private ListView mListView;
	private HomeShowAllListAdapter mAdapter;
	private ArrayList<GrowPictureBean> dataList;
	private ArrayList<GrowQuestionBean> questionList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage_test_layout);
		setTopTitle(R.string.title_pic);
		dataList = (ArrayList<GrowPictureBean>) getIntent().getSerializableExtra("growpicturelist");
		questionList = (ArrayList<GrowQuestionBean>) getIntent().getSerializableExtra("questionlist");
		mListView = (ListView) findViewById(R.id.content_lv);
		mAdapter = new HomeShowAllListAdapter(this, dataList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(position >= dataList.size()){
			Intent mIntent = new Intent(this, ThoughtReadingActivity.class);
			mIntent.putExtra(ThoughtReadingUtils.GrowBeanData, questionList.get(position - dataList.size()));
			mIntent.putExtra(ThoughtReadingUtils.ThemeTitle, Constants.HomePageTestTitle[position - dataList.size()]);
			startActivity(mIntent);
		}else {
			Intent mIntent = new Intent(this, PlamPictureDetailActivity.class);
			mIntent.putExtra(Constants.PictureBean, dataList.get(position));
			startActivity(mIntent);
		}
	}
}
