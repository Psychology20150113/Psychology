package com.dcy.psychology.fragment;

import java.util.ArrayList;

import com.dcy.psychology.FlowerGameChooseActivity;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.PlamPictureDetailActivity;
import com.dcy.psychology.PlatformTwoActivity;
import com.dcy.psychology.QuestionThemeChooseActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.SeaGameActivity;
import com.dcy.psychology.StudentGrowActivity;
import com.dcy.psychology.ThoughtReadingActivity;
import com.dcy.psychology.adapter.HomeListAdapter;
import com.dcy.psychology.gsonbean.GrowPictureBean;
import com.dcy.psychology.gsonbean.GrowQuestionBean;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.ThoughtReadingUtils;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.PageIndicatorView;
import com.dcy.psychology.view.PullRefreshListView;
import com.dcy.psychology.view.PullRefreshListView.OnRefreshListener;
import com.google.gson.reflect.TypeToken;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class SlideMainFragment extends Fragment implements OnClickListener , OnItemClickListener{
	private Context mContext;
	private PullRefreshListView mListView;
	private ArrayList<GrowPictureBean> dataList;
	private ArrayList<GrowQuestionBean> questionList;
	private HomeListAdapter mAdapter;
	
	private PageIndicatorView indicatorView;
	private final int[] bannerRes = {R.drawable.banner_one , R.drawable.banner_two , R.drawable.banner_three};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		dataList = MyApplication.mGson.fromJson(Utils.loadRawString(mContext, R.raw.homepage_pic_text_lib), new TypeToken<ArrayList<GrowPictureBean>>(){}.getType());
		questionList = MyApplication.mGson.fromJson(Utils.loadRawString(mContext, R.raw.homepage_growquestionlib), new TypeToken<ArrayList<GrowQuestionBean>>(){}.getType());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_slide_main_layout, null);
		view.findViewById(R.id.platform_one_ll).setOnClickListener(this);
		view.findViewById(R.id.platform_two_ll).setOnClickListener(this);
		view.findViewById(R.id.game_one_tv).setOnClickListener(this);
		view.findViewById(R.id.game_two_tv).setOnClickListener(this);
		view.findViewById(R.id.game_more_tv).setOnClickListener(this);
		ArrayList<View> bannerList = new ArrayList<View>();
		for(int i = 0 ; i < bannerRes.length ; i ++){
			ImageView item = new ImageView(mContext);
			item.setScaleType(ScaleType.FIT_XY);
			item.setImageResource(bannerRes[i]);
			bannerList.add(item);
		}
		ViewPager bannerPager = (ViewPager) view.findViewById(R.id.banner_vp);
		bannerPager.setAdapter(new Utils.ViewAdapter(bannerList));
		bannerPager.setOnPageChangeListener(mBannerListener);
		indicatorView = (PageIndicatorView) view.findViewById(R.id.banner_indicator);
		indicatorView.setCount(bannerRes.length , true);
		indicatorView.updateIndicator(0);
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
	
	private OnPageChangeListener mBannerListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			indicatorView.updateIndicator(position);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if((position-1) % mAdapter.getCount() == 1){
			Intent mIntent = new Intent(mContext, ThoughtReadingActivity.class);
			mIntent.putExtra(ThoughtReadingUtils.GrowBeanData, questionList.get(mAdapter.getPageIndex()));
			mIntent.putExtra(ThoughtReadingUtils.ThemeTitle, Constants.HomePageTestTitle[mAdapter.getPageIndex()]);
			startActivity(mIntent);
		}else {
			Intent mIntent = new Intent(mContext, PlamPictureDetailActivity.class);
			mIntent.putExtra(Constants.PictureBean, (GrowPictureBean)mAdapter.getItem(position - 1));
			startActivity(mIntent);
		}
	}
	
	@Override
	public void onClick(View v) {
		Intent mIntent = null;
		switch (v.getId()) {
		case R.id.platform_one_ll:
			mIntent = new Intent(mContext, StudentGrowActivity.class);
			break;
		case R.id.platform_two_ll:
			mIntent = new Intent(mContext, PlatformTwoActivity.class);
			break;
		case R.id.game_one_tv:
//			mIntent = new Intent(mContext, FlowerGameChooseActivity.class);
			mIntent = new Intent(mContext, SeaGameActivity.class);
			break;
		case R.id.game_two_tv:
			mIntent = new Intent(mContext, QuestionThemeChooseActivity.class);
			break;
		case R.id.game_more_tv:
			break;
		default:
			break;
		}
		if(mIntent != null)
			startActivity(mIntent);
	}
}
