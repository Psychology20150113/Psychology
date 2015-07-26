package com.dcy.psychology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dcy.psychology.R;
import com.dcy.psychology.adapter.HomeShowAllListAdapter;
import com.dcy.psychology.gsonbean.GrowQuestionBean;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.ThoughtReadingUtils;
import com.dcy.psychology.util.Utils;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

public class AllTestActivity extends BaseActivity implements OnItemClickListener{
	private ListView mListView;
	private HomeShowAllListAdapter mAdapter;
	private ArrayList<GrowQuestionBean> questionList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage_test_layout);
		setTopTitle(R.string.title_test);
		questionList = MyApplication.mGson.fromJson(Utils.loadRawString(AllTestActivity.this, 
				R.raw.homepage_growquestionlib), new TypeToken<ArrayList<GrowQuestionBean>>(){}.getType());
		mListView = (ListView) findViewById(R.id.content_lv);
		mAdapter = new HomeShowAllListAdapter(this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int reversePos = questionList.size() - 1 - position;
		Intent mIntent = new Intent(this, ThoughtReadingActivity.class);
		mIntent.putExtra(ThoughtReadingUtils.GrowBeanData, questionList.get(reversePos));
		mIntent.putExtra(ThoughtReadingUtils.ThemeTitle, Constants.HomePageTestTitle[reversePos]);
		startActivity(mIntent);
		Map<String, String> questionMap = new HashMap<String, String>();
		questionMap.put("name", Constants.HomePageTestTitle[position]);
		MobclickAgent.onEvent(this, "test_without_theme", questionMap);
	}
}
