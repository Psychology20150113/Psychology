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

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.chat.EMChatConfig;
import com.easemob.chat.EMChatManager;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.easemob.exceptions.EMNetworkUnconnectedException;
import com.easemob.exceptions.EaseMobException;

/**
 * 娉ㄥ唽椤�
 * 
 */
public class RegisterActivity extends BaseActivity {
	private EditText userNameEditText;
	private EditText passwordEditText;
	private EditText confirmPwdEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		userNameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		confirmPwdEditText = (EditText) findViewById(R.id.confirm_password);
	}

	/**
	 * 娉ㄥ唽
	 * 
	 * @param view
	 */
	public void register(View view) {
		final String username = userNameEditText.getText().toString().trim();
		final String pwd = passwordEditText.getText().toString().trim();
		String confirm_pwd = confirmPwdEditText.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, "鐢ㄦ埛鍚嶄笉鑳戒负绌猴紒", Toast.LENGTH_SHORT).show();
			userNameEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(this, "瀵嗙爜涓嶈兘涓虹┖锛�", Toast.LENGTH_SHORT).show();
			passwordEditText.requestFocus();
			return;
		} else if (TextUtils.isEmpty(confirm_pwd)) {
			Toast.makeText(this, "纭瀵嗙爜涓嶈兘涓虹┖锛�", Toast.LENGTH_SHORT).show();
			confirmPwdEditText.requestFocus();
			return;
		} else if (!pwd.equals(confirm_pwd)) {
			Toast.makeText(this, "涓ゆ杈撳叆鐨勫瘑鐮佷笉涓�鑷达紝璇烽噸鏂拌緭鍏ワ紒", Toast.LENGTH_SHORT).show();
			return;
		}

		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
			final ProgressDialog pd = new ProgressDialog(this);
			pd.setMessage("姝ｅ湪娉ㄥ唽...");
			pd.show();
			new Thread(new Runnable() {
				public void run() {
					try {
						// 璋冪敤sdk娉ㄥ唽鏂规硶
						EMChatManager.getInstance().createAccountOnServer(username, pwd);
						runOnUiThread(new Runnable() {
							public void run() {
								if (!RegisterActivity.this.isFinishing())
									pd.dismiss();
								// 淇濆瓨鐢ㄦ埛鍚�
								MyApplication.getInstance().setUserName(username);
								Toast.makeText(getApplicationContext(), "娉ㄥ唽鎴愬姛", 0).show();
								finish();
							}
						});
					} catch (final Exception e) {
						runOnUiThread(new Runnable() {
							public void run() {
								if (!RegisterActivity.this.isFinishing())
									pd.dismiss();
								if (e != null && e.getMessage() != null) {
									String errorMsg = e.getMessage();
									if (errorMsg.indexOf("EMNetworkUnconnectedException") != -1) {
										Toast.makeText(getApplicationContext(), "缃戠粶寮傚父锛岃妫�鏌ョ綉缁滐紒", 0).show();
									} else if (errorMsg.indexOf("conflict") != -1) {
										Toast.makeText(getApplicationContext(), "鐢ㄦ埛宸插瓨鍦紒", 0).show();
									}/* else if (errorMsg.indexOf("not support the capital letters") != -1) {
										Toast.makeText(getApplicationContext(), "鐢ㄦ埛鍚嶄笉鏀寔澶у啓瀛楁瘝锛�", 0).show();
									} */else {
										Toast.makeText(getApplicationContext(), "娉ㄥ唽澶辫触: " + e.getMessage(), 1).show();
									}

								} else {
									Toast.makeText(getApplicationContext(), "娉ㄥ唽澶辫触: 鏈煡寮傚父", 1).show();

								}
							}
						});
					}
				}
			}).start();

		}
	}

	public void back(View view) {
		finish();
	}

}
