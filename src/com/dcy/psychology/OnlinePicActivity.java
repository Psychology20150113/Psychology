package com.dcy.psychology;

import com.dcy.psychology.fragment.OnlinePicFragment;

import android.os.Bundle;

public class OnlinePicActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_online_pic_layout);
		setTopTitle(R.string.online_pic);
		getFragmentManager().beginTransaction()
			.add(R.id.container_ll, new OnlinePicFragment()).commit();
	}
}
