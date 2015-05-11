package com.dcy.psychology;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ListView;

import com.dcy.psychology.adapter.BigClassShowListAdapter;
import com.dcy.psychology.gsonbean.ClassBean;

public class BigOpenClassActivity extends BaseActivity {
	private BigClassShowListAdapter mAdapter;
	private ArrayList<ClassBean> mDateList = new ArrayList<ClassBean>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_layout);
		ListView mListView = (ListView) findViewById(R.id.list_lv);
		mAdapter = new BigClassShowListAdapter(this, mDateList);
		mListView.setAdapter(mAdapter);
	}
}
