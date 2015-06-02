package com.dcy.psychology.fragment;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dcy.psychology.AllTestActivity;
import com.dcy.psychology.BigOpenClassActivity;
import com.dcy.psychology.LoginActivity;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.OnlinePicListActivity;
import com.dcy.psychology.PlamPictureDetailActivity;
import com.dcy.psychology.QuestionThemeChooseActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.SeaGameActivity;
import com.dcy.psychology.StudentGrowActivity;
import com.dcy.psychology.ThoughtReadingActivity;
import com.dcy.psychology.gsonbean.ArticleBean;
import com.dcy.psychology.gsonbean.GrowQuestionBean;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.ThoughtReadingUtils;
import com.dcy.psychology.util.Utils;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

public class StyleTwoBoxFragment extends Fragment implements OnClickListener{
	private Context mContext;
	private ImageView mNewPicView;
	private TextView mNewPicTitleTv;
	private TextView mNewPicSubTitleTv;
	private int newestArticleId = -1;
	private ArrayList<GrowQuestionBean> questionList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		new GetNewestArticleTask().execute();
		questionList = MyApplication.mGson.fromJson(Utils.loadRawString(mContext, 
				R.raw.homepage_growquestionlib), new TypeToken<ArrayList<GrowQuestionBean>>(){}.getType());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_style2_box_layout, null);
		view.findViewById(R.id.game_one_tv).setOnClickListener(this);
		view.findViewById(R.id.game_two_tv).setOnClickListener(this);
		view.findViewById(R.id.iv_open_class).setOnClickListener(this);
		view.findViewById(R.id.online_pic_tv).setOnClickListener(this);
		view.findViewById(R.id.tv_more_test).setOnClickListener(this);
		view.findViewById(R.id.ll_new_pic).setOnClickListener(this);
		view.findViewById(R.id.ll_new_test).setOnClickListener(this);
		mNewPicView = (ImageView) view.findViewById(R.id.iv_new_thumb);
		mNewPicTitleTv = (TextView) view.findViewById(R.id.tv_new_title);
		mNewPicSubTitleTv = (TextView) view.findViewById(R.id.tv_new_subtitle);
		return view;
	}
	
	private class GetNewestArticleTask extends AsyncTask<Void, Void, ArticleBean>{
		@Override
		protected ArticleBean doInBackground(Void... params) {
			return Utils.getNewestArticle();
		}
		
		@Override
		protected void onPostExecute(ArticleBean result) {
			if(result == null){
				return;
			}
			if(!TextUtils.isEmpty(result.getArticleSmallImgUrl())){
				AsyncImageCache.from(mContext).displayImage(mNewPicView, R.drawable.ic_launcher, 
						new AsyncImageCache.NetworkImageGenerator(result.getArticleSmallImgUrl(), result.getArticleSmallImgUrl()));
			}
			newestArticleId = result.getArticleID();
			mNewPicTitleTv.setText(result.getArticleName());
			mNewPicSubTitleTv.setText(result.getArticleSummary());
		}
	}
	
	@Override
	public void onClick(View v) {
		Intent mIntent = null;
		switch (v.getId()) {
		case R.id.game_one_tv:
			MobclickAgent.onEvent(mContext, "color_fish");
			mIntent = new Intent(mContext, StudentGrowActivity.class);
			break;
		case R.id.game_two_tv:
			MobclickAgent.onEvent(mContext, "mind_reading");
			mIntent = new Intent(mContext, QuestionThemeChooseActivity.class);
			break;
		case R.id.iv_open_class:
			if(TextUtils.isEmpty(MyApplication.myPhoneNum)){
				Toast.makeText(mContext, R.string.please_login, Toast.LENGTH_SHORT).show();
				mIntent = new Intent(mContext, LoginActivity.class);
			} else {
				mIntent = new Intent(mContext, BigOpenClassActivity.class);
			}
			break;
		case R.id.online_pic_tv:
			mIntent = new Intent(mContext, OnlinePicListActivity.class);
			break;
		case R.id.tv_more_test:
			mIntent = new Intent(mContext, AllTestActivity.class);
			break;
		case R.id.ll_new_pic:
			if(newestArticleId == -1){
				return;
			}
			mIntent = new Intent(mContext, PlamPictureDetailActivity.class);
			mIntent.putExtra(Constants.OnlineArticleId, newestArticleId);
			break;
		case R.id.ll_new_test:
			mIntent = new Intent(mContext, ThoughtReadingActivity.class);
			mIntent.putExtra(ThoughtReadingUtils.GrowBeanData, questionList.get(questionList.size() - 1));
			mIntent.putExtra(ThoughtReadingUtils.ThemeTitle, Constants.HomePageTestTitle[questionList.size() - 1]);
			break;
		default:
			break;
		}
		if(mIntent != null)
			startActivity(mIntent);
	}
}
