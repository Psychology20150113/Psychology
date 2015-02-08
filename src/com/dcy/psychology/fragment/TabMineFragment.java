package com.dcy.psychology.fragment;

import com.dcy.psychology.LoginActivity;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.dcy.psychology.RegisterActivity;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.Utils;

import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TabMineFragment extends Fragment implements OnClickListener{
	
	private LinearLayout mLoginLayout;
	private LinearLayout mUnLoginLayout;
	private TextView mNameText;
	private TextView mLogoutText;
	
	private Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mine_layout, null);
		view.findViewById(R.id.login_btn).setOnClickListener(this);
		view.findViewById(R.id.register_btn).setOnClickListener(this);
		mLoginLayout = (LinearLayout) view.findViewById(R.id.mine_load_ll);
		mUnLoginLayout = (LinearLayout) view.findViewById(R.id.mine_unlogin_ll);
		mNameText = (TextView) view.findViewById(R.id.mine_name_tv);
		mLogoutText = (TextView) view.findViewById(R.id.quit_btn);
		mLogoutText.setOnClickListener(this);
		if(TextUtils.isEmpty(MyApplication.myUserName)){
			logoutStatus();
		}else {
			loginStatus();
		}
		return view;
	}
	
	private void loginStatus(){
		mUnLoginLayout.setVisibility(View.GONE);
		mLoginLayout.setVisibility(View.VISIBLE);
		mNameText.setText(TextUtils.isEmpty(MyApplication.myNick) ?
				MyApplication.myUserName : MyApplication.myNick);
		mLogoutText.setVisibility(View.VISIBLE);
	}
	
	private void logoutStatus(){
		mLoginLayout.setVisibility(View.GONE);
		mLogoutText.setVisibility(View.GONE);
		mUnLoginLayout.setVisibility(View.VISIBLE);
	}
	
	private void clearInfo(){
		MyApplication.myUserName = "";
		MyApplication.myPwd = "";
		MyApplication.myNick = "";
		new InfoShared(mContext).clearInfo();
	}
	
	@Override
	public void onClick(View view) {
		Intent mIntent = null;
		switch (view.getId()) {
		case R.id.login_btn:
			mIntent = new Intent(mContext , LoginActivity.class);
			startActivityForResult(mIntent, 0);
			break;
		case R.id.register_btn:
			mIntent = new Intent(mContext, RegisterActivity.class);
			startActivityForResult(mIntent, 1);
			break;
		case R.id.quit_btn:
			clearInfo();
			logoutStatus();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
		case 0:
			if(data != null){
				if(data.getBooleanExtra("login_success", false)){
					loginStatus();
					//startActivity(new Intent(mContext, ChatMainActivity.class));
				}
			}
			break;
		default:
			break;
		}
	}
}
