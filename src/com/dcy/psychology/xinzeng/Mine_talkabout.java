package com.dcy.psychology.xinzeng;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.R;

public class Mine_talkabout extends BaseActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_talkabout);
		setTopTitle(R.string.mine_talkabout);
	}

	
}