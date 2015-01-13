package com.dcy.psychology;

import java.util.ArrayList;

import com.dcy.psychology.adapter.CommentListAdapter;
import com.dcy.psychology.gsonbean.CommentBean;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.Utils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class DiscussListActivity extends BaseActivity 
		implements OnItemClickListener , OnScrollListener{
	private ListView mListView;
	private CommentListAdapter mAdapter;
	private ArrayList<CommentBean> dataList = new ArrayList<CommentBean>();
	private int pageIndex = 1;
	private boolean isEnd = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTopTitle(R.string.discussion);
		showCustomDialog();
		setContentView(R.layout.activity_discussion_list_layout);
		initView();
		new CommentListTask().execute();
	}
	
	private void initView(){
		mListView = (ListView) findViewById(R.id.content_lv);
		mAdapter = new CommentListAdapter(this, dataList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setOnScrollListener(this);
	}
	
	private class CommentListTask extends AsyncTask<Void, Void, ArrayList<CommentBean>>{
		@Override
		protected ArrayList<CommentBean> doInBackground(Void... params) {
			return Utils.getCommentList(pageIndex);
		}
		
		@Override
		protected void onPostExecute(ArrayList<CommentBean> result) {
			hideCustomDialog();
			if(result == null)
				return;
			if (result.size() < Constants.PageCount)
				isEnd = true;
			pageIndex ++;
			dataList.addAll(result);
			mAdapter.updateList(dataList);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

	int lastItem;
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(!isEnd && lastItem == mAdapter.getCount() - 1 && scrollState == OnScrollListener.SCROLL_STATE_IDLE){
			showCustomDialog();
			new CommentListTask().execute();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		lastItem = firstVisibleItem + visibleItemCount - 1;
	}
}
