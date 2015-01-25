package com.dcy.psychology;

import com.dcy.psychology.gsonbean.LoginBean;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.CustomProgressDialog;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements OnClickListener{
	private EditText accountET;
	private EditText pwdET;
	
	private class LoginTask extends AsyncTask<Void, Void, LoginBean>{
		@Override
		protected LoginBean doInBackground(Void... arg0) {
			/*EMChatManager.getInstance().login(AsyncImageCache.MD5.Md5(accountET.getText().toString()),
					AsyncImageCache.MD5.Md5(pwdET.getText().toString()), new EMCallBack() {
				@Override
				public void onError(int arg0, String arg1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(LoginActivity.this, R.string.login_chat_failed, Toast.LENGTH_SHORT).show();
						}
					});
				}
				@Override
				public void onProgress(int arg0, String arg1) {
				}
				@Override
				public void onSuccess() {
					Utils.getFriends(LoginActivity.this);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(LoginActivity.this, R.string.login_chat_success, Toast.LENGTH_SHORT).show();
						}
					});
				}
			});*/
			return Utils.getLoginWeb(accountET.getText().toString(), pwdET.getText().toString());
		}
		
		@Override
		protected void onPostExecute(LoginBean result) {
			hideCustomDialog();
			if("NO".equals(result.getResult())){
				Toast.makeText(LoginActivity.this, result.getReason(), Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
				saveInfo(result.getLoginState());
				Intent mIntent = new Intent();
				mIntent.putExtra("login_success", true);
				setResult(0, mIntent);
				finish();
			}
		}
		
	} 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_layout);
		initView();
	}
	
	private void initView(){
		setTopTitle(R.string.login);
		setRightText(R.string.register);
		accountET = (EditText) findViewById(R.id.account_et);
		pwdET = (EditText) findViewById(R.id.psw_et);
		findViewById(R.id.login_btn).setOnClickListener(this);
	}
	
	@Override
	public void onRightTextClick() {
		startActivityForResult(new Intent(this, RegisterActivity.class) , 0);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.login_btn:
			if(checkInput()){
				showCustomDialog();
				new LoginTask().execute();
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
			if(data != null){
				setResult(0, data);
			}
			finish();
			break;

		default:
			break;
		}
	}
	
	private void saveInfo(String role){
		String account = accountET.getText().toString();
		String pwd = pwdET.getText().toString();
		InfoShared mInfo = new InfoShared(LoginActivity.this);
		mInfo.setUserName(account);
		mInfo.setUserPwd(pwd);
		mInfo.setUserRole(role);
		MyApplication.myUserName = account;
		MyApplication.myPwd = pwd;
		MyApplication.myUserRole = role;
	}
	
	private boolean checkInput(){
		if(TextUtils.isEmpty(accountET.getText())){
			Toast.makeText(this, R.string.account_empty, Toast.LENGTH_SHORT).show();
			accountET.requestFocus();
			accountET.setSelection(accountET.getText().length());
			return false;
		}
		if(TextUtils.isEmpty(pwdET.getText())){
			Toast.makeText(this, R.string.pwd_empty, Toast.LENGTH_SHORT).show();
			pwdET.requestFocus();
			pwdET.setSelection(pwdET.getText().length());
			return false;
		}
		return true;
	}
}
