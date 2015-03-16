package com.dcy.psychology;

import com.dcy.psychology.gsonbean.LoginBean;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.IMManager;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.Utils;
import com.dcy.psychology.view.CustomProgressDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
			return Utils.getLoginWeb(accountET.getText().toString(), pwdET.getText().toString());
		}
		
		@Override
		protected void onPostExecute(LoginBean result) {
			hideCustomDialog();
			if("NO".equals(result.getResult())){
				Toast.makeText(LoginActivity.this, result.getReason(), Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
				InfoShared mShared = new InfoShared(LoginActivity.this);
				mShared.saveInfo(accountET.getText().toString(), pwdET.getText().toString(), result.getLoginState());
				Intent mIntent = new Intent();
				mIntent.putExtra("login_success", true);
				setResult(0, mIntent);
				finish();
			}
		}
	} 

	public static class ChatLoginTask extends AsyncTask<String, Void, Boolean>{
		private Context mContext;
		private Runnable finishTask;
		
		public ChatLoginTask(Context context) {
			mContext = context;
		}
		
		public void setRunnable(Runnable runnable){
			this.finishTask = runnable;
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			if(TextUtils.isEmpty(params[0]) || TextUtils.isEmpty(params[1]))
				return false;
			return IMManager.getInstance().loginIM(params[0], params[1]);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(!result)
				Toast.makeText(mContext, R.string.login_chat_failed, Toast.LENGTH_SHORT).show();
			if(finishTask != null)
				finishTask.run();
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
