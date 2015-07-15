package com.dcy.psychology.xinzeng;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.dcy.psychology.BaseActivity;
import com.dcy.psychology.R;

public class mine_yueliao extends BaseActivity implements OnClickListener{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_yueliao);
		setTopTitle(R.string.mine_yueliao);
		findViewById(R.id.yiyueliao).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		
	}
}