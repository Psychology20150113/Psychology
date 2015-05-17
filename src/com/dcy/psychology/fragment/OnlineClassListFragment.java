package com.dcy.psychology.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.dcy.psychology.PlamPictureDetailActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.adapter.ArticleListAdapter;
import com.dcy.psychology.adapter.ClassShowListAdapter;
import com.dcy.psychology.gsonbean.ArticleBean;
import com.dcy.psychology.gsonbean.ClassBean;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.CustomProgressDialog;
import com.dcy.psychology.view.PullRefreshListView;
import com.dcy.psychology.view.PullRefreshListView.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

public class OnlineClassListFragment extends Fragment implements OnItemClickListener{
	private Context mContext;
	private CustomProgressDialog mDialog;
	private int pageIndex = 1;
	private int categoryId = 1;
	private PullRefreshListView mListView;
	private ClassShowListAdapter mAdapter;
	private ArrayList<ClassBean> mDataList = new ArrayList<ClassBean>();
	private boolean isRefresh = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mAdapter = new ClassShowListAdapter(mContext, mDataList);
		mDialog = new CustomProgressDialog(mContext);
		mDialog.show();
		categoryId = getArguments().getInt(Constants.ClassCategoryId);
		new GetClassListTask().execute();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_online_pic_layout, null);
		mListView = (PullRefreshListView) view.findViewById(R.id.pull_refresh_lv);
		mListView.setOnItemClickListener(this);
		mListView.setonRefreshListener(mRefreshListener);
		mListView.setOnScrollListener(mScrollListener);
		mListView.setAdapter(mAdapter);
		return view;
	}
	
	private OnRefreshListener mRefreshListener = new OnRefreshListener() {
		@Override
		public void onRefresh() {
			mDataList.removeAll(mDataList);
			pageIndex = 1;
			mListView.onRefreshComplete();
			mDialog.show();
			isRefresh = true;
			new GetClassListTask().execute();
		}
	};
	
	private OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if(scrollState == SCROLL_STATE_IDLE){
				if(mListView.getLastVisiblePosition() == mListView.getCount() - 1){
					if (isRefresh) {
						isRefresh = false;
					} else {
						pageIndex ++;
						mDialog.show();
						new GetClassListTask().execute();
					}
				}
			}
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
		}
	};
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Map<String, String> picMap = new HashMap<String, String>();
		picMap.put("name", mDataList.get(position - 1).getClassTitleName());
		MobclickAgent.onEvent(mContext, "article_without_theme", picMap);
		Intent mIntent = new Intent(mContext, PlamPictureDetailActivity.class);
		mIntent.putExtra(Constants.OnlineClassId, mDataList.get(position - 1).getClassID());
		startActivity(mIntent);
	}
	
	private class GetClassListTask extends AsyncTask<Void, Void, ArrayList<ClassBean>>{
		@Override
		protected ArrayList<ClassBean> doInBackground(Void... params) {
			return Utils.getClassList(pageIndex, categoryId);
		}
		
		@Override
		protected void onPostExecute(ArrayList<ClassBean> result) {
			mDialog.dismiss();
			if(result == null){
				return;
			}
			mDataList.addAll(result);
			mAdapter.notifyDataSetChanged();
		}
	}
}
