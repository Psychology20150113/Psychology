package com.dcy.psychology.fragment;

import com.dcy.psychology.LoginActivity;
import com.dcy.psychology.R;
import com.dcy.psychology.adapter.SpecialUserListAdapter;
import com.dcy.psychology.db.DbManager;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.view.CustomProgressDialog;
import com.dcy.psychology.xinzeng.advance;
import com.dcy.psychology.xinzeng.help;
import com.dcy.psychology.xinzeng.mine_yueliao;
import com.dcy.psychology.xinzeng.underway;
import com.dcy.psychology.xinzeng.yueliao;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class Zhiyedianbofragment extends Fragment implements OnClickListener{
	

	private Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.zhiyedianbo, null);
		view.findViewById(R.id.mine_yueliao).setOnClickListener(this);
		view.findViewById(R.id.advance).setOnClickListener(this);
		view.findViewById(R.id.underway).setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent nIntent = null;
		switch (v.getId()) {
		case R.id.mine_yueliao:
			nIntent = new Intent(mContext, mine_yueliao.class);
			
			break;
		case R.id.advance:
			nIntent = new Intent(mContext, advance.class);
			break;
		case R.id.underway:
			nIntent = new Intent(mContext, underway.class);
			break;
		
		}
		if(nIntent != null){
			startActivity(nIntent);
		}
	}
}

