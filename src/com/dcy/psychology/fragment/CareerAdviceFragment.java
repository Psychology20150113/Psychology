package com.dcy.psychology.fragment;

import com.dcy.psychology.LoginActivity;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.dcy.psychology.xinzeng.AdvanceActivity;
import com.dcy.psychology.xinzeng.Mine_talkaboutActivity;
import com.dcy.psychology.xinzeng.UnderwayActivity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
		View view = inflater.inflate(R.layout.fragment_career_advice_layout, null);
		view.findViewById(R.id.btn_mine_talkabout).setOnClickListener(this);
		view.findViewById(R.id.btn_advance).setOnClickListener(this);
		view.findViewById(R.id.btn_underway).setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO �Զ���ɵķ������
		Intent nIntent = null;
		switch (v.getId()) {
		case R.id.btn_mine_talkabout:
			
			if(TextUtils.isEmpty(MyApplication.myPhoneNum)){
				nIntent = new Intent(mContext, LoginActivity.class);
			} else
			{
			nIntent = new Intent(mContext, Mine_talkaboutActivity.class);
			}
			
			break;
		case R.id.btn_advance:
			if(TextUtils.isEmpty(MyApplication.myPhoneNum)){
				nIntent = new Intent(mContext, LoginActivity.class);
			} else
			{
			nIntent = new Intent(mContext, AdvanceActivity.class);
			}
			break;
		case R.id.btn_underway:
			if(TextUtils.isEmpty(MyApplication.myPhoneNum)){
				nIntent = new Intent(mContext, LoginActivity.class);
			} else
			{
			nIntent = new Intent(mContext, UnderwayActivity.class);
			}		
			break;
		
		}
		if(nIntent != null){
			startActivity(nIntent);
		}
	}
}

