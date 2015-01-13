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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.easemob.chatuidemo.adapter.ContactAdapter;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.widget.Sidebar;

public class GroupPickContactsActivity extends BaseActivity {
	private ListView listView;
	/** 鏄惁涓轰竴涓柊寤虹殑缇ょ粍 */
	protected boolean isCreatingNewGroup;
	/** 鏄惁涓哄崟閫� */
	private boolean isSignleChecked;
	private PickContactAdapter contactAdapter;
	/** group涓竴寮�濮嬪氨鏈夌殑鎴愬憳 */
	private List<String> exitingMembers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_pick_contacts);

		// String groupName = getIntent().getStringExtra("groupName");
		String groupId = getIntent().getStringExtra("groupId");
		if (groupId == null) {// 鍒涘缓缇ょ粍
			isCreatingNewGroup = true;
		} else {
			// 鑾峰彇姝ょ兢缁勭殑鎴愬憳鍒楄〃
			EMGroup group = EMGroupManager.getInstance().getGroup(groupId);
			exitingMembers = group.getMembers();
		}
		if(exitingMembers == null)
			exitingMembers = new ArrayList<String>();
		// 鑾峰彇濂藉弸鍒楄〃
		final List<User> alluserList = new ArrayList<User>();
		for (User user : MyApplication.getInstance().getContactList().values()) {
			if (!user.getUsername().equals(Constant.NEW_FRIENDS_USERNAME) & !user.getUsername().equals(Constant.GROUP_USERNAME))
				alluserList.add(user);
		}
		// 瀵筶ist杩涜鎺掑簭
		Collections.sort(alluserList, new Comparator<User>() {
			@Override
			public int compare(User lhs, User rhs) {
				return (lhs.getUsername().compareTo(rhs.getUsername()));

			}
		});

		listView = (ListView) findViewById(R.id.list);
		contactAdapter = new PickContactAdapter(this, R.layout.row_contact_with_checkbox, alluserList);
		listView.setAdapter(contactAdapter);
		((Sidebar) findViewById(R.id.sidebar)).setListView(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
				checkBox.toggle();

			}
		});
	}

	/**
	 * 纭閫夋嫨鐨刴embers
	 * 
	 * @param v
	 */
	public void save(View v) {
		setResult(RESULT_OK, new Intent().putExtra("newmembers", getToBeAddMembers().toArray(new String[0])));
		finish();
	}

	/**
	 * 鑾峰彇瑕佽娣诲姞鐨勬垚鍛�
	 * 
	 * @return
	 */
	private List<String> getToBeAddMembers() {
		List<String> members = new ArrayList<String>();
		int length = contactAdapter.isCheckedArray.length;
		for (int i = 0; i < length; i++) {
			String username = contactAdapter.getItem(i + 1).getUsername();
			if (contactAdapter.isCheckedArray[i] && !exitingMembers.contains(username)) {
				members.add(username);
			}
		}

		return members;
	}

	/**
	 * adapter
	 */
	private class PickContactAdapter extends ContactAdapter {

		private boolean[] isCheckedArray;

		public PickContactAdapter(Context context, int resource, List<User> users) {
			super(context, resource, users, null);
			isCheckedArray = new boolean[users.size()];
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			if (position > 0) {
				final String username = getItem(position).getUsername();
				// 閫夋嫨妗哻heckbox
				final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
				if(exitingMembers != null && exitingMembers.contains(username)){
					checkBox.setButtonDrawable(R.drawable.checkbox_bg_gray_selector);
				}else{
					checkBox.setButtonDrawable(R.drawable.checkbox_bg_selector);
				}
				if (checkBox != null) {
					// checkBox.setOnCheckedChangeListener(null);

					checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							// 缇ょ粍涓師鏉ョ殑鎴愬憳涓�鐩磋涓洪�変腑鐘舵��
							if (exitingMembers.contains(username)) {
									isChecked = true;
									checkBox.setChecked(true);
							}
							isCheckedArray[position - 1] = isChecked;
							//濡傛灉鏄崟閫夋ā寮�
							if (isSignleChecked && isChecked) {
								for (int i = 0; i < isCheckedArray.length; i++) {
									if (i != position - 1) {
										isCheckedArray[i] = false;
									}
								}
								contactAdapter.notifyDataSetChanged();
							}
							
						}
					});
					// 缇ょ粍涓師鏉ョ殑鎴愬憳涓�鐩磋涓洪�変腑鐘舵��
					if (exitingMembers.contains(username)) {
							checkBox.setChecked(true);
							isCheckedArray[position - 1] = true;
					} else {
						checkBox.setChecked(isCheckedArray[position - 1]);
					}
				}
			}
			return view;
		}
	}

	public void back(View view){
		finish();
	}
	
}
