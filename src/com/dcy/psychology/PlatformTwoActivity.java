package com.dcy.psychology;

import com.dcy.psychology.fragment.TabCureFragment;

import android.os.Bundle;

public class PlatformTwoActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_platform_two_layout);
		setTopTitle(R.string.platform_two);
		getFragmentManager().beginTransaction()
			.add(R.id.container, new TabCureFragment()).commit();
	}
}
