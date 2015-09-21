package com.dcy.psychology;

import com.dcy.psychology.R;
import com.dcy.psychology.LoginActivity.ChatLoginTask;
import com.dcy.psychology.gsonbean.BasicBean;
import com.dcy.psychology.gsonbean.LoginBean;
import com.dcy.psychology.gsonbean.PhoneRegisterBean;
import com.dcy.psychology.gsonbean.SmsCodeBean;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.IMManager;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.CustomProgressDialog;
import com.dcy.psychology.xinzeng.PersonalInfo_PerfectActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PhoneRegisterActivity extends Activity implements OnClickListener{
	private EditText mPhoneEt;
	private EditText mCodeEt;
	private EditText mPwdEt;
	private EditText mSecondPwdEt;
	private TextView mGetCodeText;
	private CustomProgressDialog mDialog;
	private InfoShared mShared;
	private final int CountDownNum = 30;
	private ImageView magree;
	private ImageView mnoagree;
	private ImageView backview;
	

	private int mCountDownTime = CountDownNum;
	private Handler mCountDownHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); //璁剧疆鏃犳爣棰�
		setContentView(R.layout.activity_phone_register_layout);
		initView();
		mShared = new InfoShared(this);
	}

	private void initView(){
		//setTopTitle(R.string.register);
		mPhoneEt = (EditText) findViewById(R.id.phone_et);
		mCodeEt = (EditText) findViewById(R.id.code_et);
		mPwdEt = (EditText) findViewById(R.id.password_et);
		mSecondPwdEt = (EditText) findViewById(R.id.second_password_et);
		mGetCodeText = (TextView) findViewById(R.id.get_code_tv);
		magree = (ImageView) findViewById(R.id.iv_agree);
		mnoagree = (ImageView) findViewById(R.id.iv_noagree);
		magree.setOnClickListener(this);
		mnoagree.setOnClickListener(this);
		mGetCodeText.setOnClickListener(this);
		backview=(ImageView) findViewById(R.id.iv_back);
		backview.setImageResource(R.drawable.icon_orangeback);
		backview.setOnClickListener(this);
		findViewById(R.id.register_tv).setOnClickListener(this);
		//findViewById(R.id.find_pwd_tv).setOnClickListener(this);
	}
	
	private class RegisterTask extends AsyncTask<String, Void, PhoneRegisterBean>{
		private String phoneNum;
		private String pwd;
		
		@Override
		protected void onPreExecute() {
			phoneNum = mPhoneEt.getText().toString();
			pwd = mPwdEt.getText().toString();
		}
		
		@Override
		protected PhoneRegisterBean doInBackground(String... arg0) {
			return Utils.getVerifySmsCode(phoneNum, mCodeEt.getText().toString(), pwd);
		}
		
		@Override
		protected void onPostExecute(PhoneRegisterBean result) {
			super.onPostExecute(result);
			if(result.isResult()){
				mShared.savePhoneInfo(phoneNum, pwd, Constants.RoleUser, false);
				Intent mIntent = new Intent(PhoneRegisterActivity.this,PersonalInfo_PerfectActivity.class);
				mIntent.putExtra("login_success", true);
				if(!TextUtils.isEmpty(result.HXPWD)){
					mShared.setHxPwd(result.HXPWD);
					MyApplication.getInstance().getChatManager().chatLogin(MyApplication.myPhoneNum, result.HXPWD);
				}
				//new ChatLoginTask(PhoneRegisterActivity.this).execute(MyApplication.myPhoneNum, MyApplication.myPwd);
				setResult(1, mIntent);
				finish();
//				new ChatRegisterTask().execute(phoneNum, pwd);
			} else {
				hideCustomDialog();
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
				Intent mIntent = new Intent(PhoneRegisterActivity.this,PersonalInfo_PerfectActivity.class);
				mIntent.putExtra("login_success", true);
				new ChatLoginTask(PhoneRegisterActivity.this).execute(MyApplication.myPhoneNum, MyApplication.myPwd);
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
				Log.i("mylog", result.getVerifyCode());
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
		/*case R.id.find_pwd_tv:
			startActivityForResult(new Intent(this, FindPwdActivity.class), 0);
			break;*/
		case R.id.iv_agree:
			magree.setVisibility(View.GONE);
			mnoagree.setVisibility(View.VISIBLE);
			break;
		case R.id.iv_noagree:
			mnoagree.setVisibility(View.GONE);
			magree.setVisibility(View.VISIBLE);
			break;
		case R.id.iv_back:
				finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 100:
			finish();
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
				mGetCodeText.setText(R.string.retry);
				mGetCodeText.setClickable(true);
				mCountDownTime = CountDownNum;
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
	protected void showCustomDialog(){
		if(mDialog == null){
			mDialog = new CustomProgressDialog(this);
		}
		mDialog.show();
	}
	
	protected void hideCustomDialog(){
		if(mDialog != null && mDialog.isShowing())
			mDialog.dismiss();
	}
}
