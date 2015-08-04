package com.dcy.psychology.fragment;

import java.util.ArrayList;

import com.dcy.psychology.FlowerGameChooseActivity;
import com.dcy.psychology.MainActivity;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.PlamPictureDetailActivity;
import com.dcy.psychology.QuestionThemeChooseActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.StudentGrowActivity;
import com.dcy.psychology.ThoughtReadingActivity;
import com.dcy.psychology.adapter.HomeListAdapter;
import com.dcy.psychology.gsonbean.GrowPictureBean;
import com.dcy.psychology.gsonbean.GrowQuestionBean;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.ThoughtReadingUtils;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.PullRefreshListView;
import com.dcy.psychology.view.PullRefreshListView.OnRefreshListener;
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;

public class TabGrowthFragment extends Fragment implements OnClickListener,OnItemClickListener{
	private Context mContext;
	private PullRefreshListView mListView;
	private ArrayList<GrowPictureBean> dataList;
	private ArrayList<GrowQuestionBean> questionList;
	private HomeListAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		//dataList = MyApplication.mGson.fromJson(Utils.loadRawString(mContext, R.raw.homepage_pic_text_lib), new TypeToken<ArrayList<GrowPictureBean>>(){}.getType());
		questionList = MyApplication.mGson.fromJson(Utils.loadRawString(mContext, R.raw.homepage_growquestionlib), new TypeToken<ArrayList<GrowQuestionBean>>(){}.getType());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_one_layout, null);
		view.findViewById(R.id.game_btn).setOnClickListener(this);
		view.findViewById(R.id.thought_btn).setOnClickListener(this);
		view.findViewById(R.id.platform_one_ll).setOnClickListener(this);
		view.findViewById(R.id.platform_two_ll).setOnClickListener(this);
		view.findViewById(R.id.show_all_ll).setOnClickListener(this);
		mAdapter = new HomeListAdapter(mContext, dataList);
		mListView = (PullRefreshListView) view.findViewById(R.id.pull_refresh_lv);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setonRefreshListener(mRefreshListener);
		return view;
	}
	
	private OnRefreshListener mRefreshListener = new OnRefreshListener() {
		@Override
		public void onRefresh() {
			mAdapter.notifyDataSetChanged();
			mListView.onRefreshComplete();
		}
	};
	
	@Override
	public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
		if((position-1) % mAdapter.getCount() == 1){
			Intent mIntent = new Intent(mContext, ThoughtReadingActivity.class);
			mIntent.putExtra(ThoughtReadingUtils.GrowBeanData, questionList.get((position - 1)/ mAdapter.getCount()));
			mIntent.putExtra(ThoughtReadingUtils.ThemeTitle, Constants.HomePageTestTitle[(position - 1)/ mAdapter.getCount()]);
			startActivity(mIntent);
		}else {
			Intent mIntent = new Intent(mContext, PlamPictureDetailActivity.class);
			mIntent.putExtra(Constants.PictureBean, (GrowPictureBean)mAdapter.getItem(position - 1));
			startActivity(mIntent);
		}
	};
	
	@Override
	public void onClick(View view) {
		Intent mIntent = null;
		switch (view.getId()) {
			case R.id.game_btn:
				mIntent = new Intent(mContext, FlowerGameChooseActivity.class);
				break;
			case R.id.thought_btn:
				mIntent = new Intent(mContext, QuestionThemeChooseActivity.class);
				break;
			case R.id.platform_one_ll:
				mIntent = new Intent(mContext, StudentGrowActivity.class);
				break;
			case R.id.platform_two_ll:
				((MainActivity)getActivity()).checkCureFragment();
				break;
			case R.id.show_all_ll:
//				mIntent = new Intent(mContext, HomePagePicActivity.class);
//				mIntent.putExtra("growpicturelist", dataList);
//				mIntent.putExtra("questionlist", questionList);
				break;
			default:
				break;
		}
		if(mIntent != null)
			startActivity(mIntent);
	}
}
