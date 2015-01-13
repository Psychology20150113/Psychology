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
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupInfo;
import com.easemob.chat.EMGroupManager;
import com.dcy.psychology.R;
import com.easemob.exceptions.EaseMobException;

public class GroupSimpleDetailActivity extends BaseActivity {
	private Button btn_add_group;
	private TextView tv_admin;
	private TextView tv_name;
	private TextView tv_introduction;
	private EMGroup group;
	private String groupid;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_simle_details);
		tv_name = (TextView) findViewById(R.id.name);
		tv_admin = (TextView) findViewById(R.id.tv_admin);
		btn_add_group = (Button) findViewById(R.id.btn_add_to_group);
		tv_introduction = (TextView) findViewById(R.id.tv_introduction);
		progressBar = (ProgressBar) findViewById(R.id.loading);

		EMGroupInfo groupInfo = (EMGroupInfo) getIntent().getSerializableExtra("groupinfo");
		String groupname = groupInfo.getGroupName();
		groupid = groupInfo.getGroupId();
		
		tv_name.setText(groupname);
		
		
		new Thread(new Runnable() {

			public void run() {
				//浠庢湇鍔″櫒鑾峰彇璇︽儏
				try {
					group = EMGroupManager.getInstance().getGroupFromServer(groupid);
					runOnUiThread(new Runnable() {
						public void run() {
							progressBar.setVisibility(View.INVISIBLE);
							//鑾峰彇璇︽儏鎴愬姛锛屽苟涓旇嚜宸变笉鍦ㄧ兢涓紝鎵嶈鍔犲叆缇よ亰鎸夐挳鍙偣鍑�
							if(!group.getMembers().contains(EMChatManager.getInstance().getCurrentUser()))
								btn_add_group.setEnabled(true);
							tv_name.setText(group.getGroupName());
							tv_admin.setText(group.getOwner());
							tv_introduction.setText(group.getDescription());
						}
					});
				} catch (final EaseMobException e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						public void run() {
							progressBar.setVisibility(View.INVISIBLE);
							Toast.makeText(GroupSimpleDetailActivity.this, "鑾峰彇缇よ亰淇℃伅澶辫触: "+e.getMessage(), 1).show();
						}
					});
				}
				
			}
		}).start();
		
	}
	
	//鍔犲叆缇よ亰
	public void addToGroup(View view){
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage("姝ｅ湪鍙戦�佽姹�...");
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					//濡傛灉鏄痬embersOnly鐨勭兢锛岄渶瑕佺敵璇峰姞鍏ワ紝涓嶈兘鐩存帴join
					if(group.isMembersOnly()){
						EMGroupManager.getInstance().applyJoinToGroup(groupid, "姹傚姞鍏�");
					}else{
						EMGroupManager.getInstance().joinGroup(groupid);
					}
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(GroupSimpleDetailActivity.this, "鍔犲叆缇よ亰鎴愬姛", 0).show();
							btn_add_group.setEnabled(false);
						}
					});
				} catch (final EaseMobException e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(GroupSimpleDetailActivity.this, "鍔犲叆缇よ亰澶辫触锛�"+e.getMessage(), 0).show();
						}
					});
				}
			}
		}).start();
	}
	
	public void back(View view){
		finish();
	}
}
