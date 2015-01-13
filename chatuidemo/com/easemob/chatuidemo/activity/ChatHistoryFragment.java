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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.GroupChangeListener;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.easemob.chatuidemo.adapter.ChatHistoryAdapter;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.domain.User;

/**
 * 鑱婂ぉ璁板綍Fragment
 * 
 */
public class ChatHistoryFragment extends Fragment {

	private InputMethodManager inputMethodManager;
	private ListView listView;
	private Map<String, User> contactList;
	private ChatHistoryAdapter adapter;
	private EditText query;
	private ImageButton clearSearch;
	public RelativeLayout errorItem;
	public TextView errorText;
	private boolean hidden;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_conversation_history, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		errorItem = (RelativeLayout) getView().findViewById(R.id.rl_error_item);
		errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);
		// contact list
		contactList = MyApplication.getInstance().getContactList();
		listView = (ListView) getView().findViewById(R.id.list);
		adapter = new ChatHistoryAdapter(getActivity(), 1, loadUsersWithRecentChat());
		// 璁剧疆adapter
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				EMContact emContact = adapter.getItem(position);
				if (adapter.getItem(position).getUsername().equals(MyApplication.getInstance().getUserName()))
					Toast.makeText(getActivity(), "涓嶈兘鍜岃嚜宸辫亰澶�", 0).show();
				else {
					// 杩涘叆鑱婂ぉ椤甸潰
					  Intent intent = new Intent(getActivity(), ChatActivity.class);
					 if (emContact instanceof EMGroup) {
		                    //it is group chat
		                    intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
		                    intent.putExtra("groupId", ((EMGroup) emContact).getGroupId());
		                } else {
		                    //it is single chat
		                    intent.putExtra("userId", emContact.getUsername());
		                } 
					startActivity(intent);
				}
			}
		});
		// 娉ㄥ唽涓婁笅鏂囪彍鍗�
		registerForContextMenu(listView);

		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 闅愯棌杞敭鐩�
				if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getActivity().getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});
		// 鎼滅储妗�
		query = (EditText) getView().findViewById(R.id.query);
		// 鎼滅储妗嗕腑娓呴櫎button
		clearSearch = (ImageButton) getView().findViewById(R.id.search_clear);
		query.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				adapter.getFilter().filter(s);
				if (s.length() > 0) {
					clearSearch.setVisibility(View.VISIBLE);
				} else {
					clearSearch.setVisibility(View.INVISIBLE);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
		clearSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				query.getText().clear();

			}
		});

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// if(((AdapterContextMenuInfo)menuInfo).position > 0){ m,
		getActivity().getMenuInflater().inflate(R.menu.delete_message, menu);
		// }
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_message) {
			EMContact tobeDeleteUser = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			boolean isGroup = false;
			if(tobeDeleteUser instanceof EMGroup)
				isGroup = true;
			// 鍒犻櫎姝や細璇�
			EMChatManager.getInstance().deleteConversation(tobeDeleteUser.getUsername(),isGroup);
			InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
			inviteMessgeDao.deleteMessage(tobeDeleteUser.getUsername());
			adapter.remove(tobeDeleteUser);
			adapter.notifyDataSetChanged();

			// 鏇存柊娑堟伅鏈鏁�
			((ChatMainActivity) getActivity()).updateUnreadLabel();

			return true;
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * 鍒锋柊椤甸潰
	 */
	public void refresh() {
		adapter = new ChatHistoryAdapter(getActivity(), R.layout.row_chat_history, loadUsersWithRecentChat());
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	
	
	/**
	 * 鑾峰彇鏈夎亰澶╄褰曠殑users鍜実roups
	 * 
	 * @param context
	 * @return
	 */
	private List<EMContact> loadUsersWithRecentChat() {
		List<EMContact> resultList = new ArrayList<EMContact>();
		//鑾峰彇鏈夎亰澶╄褰曠殑users锛屼笉鍖呮嫭闄岀敓浜�
		for (User user : contactList.values()) {
			EMConversation conversation = EMChatManager.getInstance().getConversation(user.getUsername());
			if (conversation.getMsgCount() > 0) {
				resultList.add(user);
			}
		}
		for(EMGroup group : EMGroupManager.getInstance().getAllGroups()){
			EMConversation conversation = EMChatManager.getInstance().getConversation(group.getGroupId());
			if(conversation.getMsgCount() > 0){
				resultList.add(group);
			}
			
		}
		
		// 鎺掑簭
		sortUserByLastChatTime(resultList);
		return resultList;
	}

	/**
	 * 鏍规嵁鏈�鍚庝竴鏉℃秷鎭殑鏃堕棿鎺掑簭
	 * 
	 * @param usernames
	 */
	private void sortUserByLastChatTime(List<EMContact> contactList) {
		Collections.sort(contactList, new Comparator<EMContact>() {
			@Override
			public int compare(final EMContact user1, final EMContact user2) {
				EMConversation conversation1 = EMChatManager.getInstance().getConversation(user1.getUsername());
				EMConversation conversation2 = EMChatManager.getInstance().getConversation(user2.getUsername());

				EMMessage user2LastMessage = conversation2.getLastMessage();
				EMMessage user1LastMessage = conversation1.getLastMessage();
				if (user2LastMessage.getMsgTime() == user1LastMessage.getMsgTime()) {
					return 0;
				} else if (user2LastMessage.getMsgTime() > user1LastMessage.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			refresh();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden) {
			refresh();
		}
	}
	
	
}
