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
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.easemob.chat.EMGroupManager;
import com.dcy.psychology.R;

public class NewGroupActivity extends BaseActivity {
	private EditText groupNameEditText;
	private ProgressDialog progressDialog;
	private EditText introductionEditText;
	private CheckBox checkBox;
	private CheckBox memberCheckbox;
	private LinearLayout openInviteContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_group);
		groupNameEditText = (EditText) findViewById(R.id.edit_group_name);
		introductionEditText = (EditText) findViewById(R.id.edit_group_introduction);
		checkBox = (CheckBox) findViewById(R.id.cb_public);
		memberCheckbox = (CheckBox) findViewById(R.id.cb_member_inviter);
		openInviteContainer = (LinearLayout) findViewById(R.id.ll_open_invite);
		
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					openInviteContainer.setVisibility(View.INVISIBLE);
				}else{
					openInviteContainer.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	/**
	 * @param v
	 */
	public void save(View v) {
		String name = groupNameEditText.getText().toString();
		if (TextUtils.isEmpty(name)) {
			Intent intent = new Intent(this, AlertDialog.class);
			intent.putExtra("msg", "缇ょ粍鍚嶇О涓嶈兘涓虹┖");
			startActivity(intent);
		} else {
			// 杩涢�氳褰曢�変汉
			startActivityForResult(new Intent(this, GroupPickContactsActivity.class).putExtra("groupName", name), 0);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			//鏂板缓缇ょ粍
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("姝ｅ湪鍒涘缓缇よ亰...");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

			new Thread(new Runnable() {
				@Override
				public void run() {
					// 璋冪敤sdk鍒涘缓缇ょ粍鏂规硶
					String groupName = groupNameEditText.getText().toString().trim();
					String desc = introductionEditText.getText().toString();
					String[] members = data.getStringArrayExtra("newmembers");
					try {
						if(checkBox.isChecked()){
							//鍒涘缓鍏紑缇わ紝姝ょ鏂瑰紡鍒涘缓鐨勭兢锛屽彲浠ヨ嚜鐢卞姞鍏�
//							EMGroupManager.getInstance().createPublicGroup(groupName, desc, members, false);
							//鍒涘缓鍏紑缇わ紝姝ょ鏂瑰紡鍒涘缓鐨勭兢锛岀敤鎴烽渶瑕佺敵璇凤紝绛夌兢涓诲悓鎰忓悗鎵嶈兘鍔犲叆姝ょ兢
							EMGroupManager.getInstance().createPublicGroup(groupName, desc, members, true);
						}else{
							//鍒涘缓涓嶅叕寮�缇�
							EMGroupManager.getInstance().createPrivateGroup(groupName, desc, members, memberCheckbox.isChecked());
						}
						runOnUiThread(new Runnable() {
							public void run() {
								progressDialog.dismiss();
								setResult(RESULT_OK);
								finish();
							}
						});
					} catch (final Exception e) {
						runOnUiThread(new Runnable() {
							public void run() {
								progressDialog.dismiss();
								Toast.makeText(NewGroupActivity.this, "鍒涘缓缇ょ粍澶辫触:" + e.getLocalizedMessage(), 1).show();
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
