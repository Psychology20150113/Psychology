package com.dcy.psychology.fragment;

import java.sql.NClob;
import java.util.ArrayList;

import com.dcy.psychology.PlamPictureDetailActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.adapter.ArticleListAdapter;
import com.dcy.psychology.gsonbean.ArticleBean;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.CustomCircleView;
import com.dcy.psychology.view.CustomProgressDialog;
import com.dcy.psychology.view.PullRefreshListView;
import com.dcy.psychology.view.PullRefreshListView.OnRefreshListener;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnlinePicFragment extends Fragment implements OnItemClickListener{
	private Context mContext;
	private CustomProgressDialog mDialog;
	private int pageIndex = 1;
	private PullRefreshListView mListView;
	private ArticleListAdapter mAdapter;
	private ArrayList<ArticleBean> mDataList = new ArrayList<ArticleBean>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mAdapter = new ArticleListAdapter(mContext, mDataList);
		mDialog = new CustomProgressDialog(mContext);
		mDialog.show();
		new GetArticleListTask().execute();
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
			new GetArticleListTask().execute();
		}
	};
	
	private OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if(scrollState == SCROLL_STATE_IDLE){
				if(mListView.getLastVisiblePosition() == mListView.getCount() - 1){
					pageIndex ++;
					mDialog.show();
					new GetArticleListTask().execute();
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
		Intent mIntent = new Intent(mContext, PlamPictureDetailActivity.class);
		mIntent.putExtra(Constants.OnlineArticleId, mDataList.get(position - 1).getArticleID());
		startActivity(mIntent);
	}
	
	private class GetArticleListTask extends AsyncTask<Void, Void, ArrayList<ArticleBean>>{
		@Override
		protected ArrayList<ArticleBean> doInBackground(Void... params) {
			return Utils.getArticleList(pageIndex);
		}
		
		@Override
		protected void onPostExecute(ArrayList<ArticleBean> result) {
			mDialog.dismiss();
			if(result == null){
				return;
			}
			mDataList.addAll(result);
			mAdapter.notifyDataSetChanged();
		}
	}
}
