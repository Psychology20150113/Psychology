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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;;

public class AddContactActivity extends BaseActivity{
	private EditText editText;
	private LinearLayout searchedUserLayout;
	private TextView nameText;
	private Button searchBtn;
	private ImageView avatar;
	private InputMethodManager inputMethodManager;
	private String toAddUsername;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		
		editText = (EditText) findViewById(R.id.edit_note);
		searchedUserLayout = (LinearLayout) findViewById(R.id.ll_user);
		nameText = (TextView) findViewById(R.id.name);
		searchBtn = (Button) findViewById(R.id.search);
		avatar = (ImageView) findViewById(R.id.avatar);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	
	/**
	 * 鏌ユ壘contact
	 * @param v
	 */
	public void searchContact(View v) {
		final String name = editText.getText().toString();
		String saveText = searchBtn.getText().toString();
		
		if (getString(R.string.button_search).equals(saveText)) {
			toAddUsername = name;
			if(TextUtils.isEmpty(name)) {
				startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "璇疯緭鍏ョ敤鎴峰悕"));
				return;
			}
			
			// TODO 浠庢湇鍔″櫒鑾峰彇姝ontact,濡傛灉涓嶅瓨鍦ㄦ彁绀轰笉瀛樺湪姝ょ敤鎴�
			
			//鏈嶅姟鍣ㄥ瓨鍦ㄦ鐢ㄦ埛锛屾樉绀烘鐢ㄦ埛鍜屾坊鍔犳寜閽�
			searchedUserLayout.setVisibility(View.VISIBLE);
			nameText.setText(toAddUsername);
			
		} 
	}	
	
	/**
	 *  娣诲姞contact
	 * @param view
	 */
	public void addContact(View view){
		if(MyApplication.getInstance().getUserName().equals(nameText.getText().toString())){
			startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "涓嶈兘娣诲姞鑷繁"));
			return;
		}
		
		if(MyApplication.getInstance().getContactList().containsKey(nameText.getText().toString())){
			startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "姝ょ敤鎴峰凡鏄綘鐨勫ソ鍙�"));
			return;
		}
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("姝ｅ湪鍙戦�佽姹�...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		new Thread(new Runnable() {
			public void run() {
				
				try {
					//demo鍐欐浜嗕釜reason锛屽疄闄呭簲璇ヨ鐢ㄦ埛鎵嬪姩濉叆
					EMContactManager.getInstance().addContact(toAddUsername, "鍔犱釜濂藉弸鍛�");
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(getApplicationContext(), "鍙戦�佽姹傛垚鍔�,绛夊緟瀵规柟楠岃瘉", 1).show();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(getApplicationContext(), "璇锋眰娣诲姞濂藉弸澶辫触:" + e.getMessage(), 1).show();
						}
					});
				}
			}
		}).start();
	}
	
	public void back(View v) {
		finish();
	}
}
