package com.dcy.psychology;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.LoginBean;
import com.dcy.psychology.util.Constants;
import com.dcy.psychology.util.IMManager;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.Utils;

public class LoginActivity extends Activity implements OnClickListener{
	private EditText accountET;
	private EditText pwdET;
	private String userRole;
	private ImageView backview;
	
	private class LoginTask extends AsyncTask<Void, Void, LoginBean>{
		@Override
		protected LoginBean doInBackground(Void... arg0) {
			return Utils.getLoginWeb(accountET.getText().toString(), pwdET.getText().toString());
		}
		
		@Override
		protected void onPostExecute(LoginBean result) {
			//hideCustomDialog();
			if("NO".equals(result.getResult())){
				Toast.makeText(LoginActivity.this, result.getReason(), Toast.LENGTH_SHORT).show();
			}else {
				InfoShared mShared = new InfoShared(LoginActivity.this);
				if(userRole != null && !Constants.RoleUser.equals(userRole)){
					if(Constants.RoleUser.equals(result.getLoginState())){
						Toast.makeText(LoginActivity.this, R.string.error_role, Toast.LENGTH_SHORT).show();
						return;
					} else {
						mShared.setLastIsDoctor(true);
					}
				}
				Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
				mShared.savePhoneInfo(accountET.getText().toString(), pwdET.getText().toString(), result.getLoginState(), result.isIsPrefectUserInfo());
				if(!TextUtils.isEmpty(result.HXPWD)){
					mShared.setHxPwd(result.HXPWD);
					MyApplication.getInstance().getChatManager().chatLogin(MyApplication.myPhoneNum, result.HXPWD);
				}
//				new ChatLoginTask(LoginActivity.this).execute(MyApplication.myPhoneNum, MyApplication.myPwd);
				Intent mIntent = new Intent(LoginActivity.this,SlideMainActivity.class);
				mIntent.putExtra("login_success", true);
				setResult(0, mIntent);
				
				finish();
				startActivityForResult(mIntent, 0);
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
			mContext.sendBroadcast(new Intent(Constants.ReceiverAction_LoginSuccess));
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); //璁剧疆鏃犳爣棰�
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login_layout);
		userRole = getIntent().getStringExtra(Constants.UserRole);
		initView();
	}
	
	private void initView(){
		//setTopTitle(R.string.login);
		/*if(userRole == null || Constants.RoleUser.equals(userRole)){
			setRightText(R.string.register);
		}*/
		accountET = (EditText) findViewById(R.id.account_et);
		pwdET = (EditText) findViewById(R.id.psw_et); 
		findViewById(R.id.login_btn).setOnClickListener(this);
		findViewById(R.id.register_btn).setOnClickListener(this);
		findViewById(R.id.find_pwd_tv).setOnClickListener(this);
		backview=(ImageView) findViewById(R.id.iv_back);
		backview.setImageResource(R.drawable.icon_orangeback);
		backview.setOnClickListener(this);
	}
	
	/*@Override
	public void onRightTextClick() {
		//startActivityForResult(new Intent(this, RegisterActivity.class) , 0);
		startActivityForResult(new Intent(this, PhoneRegisterActivity.class) , 0);
	}*/
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.login_btn:
			if(checkInput()){
				//showCustomDialog();
				new LoginTask().execute();
			}
			break;
		case R.id.find_pwd_tv:
			startActivity(new Intent(this, FindPwdActivity.class));
			break;
		case R.id.register_btn:
			startActivity(new Intent(this, PhoneRegisterActivity.class));
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
