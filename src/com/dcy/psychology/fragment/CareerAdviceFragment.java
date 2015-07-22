package com.dcy.psychology.fragment;

import com.dcy.psychology.R;
import com.dcy.psychology.view.dialog.IndividualDialog;
import com.dcy.psychology.xinzeng.AboutActivity;
import com.dcy.psychology.xinzeng.AdvanceActivity;
import com.dcy.psychology.xinzeng.UnderwayActivity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class CareerAdviceFragment extends Fragment implements OnClickListener{
	

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
		View view = inflater.inflate(R.layout.fragment_career_advice, null);
		view.findViewById(R.id.btn_mine_talkabout).setOnClickListener(this);
		view.findViewById(R.id.btn_advance).setOnClickListener(this);
		view.findViewById(R.id.btn_underway).setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		Intent nIntent = null;
		switch (v.getId()) {
		case R.id.btn_mine_talkabout:
			nIntent = new Intent(mContext, IndividualDialog.class);
			
			break;
		case R.id.btn_advance:
			nIntent = new Intent(mContext, AdvanceActivity.class);
			break;
		case R.id.btn_underway:
			nIntent = new Intent(mContext, UnderwayActivity.class);
			break;
		
		}
		if(nIntent != null){
			startActivity(nIntent);
		}
	}
}

