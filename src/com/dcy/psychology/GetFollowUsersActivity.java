package com.dcy.psychology;

import java.util.ArrayList;

import com.dcy.psychology.adapter.SpecialUserListAdapter;
import com.dcy.psychology.gsonbean.SpecificUserBean;
import com.dcy.psychology.util.Utils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

public class GetFollowUsersActivity extends BaseActivity {
	private ListView mListView;
	private SpecialUserListAdapter mAdapter;
	private ArrayList<SpecificUserBean> dataList = new ArrayList<SpecificUserBean>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_follow_user_layout);
		setTopTitle(R.string.mine_attention);
		mAdapter = new SpecialUserListAdapter(this, dataList, false);
		mListView = (ListView) findViewById(R.id.attention_lv);
		mListView.setAdapter(mAdapter);
		showCustomDialog();
		new GetAttentionTask().execute();
	}
	
	private class GetAttentionTask extends AsyncTask<Void, Void, ArrayList<SpecificUserBean>>{
		@Override
		protected ArrayList<SpecificUserBean> doInBackground(
				Void... params) {
			return Utils.getFollowSpecificUserList();
		}
		
		@Override
		protected void onPostExecute(ArrayList<SpecificUserBean> result) {
			hideCustomDialog();
			if(result == null){
				return;
			}
			dataList.addAll(result);
			mAdapter.notifyDataSetChanged();
		}
	}
}
