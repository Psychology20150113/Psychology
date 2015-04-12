package com.dcy.psychology;

import com.dcy.psychology.gsonbean.BasicBean;
import com.dcy.psychology.gsonbean.RegisterBean;
import com.dcy.psychology.gsonbean.SmsCodeBean;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.IMManager;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.Utils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PhoneRegisterActivity extends BaseActivity implements OnClickListener{
	private EditText mPhoneEt;
	private EditText mCodeEt;
	private EditText mPwdEt;
	private EditText mSecondPwdEt;
	private TextView mGetCodeText;
	private InfoShared mShared;
	private final int CountDownNum = 10;
	private int mCountDownTime = CountDownNum;
	private Handler mCountDownHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_register_layout);
		initView();
		mShared = new InfoShared(this);
	}

	private void initView(){
		setTopTitle(R.string.register);
		mPhoneEt = (EditText) findViewById(R.id.phone_et);
		mCodeEt = (EditText) findViewById(R.id.code_et);
		mPwdEt = (EditText) findViewById(R.id.password_et);
		mSecondPwdEt = (EditText) findViewById(R.id.second_password_et);
		mGetCodeText = (TextView) findViewById(R.id.get_code_tv);
		mGetCodeText.setOnClickListener(this);
		findViewById(R.id.register_tv).setOnClickListener(this);
	}
	
	private class RegisterTask extends AsyncTask<String, Void, BasicBean>{
		private String phoneNum;
		private String pwd;
		
		@Override
		protected void onPreExecute() {
			phoneNum = mPhoneEt.getText().toString();
			pwd = mPwdEt.getText().toString();
		}
		
		@Override
		protected BasicBean doInBackground(String... arg0) {
			return Utils.getVerifySmsCode(phoneNum, mShared.getPhoneNum(), pwd);
		}
		
		@Override
		protected void onPostExecute(BasicBean result) {
			super.onPostExecute(result);
			if(result.isResult()){
				mShared.savePhoneInfo(phoneNum, pwd, Constants.RoleUser);
				new ChatRegisterTask().execute(phoneNum, pwd);
			} else {
				Toast.makeText(PhoneRegisterActivity.this, result.getReason(), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private class ChatRegisterTask extends AsyncTask<String, Void, Boolean>{
		@Override
		protected Boolean doInBackground(String... params) {
			return IMManager.getInstance().registerIM(params[0], params[1]);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			hideCustomDialog();
			if(result){
				Toast.makeText(PhoneRegisterActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
				Intent mIntent = new Intent();
				mIntent.putExtra("login_success", true);
				setResult(1, mIntent);
				finish();
			}
		}
	}
	
	private class GetSmsCodeTask extends AsyncTask<String, Void, SmsCodeBean>{
		@Override
		protected SmsCodeBean doInBackground(String... params) {
			return Utils.sendSMS(mPhoneEt.getText().toString());
		}
		
		@Override
		protected void onPostExecute(SmsCodeBean result) {
			hideCustomDialog();
			if(result.isResult()){
				mShared.setPhoneNum(result.getVerifyCode());
				mGetCodeText.setClickable(false);
				mGetCodeText.setText("" + CountDownNum);
				mCountDownHandler.postDelayed(mCountDownRunnable, 1000);
			} else {
				Toast.makeText(PhoneRegisterActivity.this, result.getReason(), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_code_tv:
			Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
			if(!checkPhone()){
				return;
			}
			showCustomDialog();
			new GetSmsCodeTask().execute();
			break;
		case R.id.register_tv:
			if(!checkInput()){
				return;
			}
			showCustomDialog();
			new RegisterTask().execute();
			break;
		default:
			break;
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
				mGetCodeText.setText("OK");
			}
		}
	};
	
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
