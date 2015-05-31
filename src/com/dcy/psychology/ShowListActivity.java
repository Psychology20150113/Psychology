package com.dcy.psychology;

import java.util.ArrayList;
import java.util.HashMap;

import com.dcy.psychology.adapter.ContentListAdapter;
import com.dcy.psychology.gsonbean.GrowPictureBean;
import com.dcy.psychology.gsonbean.GrowQuestionBean;
import com.dcy.psychology.util.CalculateUtils;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.ThoughtReadingUtils;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.dialog.SimpleMessageDialog;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

public class ShowListActivity extends BaseActivity implements OnItemClickListener{
	private ListView mListView;
	private ContentListAdapter mAdapter;
	private ArrayList<GrowPictureBean> mDateBeanList = new ArrayList<GrowPictureBean>();
	
	private int themeIndex = 0;
	private String type;
	private final int RequestCode_Test_Hollend = 100;
	private final int RequestCode_Test_Qizhi = 101;
	private InfoShared mShared;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_layout);
		themeIndex = getIntent().getIntExtra(Constants.ThemeIndex, 0);
		setTopTitle(getIntent().getStringExtra(Constants.ThemeTitle));
		type = getIntent().getStringExtra(Constants.ListType);
		initView();
		if(Constants.PicType.equals(type)){
			ArrayList<ArrayList<GrowPictureBean>> result = MyApplication.mGson.fromJson(Utils.loadRawString(this, R.raw.picture_text_lib), 
					new TypeToken<ArrayList<ArrayList<GrowPictureBean>>>(){}.getType());
			mDateBeanList.addAll(result.get(themeIndex));
			mAdapter.notifyDataSetChanged();
		}
		mShared = new InfoShared(this);
	}
	
	private void initView(){
		mListView = (ListView) findViewById(R.id.list_lv);
		if(Constants.PicType.equals(type)){
			mAdapter = new ContentListAdapter(this, mDateBeanList);
		}else if(Constants.TestType.equals(type)){
			mAdapter = new ContentListAdapter(this, Constants.GrowTestTitle[themeIndex]);
		}else if(Constants.SpecialTestType.equals(type)){
			mAdapter = new ContentListAdapter(this, Constants.SpecialGrowTestTitle[themeIndex]);
		}else if(Constants.DNAType.equals(type)){
			setTopTitle(getString(R.string.mine_DNA));
			mAdapter = new ContentListAdapter(this, mResources.getStringArray(R.array.dna_array));
		}
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1,final int position, long arg3) {
		if(Constants.PicType.equals(type)){
			Intent mIntent = new Intent(this, PlamPictureDetailActivity.class);
			mIntent.putExtra(Constants.PictureBean, mDateBeanList.get(position));
			startActivity(mIntent);
		}else if(Constants.TestType.equals(type)){
			Intent mIntent = new Intent(this, ThoughtReadingActivity.class);
			mIntent.putExtra(ThoughtReadingUtils.IsThoughtReadingMode, false);
			mIntent.putExtra(ThoughtReadingUtils.ThemeTitle, Constants.GrowTestTitle[themeIndex][position]);
			mIntent.putExtra(ThoughtReadingUtils.GrowGroupIndex, position);
			mIntent.putExtra(ThoughtReadingUtils.GrowThemeIndex, themeIndex);
			startActivity(mIntent);
		}else if(Constants.SpecialTestType.equals(type)){
			Intent mIntent = new Intent(this, ThoughtReadingActivity.class);
			mIntent.putExtra(ThoughtReadingUtils.IsThoughtReadingMode, false);
			mIntent.putExtra(ThoughtReadingUtils.ThemeTitle, Constants.SpecialGrowTestTitle[themeIndex][position]);
			mIntent.putExtra(ThoughtReadingUtils.IsSpecialMode, true);
			mIntent.putExtra(ThoughtReadingUtils.GrowGroupIndex, position);
			mIntent.putExtra(ThoughtReadingUtils.GrowThemeIndex, themeIndex);
			startActivity(mIntent);
		}else if(Constants.DNAType.equals(type)){
			if(!TextUtils.isEmpty(position == 0 ? mShared.getHollendResult() : mShared.getQizhiResult())){
				SimpleMessageDialog dialog = new SimpleMessageDialog(this, getString(position == 0 ? R.string.Test_Hollend : R.string.Test_Qizhi), 
						position == 0 ? mShared.getHollendResult() : mShared.getQizhiResult(), getString(R.string.ok), getString(R.string.Test_retry));
				dialog.setSureClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						gotoDnaTest(position);
					}
				});
				dialog.show();
			} else {
				gotoDnaTest(position);
			}
		}
	}

	private void gotoDnaTest(int position) {
		GrowQuestionBean beanList = MyApplication.mGson.fromJson(
				Utils.loadRawString(this, position == 0 ? R.raw.test_zhiye : R.raw.test_qizhi), 
				GrowQuestionBean.class);
		Intent mIntent = new Intent(this, ThoughtReadingActivity.class);
		mIntent.putExtra(ThoughtReadingUtils.GrowBeanData, beanList);
		mIntent.putExtra(ThoughtReadingUtils.ThemeTitle, getString(position == 0 ? R.string.Test_Hollend : R.string.Test_Qizhi));
		mIntent.putExtra(ThoughtReadingUtils.DNATest, true);
		startActivityForResult(mIntent, position == 0 ? RequestCode_Test_Hollend : RequestCode_Test_Qizhi);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RequestCode_Test_Hollend:
			if(data == null){
				return;
			}
			HashMap<String, String> resultMap = CalculateUtils.calculateZhiyeResult(this, 
					data.getIntegerArrayListExtra(ThoughtReadingUtils.PointResult));
			if(resultMap == null){
				return;
			}
			new SimpleMessageDialog(this, getString(R.string.Test_Hollend), 
					resultMap.get("showResult") + "\n" + resultMap.get("typeResult")).show();
			new CalculateUtils.SaveTestResultTask(this, resultMap).execute();
			mShared.setHollendResult(resultMap.get("typeResult"), resultMap.get("pointResult"));
			break;
		case RequestCode_Test_Qizhi:
			if(data == null){
				return;
			}
			HashMap<String, String> resultQiZhiMap = CalculateUtils.calculateQizhiResult(this, 
					data.getIntegerArrayListExtra(ThoughtReadingUtils.PointResult));
			if(resultQiZhiMap == null){
				return;
			}
			new SimpleMessageDialog(this, getString(R.string.Test_Qizhi), 
					resultQiZhiMap.get("showResult") + "\n" + resultQiZhiMap.get("typeResult")).show();
			mShared.setQizhiResult(resultQiZhiMap.get("typeResult"), resultQiZhiMap.get("pointResult"));
			new CalculateUtils.SaveTestResultTask(this, resultQiZhiMap).execute();
			break;
		default:
			break;
		}
	}
}
