package com.dcy.psychology;

import com.dcy.psychology.adapter.ClassShowListAdapter;

import android.os.Bundle;

public class OpenClassActivity extends BaseActivity {
	private ClassShowListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_layout);
	}
}
