package com.dcy.psychology;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.dcy.psychology.R;
import com.dcy.psychology.adapter.BigClassShowListAdapter;
import com.dcy.psychology.gsonbean.ClassBean;
import com.dcy.psychology.util.Utils;

public class BigOpenClassActivity extends BaseActivity {
	private BigClassShowListAdapter mAdapter;
	private ArrayList<ClassBean> mDateList = new ArrayList<ClassBean>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_layout);
		setTopTitle(R.string.open_class);
		ListView mListView = (ListView) findViewById(R.id.list_lv);
		mAdapter = new BigClassShowListAdapter(this, mDateList);
		mListView.setAdapter(mAdapter);
		showCustomDialog();
		new GetNewestClassTask().execute();
	}
	
	private class GetNewestClassTask extends AsyncTask<Void, Void, ArrayList<ClassBean>> {
		@Override
		protected ArrayList<ClassBean> doInBackground(Void... params) {
			return Utils.getNewestClassList();
		}
		
		@Override
		protected void onPostExecute(ArrayList<ClassBean> result) {
			hideCustomDialog();
			if(result == null || result.size() == 0){
				return;
			}
			mDateList.addAll(result);
			mAdapter.notifyDataSetChanged();
		}
	}
}
