package com.dcy.psychology;

import u.aly.br;

import com.dcy.psychology.gsonbean.BasicBean;
import com.dcy.psychology.gsonbean.SmsCodeBean;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.Utils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FindPwdActivity extends BaseActivity implements OnClickListener{
	private EditText mPhoneEt;
	private EditText mCodeEt;
	private EditText mPwdEt;
	private EditText mSecondPwdEt;
	private TextView mGetCodeText;
	private InfoShared mShared;
	private final int CountDownNum = 30;
	private int mCountDownTime = CountDownNum;
	private Handler mCountDownHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_pwd_layout);
		initView();
		mShared = new InfoShared(this);
	}
	
	private void initView(){
		setTopTitle(R.string.find_pwd);
		mPhoneEt = (EditText) findViewById(R.id.phone_et);
		mCodeEt = (EditText) findViewById(R.id.code_et);
		mPwdEt = (EditText) findViewById(R.id.password_et);
		mSecondPwdEt = (EditText) findViewById(R.id.second_password_et);
		mGetCodeText = (TextView) findViewById(R.id.get_code_tv);
		mGetCodeText.setOnClickListener(this);
		findViewById(R.id.change_ok_tv).setOnClickListener(this);
	}
	
	private class ChangePwdTask extends AsyncTask<String, Void, BasicBean>{
		@Override
		protected BasicBean doInBackground(String... params) {
			return Utils.getVerifyFindSmsCode(mPhoneEt.getText().toString(), 
					mCodeEt.getText().toString(), mPwdEt.getText().toString());
		}
		
		@Override
		protected void onPostExecute(BasicBean result) {
			hideCustomDialog();
			if(result.isResult()){
				Toast.makeText(FindPwdActivity.this, R.string.change_ped_success, Toast.LENGTH_SHORT).show();
				setResult(100);
				finish();
			} else {
				Toast.makeText(FindPwdActivity.this, result.getReason(), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private class GetFindSmsCodeTask extends AsyncTask<String, Void, SmsCodeBean>{
		@Override
		protected SmsCodeBean doInBackground(String... params) {
			return Utils.sendFindSMS(mPhoneEt.getText().toString());
		}
		
		@Override
		protected void onPostExecute(SmsCodeBean result) {
			hideCustomDialog();
			if(result.isResult()){
				Log.i("mylog", result.getVerifyCode());
				mGetCodeText.setClickable(false);
				mGetCodeText.setText("" + CountDownNum);
				mCountDownHandler.postDelayed(mCountDownRunnable, 1000);
			} else {
				Toast.makeText(FindPwdActivity.this, result.getReason(), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private Runnable mCountDownRunnable = new Runnable() {
		@Override
		public void run() {
			mCountDownTime --;
			mGetCodeText.setText(mCountDownTime + "");
			if(mCountDownTime > 0){
				mCountDownHandler.postDelayed(mCountDownRunnable, 1000);
			} else {
				mGetCodeText.setText(R.string.retry);
				mGetCodeText.setClickable(true);
				mCountDownTime = CountDownNum;
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.get_code_tv:
				if(!checkPhone()){
					return;
				}
				showCustomDialog();
				new GetFindSmsCodeTask().execute();
				break;
			case R.id.change_ok_tv:
				if(!checkInput()){
					return;
				}
				showCustomDialog();
				new ChangePwdTask().execute();
				break;
			default:
					break;
			}
	}
	
	private boolean checkPhone(){
		if(TextUtils.isEmpty(mPhoneEt.getText())){
			Toast.makeText(this, R.string.phone_empty, Toast.LENGTH_SHORT).show();
			mPhoneEt.requestFocus();
			return false;
		}
		if(!Utils.validatePhoneNumber(mPhoneEt.getText().toString())){
			Toast.makeText(this, R.string.phone_error, Toast.LENGTH_SHORT).show();
			mPhoneEt.requestFocus();
			mPhoneEt.setSelection(mPhoneEt.getText().length());
			return false;
		}
		return true;
	}
	
	private boolean checkInput(){
		if(TextUtils.isEmpty(mPwdEt.getText())){
			Toast.makeText(this, R.string.pwd_empty, Toast.LENGTH_SHORT).show();
			mPwdEt.requestFocus();
			return false;
		}
		if(TextUtils.isEmpty(mSecondPwdEt.getText())){
			Toast.makeText(this, R.string.pwd_makesure, Toast.LENGTH_SHORT).show();
			mSecondPwdEt.requestFocus();
			return false;
		}
		if(!mSecondPwdEt.getText().toString().equals(mPwdEt.getText().toString())){
			Toast.makeText(this, R.string.pwd_diff, Toast.LENGTH_SHORT).show();
			mSecondPwdEt.requestFocus();
			mSecondPwdEt.setSelection(mSecondPwdEt.getText().length());
			return false;
		}
		if(TextUtils.isEmpty(mCodeEt.getText())){
			Toast.makeText(this, R.string.get_verfication_code, Toast.LENGTH_SHORT).show();
			mCodeEt.requestFocus();
			return false;
		}
		return true;
	}
}
