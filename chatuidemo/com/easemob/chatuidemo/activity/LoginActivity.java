/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;

/**
 * 鐧婚檰椤甸潰
 * 
 */
public class LoginActivity extends BaseActivity {
	public static final int REQUEST_CODE_SETNICK=1;
	private EditText usernameEditText;
	private EditText passwordEditText;

	private boolean progressShow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		// 濡傛灉鐢ㄦ埛鍚嶅瘑鐮侀兘鏈夛紝鐩存帴杩涘叆涓婚〉闈�
		if (MyApplication.getInstance().getUserName() != null && MyApplication.getInstance().getPassword() != null) {
			startActivity(new Intent(this, ChatMainActivity.class));
			finish();
		}
		// 濡傛灉鐢ㄦ埛鍚嶆敼鍙橈紝娓呯┖瀵嗙爜
		usernameEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				passwordEditText.setText(null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	/**
	 * 鐧婚檰
	 * 
	 * @param view
	 */
	public void login(View view) {
		if (!CommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent=new Intent(LoginActivity.this, com.easemob.chatuidemo.activity.AlertDialog.class);
		intent.putExtra("editTextShow", true);
		intent.putExtra("titleIsCancel",true);
		intent.putExtra("msg", "璇疯缃綋鍓嶇敤鎴风殑鏄电О\n涓轰簡ios绂荤嚎鎺ㄩ�佷笉鏄痷serid鑰屾槸nick锛岃鎯呰娉ㄩ噴");
		startActivityForResult(intent, REQUEST_CODE_SETNICK);
		 
		
	}

	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK)
		{
			if(requestCode==REQUEST_CODE_SETNICK)
			{
				MyApplication.currentUserNick=data.getStringExtra("edittext");
				
				final String username = usernameEditText.getText().toString();
				final String password = passwordEditText.getText().toString();
				
				if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
					progressShow = true;
					final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
					pd.setCanceledOnTouchOutside(false);
					pd.setOnCancelListener(new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							progressShow = false;
						}
					});
					pd.setMessage("姝ｅ湪鐧婚檰...");
					pd.show();
					// 璋冪敤sdk鐧婚檰鏂规硶鐧婚檰鑱婂ぉ鏈嶅姟鍣�
					EMChatManager.getInstance().login(username, password, new EMCallBack() {

						@Override
						public void onSuccess() {
							if (!progressShow) {
								return;
							}
							MyApplication.getInstance().setUserName(username);
							MyApplication.getInstance().setPassword(password);
							runOnUiThread(new Runnable() {
								public void run() {
									pd.setMessage("姝ｅ湪鑾峰彇濂藉弸鍜岀兢鑱婂垪琛�...");
								}
							});
							try {
								List<String> usernames = EMChatManager.getInstance().getContactUserNames();
								Map<String, User> userlist = new HashMap<String, User>();
								for (String username : usernames) {
									User user = new User();
									user.setUsername(username);
									setUserHearder(username, user);
									userlist.put(username, user);
								}
								User newFriends = new User();
								newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
								newFriends.setNick("鐢宠涓庨�氱煡");
								newFriends.setHeader("");
								userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
								User groupUser = new User();
								groupUser.setUsername(Constant.GROUP_USERNAME);
								groupUser.setNick("缇よ亰");
								groupUser.setHeader("");
								userlist.put(Constant.GROUP_USERNAME, groupUser);

								MyApplication.getInstance().setContactList(userlist);
								UserDao dao = new UserDao(LoginActivity.this);
								List<User> users = new ArrayList<User>(userlist.values());
								dao.saveContactList(users);

								EMGroupManager.getInstance().getGroupsFromServer();
							} catch (Exception e) {
								e.printStackTrace();
							}
									boolean updatenick = EMChatManager.getInstance()
											.updateCurrentUserNick(MyApplication.currentUserNick);
									if (!updatenick) {
										EMLog.e("LoginActivity",
												"update current user nick fail");
									}

							if (!LoginActivity.this.isFinishing())
								pd.dismiss();
							// 杩涘叆涓婚〉闈�
							startActivity(new Intent(LoginActivity.this, ChatMainActivity.class));
							finish();
						}

						@Override
						public void onProgress(int progress, String status) {

						}

						@Override
						public void onError(int code, final String message) {
							if (!progressShow) {
								return;
							}
							runOnUiThread(new Runnable() {
								public void run() {
									pd.dismiss();
									Toast.makeText(getApplicationContext(), "鐧诲綍澶辫触: " + message, 0).show();

								}
							});
						}
					});
				}
				
				
				
			}
			
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 娉ㄥ唽
	 * 
	 * @param view
	 */
	public void register(View view) {
		startActivityForResult(new Intent(this, RegisterActivity.class), 0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (MyApplication.getInstance().getUserName() != null) {
			usernameEditText.setText(MyApplication.getInstance().getUserName());
		}
	}

	/**
	 * 璁剧疆hearder灞炴�э紝鏂逛究閫氳涓鑱旂郴浜烘寜header鍒嗙被鏄剧ず锛屼互鍙婇�氳繃鍙充晶ABCD...瀛楁瘝鏍忓揩閫熷畾浣嶈仈绯讳汉
	 * 
	 * @param username
	 * @param user
	 */
	protected void setUserHearder(String username, User user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}
}
