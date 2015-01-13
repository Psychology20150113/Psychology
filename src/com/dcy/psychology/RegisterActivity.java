package com.dcy.psychology;

import com.dcy.psychology.model.UserInfoModel;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.Utils;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity implements OnClickListener{
	private EditText accountEt;
	private EditText nickEt;
	private EditText pwdEt;
	private EditText surePwdEt;
	private RadioGroup sexGroup;
	private EditText ageEt;
	private EditText phoneEt;
	private EditText mailEt;
	private EditText pwdQuestionEt;
	private EditText pwdAnswerEt;
	
	private UserInfoModel userInfo = new UserInfoModel();
	
	private class RegisterTask extends AsyncTask<String, Void, Boolean>{
		private String account;
		private String pwd;
		private String chatAccount;
		private String chatPwd;
		
		@Override
		protected Boolean doInBackground(String... arg0) {
			account = userInfo.getUserLoginName();
			pwd = userInfo.getUserPwd();
			chatAccount = AsyncImageCache.MD5.Md5(account);
			chatPwd = AsyncImageCache.MD5.Md5(pwd);
			try {
				EMChatManager.getInstance().createAccountOnServer(chatAccount, chatPwd);
				EMChatManager.getInstance().login(chatAccount, chatPwd, new EMCallBack() {
					@Override
					public void onError(int arg0, String arg1) {
						
					}
					@Override
					public void onProgress(int arg0, String arg1) {
						
					}
					@Override
					public void onSuccess() {
						Utils.getFriends(RegisterActivity.this);
					}
					
				});
				return true;
			} catch (final Exception e) {
				/*if (e != null && e.getMessage() != null) {
					String errorMsg = e.getMessage();
					if (errorMsg.indexOf("EMNetworkUnconnectedException") != -1) {
						Toast.makeText(getApplicationContext(), "网络异常，请检查网络！", 0).show();
					} else if (errorMsg.indexOf("conflict") != -1) {
						Toast.makeText(getApplicationContext(), "用户已存在！", 0).show();
					} else if (errorMsg.indexOf("not support the capital letters") != -1) {
						Toast.makeText(getApplicationContext(), "用户名不支持大写字母！", 0).show();
					} else {
						Toast.makeText(getApplicationContext(), "注册失败: " + e.getMessage(), 1).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "注册失败: 未知异常", 1).show();
				}*/
				e.printStackTrace();
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){
				Toast.makeText(RegisterActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
				MyApplication.getInstance().setUserName(chatAccount);
				MyApplication.getInstance().setPassword(chatPwd);
				MyApplication.myUserName = account;
				MyApplication.myPwd = pwd;
				MyApplication.myNick = userInfo.getUserName();
				
				Intent mIntent = new Intent();
				mIntent.putExtra("login_success", true);
				setResult(1, mIntent);
				finish();
			}else {
				Toast.makeText(RegisterActivity.this, R.string.register_failed, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_layout);
		initView();
	}
	
	private void initView(){
		setTopTitle(R.string.register);
		accountEt = (EditText) findViewById(R.id.account_et);
		nickEt = (EditText) findViewById(R.id.nick_et);
		pwdEt = (EditText) findViewById(R.id.password_et);
		surePwdEt = (EditText) findViewById(R.id.second_password_et);
		sexGroup = (RadioGroup) findViewById(R.id.sex_rg);
		ageEt = (EditText) findViewById(R.id.age_et);
		phoneEt = (EditText) findViewById(R.id.phone_et);
		mailEt = (EditText) findViewById(R.id.mail_et);
		pwdQuestionEt = (EditText) findViewById(R.id.pwd_question_et);
		pwdAnswerEt = (EditText) findViewById(R.id.pwd_answer_et);
		findViewById(R.id.register_btn).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.register_btn:
			if(checkInput()){
				initUserInfo();
				new RegisterTask().execute();
			}
			break;

		default:
			break;
		}
	}

	private void initUserInfo() {
		userInfo.setUserLoginName(accountEt.getText().toString());
		userInfo.setUserPwd(pwdEt.getText().toString());
		userInfo.setUserName(TextUtils.isEmpty(nickEt.getText()) ? 
				accountEt.getText().toString() : nickEt.getText().toString());
		userInfo.setUserSex(((RadioButton)findViewById(
				sexGroup.getCheckedRadioButtonId())).getText().toString());
		String ageString = ageEt.getText().toString();
		if(!TextUtils.isEmpty(ageString)){
			int age = Integer.parseInt(ageString);
			if( age < 1 || age > 200){
				Toast.makeText(this, R.string.age_error, Toast.LENGTH_SHORT).show();
			}else {
				userInfo.setUserAge(age);
			}
		}
		userInfo.setUserPhone(phoneEt.getText().toString());
		userInfo.setUserEmail(mailEt.getText().toString());
		userInfo.setPwdQuestion(pwdQuestionEt.getText().toString());
		userInfo.setPwdAnswer(pwdAnswerEt.getText().toString());
	}
	
	private boolean checkInput(){
		if(TextUtils.isEmpty(accountEt.getText())){
			Toast.makeText(this, R.string.account_empty, Toast.LENGTH_SHORT).show();
			accountEt.requestFocus();
			accountEt.setSelection(accountEt.getText().length());
			return false;
		}
		if(TextUtils.isEmpty(pwdEt.getText())){
			Toast.makeText(this, R.string.pwd_empty, Toast.LENGTH_SHORT).show();
			pwdEt.requestFocus();
			return false;
		}
		if(TextUtils.isEmpty(surePwdEt.getText())){
			Toast.makeText(this, R.string.pwd_makesure, Toast.LENGTH_SHORT).show();
			surePwdEt.requestFocus();
			return false;
		}
		if(!surePwdEt.getText().toString().equals(pwdEt.getText().toString())){
			Toast.makeText(this, R.string.pwd_diff, Toast.LENGTH_SHORT).show();
			surePwdEt.requestFocus();
			surePwdEt.setSelection(surePwdEt.getText().length());
			return false;
		}
		if(TextUtils.isEmpty(phoneEt.getText())){
			Toast.makeText(this, R.string.phone_empty, Toast.LENGTH_SHORT).show();
			phoneEt.requestFocus();
			return false;
		}
		if(!Utils.validatePhoneNumber(phoneEt.getText().toString())){
			Toast.makeText(this, R.string.phone_error, Toast.LENGTH_SHORT).show();
			phoneEt.requestFocus();
			phoneEt.setSelection(phoneEt.getText().length());
			return false;
		}
		return true;
	}
}
