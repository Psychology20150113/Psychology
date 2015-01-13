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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.Constant;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.InviteMessage;
import com.easemob.chatuidemo.domain.InviteMessage.InviteMesageStatus;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.easemob.util.HanziToPinyin;
import com.easemob.util.NetUtils;

public class ChatMainActivity extends FragmentActivity {

	protected static final String TAG = "ChatMainActivity";
	// 鏈娑堟伅textview
	private TextView unreadLabel;
	// 鏈閫氳褰晅extview
	private TextView unreadAddressLable;

	private Button[] mTabs;
	private ContactlistFragment contactListFragment;
//	private ChatHistoryFragment chatHistoryFragment;
	private ChatAllHistoryFragment chatHistoryFragment;
	private SettingsFragment settingFragment;
	private Fragment[] fragments;
	private int index;
	private RelativeLayout[] tab_containers;
	// 褰撳墠fragment鐨刬ndex
	private int currentTabIndex;
	private NewMessageBroadcastReceiver msgReceiver;
	// 璐﹀彿鍦ㄥ埆澶勭櫥褰�
	private boolean isConflict = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_main);
		initView();
		inviteMessgeDao = new InviteMessgeDao(this);
		userDao = new UserDao(this);
		//杩欎釜fragment鍙樉绀哄ソ鍙嬪拰缇ょ粍鐨勮亰澶╄褰�
//		chatHistoryFragment = new ChatHistoryFragment();
		//鏄剧ず鎵�鏈変汉娑堟伅璁板綍鐨刦ragment
		chatHistoryFragment = new ChatAllHistoryFragment();
		contactListFragment = new ContactlistFragment();
		settingFragment = new SettingsFragment();
		fragments = new Fragment[] { chatHistoryFragment, contactListFragment, settingFragment };
		// 娣诲姞鏄剧ず绗竴涓猣ragment
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, chatHistoryFragment)
				.add(R.id.fragment_container, contactListFragment).hide(contactListFragment).show(chatHistoryFragment)
				.commit();

		// 娉ㄥ唽涓�涓帴鏀舵秷鎭殑BroadcastReceiver
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);

		// 娉ㄥ唽涓�涓猘ck鍥炴墽娑堟伅鐨凚roadcastReceiver
		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance()
				.getAckMessageBroadcastAction());
		ackMessageIntentFilter.setPriority(3);
		registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

		// 娉ㄥ唽涓�涓绾挎秷鎭殑BroadcastReceiver
		IntentFilter offlineMessageIntentFilter = new IntentFilter(EMChatManager.getInstance()
				.getOfflineMessageBroadcastAction());
		registerReceiver(offlineMessageReceiver, offlineMessageIntentFilter);
		
		// setContactListener鐩戝惉鑱旂郴浜虹殑鍙樺寲绛�
		EMContactManager.getInstance().setContactListener(new MyContactListener());
		// 娉ㄥ唽涓�涓洃鍚繛鎺ョ姸鎬佺殑listener
		EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
		// 娉ㄥ唽缇よ亰鐩稿叧鐨刲istener
		EMGroupManager.getInstance().addGroupChangeListener(new MyGroupChangeListener());
		// 閫氱煡sdk锛孶I 宸茬粡鍒濆鍖栧畬姣曪紝娉ㄥ唽浜嗙浉搴旂殑receiver鍜宭istener, 鍙互鎺ュ彈broadcast浜�
		EMChat.getInstance().setAppInited();
	}

	/**
	 * 鍒濆鍖栫粍浠�
	 */
	private void initView() {
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
		unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
		mTabs = new Button[3];
		mTabs[0] = (Button) findViewById(R.id.btn_conversation);
		mTabs[1] = (Button) findViewById(R.id.btn_address_list);
		mTabs[2] = (Button) findViewById(R.id.btn_setting);
		// 鎶婄涓�涓猼ab璁句负閫変腑鐘舵��
		mTabs[0].setSelected(true);

	}

	/**
	 * button鐐瑰嚮浜嬩欢
	 * 
	 * @param view
	 */
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.btn_conversation:
			index = 0;
			break;
		case R.id.btn_address_list:
			index = 1;
			break;
		case R.id.btn_setting:
			index = 2;
			break;
		}
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if (!fragments[index].isAdded()) {
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		// 鎶婂綋鍓峵ab璁句负閫変腑鐘舵��
		mTabs[index].setSelected(true);
		currentTabIndex = index;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 娉ㄩ攢骞挎挱鎺ユ敹鑰�
		try {
			unregisterReceiver(msgReceiver);
		} catch (Exception e) {
		}
		try {
			unregisterReceiver(ackMessageReceiver);
		} catch (Exception e) {
		}
		try {
			unregisterReceiver(offlineMessageReceiver);
		} catch (Exception e) {
		}

		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}

	}

	/**
	 * 鍒锋柊鏈娑堟伅鏁�
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			unreadLabel.setText(String.valueOf(count));
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 鍒锋柊鐢宠涓庨�氱煡娑堟伅鏁�
	 */
	public void updateUnreadAddressLable() {
		runOnUiThread(new Runnable() {
			public void run() {
				int count = getUnreadAddressCountTotal();
				if (count > 0) {
					unreadAddressLable.setText(String.valueOf(count));
					unreadAddressLable.setVisibility(View.VISIBLE);
				} else {
					unreadAddressLable.setVisibility(View.INVISIBLE);
				}
			}
		});

	}

	/**
	 * 鑾峰彇鏈鐢宠涓庨�氱煡娑堟伅
	 * 
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		if (MyApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME) != null)
			unreadAddressCountTotal = MyApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME)
					.getUnreadMsgCount();
		return unreadAddressCountTotal;
	}

	/**
	 * 鑾峰彇鏈娑堟伅鏁�
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}

	/**
	 * 鏂版秷鎭箍鎾帴鏀惰��
	 * 
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//涓婚〉闈㈡敹鍒版秷鎭悗锛屼富瑕佷负浜嗘彁绀烘湭璇伙紝瀹為檯娑堟伅鍐呭闇�瑕佸埌chat椤甸潰鏌ョ湅
			
			// 娑堟伅id
			String msgId = intent.getStringExtra("msgid");
			// 鏀跺埌杩欎釜骞挎挱鐨勬椂鍊欙紝message宸茬粡鍦╠b鍜屽唴瀛橀噷浜嗭紝鍙互閫氳繃id鑾峰彇mesage瀵硅薄
			// EMMessage message =
			// EMChatManager.getInstance().getMessage(msgId);

			// 鍒锋柊bottom bar娑堟伅鏈鏁�
			updateUnreadLabel();
			if (currentTabIndex == 0) {
				// 褰撳墠椤甸潰濡傛灉涓鸿亰澶╁巻鍙查〉闈紝鍒锋柊姝ら〉闈�
				if (chatHistoryFragment != null) {
					chatHistoryFragment.refresh();
				}
			}
			// 娉ㄩ攢骞挎挱锛屽惁鍒欏湪ChatActivity涓細鏀跺埌杩欎釜骞挎挱
			abortBroadcast();
		}
	}

	/**
	 * 娑堟伅鍥炴墽BroadcastReceiver
	 */
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			EMConversation conversation = EMChatManager.getInstance().getConversation(from);
			if (conversation != null) {
				// 鎶妋essage璁句负宸茶
				EMMessage msg = conversation.getMessage(msgid);
				if (msg != null) {
					msg.isAcked = true;
				}
			}
			abortBroadcast();
		}
	};

	/**
	 * 绂荤嚎娑堟伅BroadcastReceiver
	 * sdk 鐧诲綍鍚庯紝鏈嶅姟鍣ㄤ細鎺ㄩ�佺绾挎秷鎭埌client锛岃繖涓猺eceiver锛屾槸閫氱煡UI 鏈夊摢浜涗汉鍙戞潵浜嗙绾挎秷鎭�
	 * UI 鍙互鍋氱浉搴旂殑鎿嶄綔锛屾瘮濡備笅杞界敤鎴蜂俊鎭�
	 */
	private BroadcastReceiver offlineMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String[] users = intent.getStringArrayExtra("fromuser");
			String[] groups = intent.getStringArrayExtra("fromgroup");
			if (users != null) {
				for (String user : users) {
					System.out.println("鏀跺埌user绂荤嚎娑堟伅锛�" + user);
				}
			}
			if (groups != null) {
				for (String group : groups) {
					System.out.println("鏀跺埌group绂荤嚎娑堟伅锛�" + group);
				}
			}
			abortBroadcast();
		}
	};
	
	private InviteMessgeDao inviteMessgeDao;
	private UserDao userDao;

	/***
	 * 濂藉弸鍙樺寲listener
	 * 
	 */
	private class MyContactListener implements EMContactListener {

		@Override
		public void onContactAdded(List<String> usernameList) {
			// 淇濆瓨澧炲姞鐨勮仈绯讳汉
			Map<String, User> localUsers = MyApplication.getInstance().getContactList();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				User user = setUserHead(username);
				// 鏆傛椂鏈変釜bug锛屾坊鍔犲ソ鍙嬫椂鍙兘浼氬洖璋僡dded鏂规硶涓ゆ
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);
			// 鍒锋柊ui
			if (currentTabIndex == 1)
				contactListFragment.refresh();

		}

		
		@Override
		public void onContactDeleted(final List<String> usernameList) {
			// 琚垹闄�
			Map<String, User> localUsers = MyApplication.getInstance().getContactList();
			for (String username : usernameList) {
				localUsers.remove(username);
				userDao.deleteContact(username);
				inviteMessgeDao.deleteMessage(username);
			}
			runOnUiThread(new Runnable() {
				public void run() {
					//濡傛灉姝ｅ湪涓庢鐢ㄦ埛鐨勮亰澶╅〉闈�
					if (ChatActivity.activityInstance != null && usernameList.contains(ChatActivity.activityInstance.getToChatUsername())) {
						Toast.makeText(ChatMainActivity.this, ChatActivity.activityInstance.getToChatUsername()+"宸叉妸浣犱粠浠栧ソ鍙嬪垪琛ㄩ噷绉婚櫎", 1).show();
						ChatActivity.activityInstance.finish();
					}
					updateUnreadLabel();
				}
			});
			// 鍒锋柊ui
			if (currentTabIndex == 1)
				contactListFragment.refresh();

		}

		@Override
		public void onContactInvited(String username, String reason) {
			// 鎺ュ埌閭�璇风殑娑堟伅锛屽鏋滀笉澶勭悊(鍚屾剰鎴栨嫆缁�)锛屾帀绾垮悗锛屾湇鍔″櫒浼氳嚜鍔ㄥ啀鍙戣繃鏉ワ紝鎵�浠ュ鎴风涓嶈閲嶅鎻愰啋
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 鑷繁灏佽鐨刯avabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			Log.d(TAG, username + "璇锋眰鍔犱綘涓哄ソ鍙�,reason: " + reason);
			// 璁剧疆鐩稿簲status
			msg.setStatus(InviteMesageStatus.BEINVITEED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactAgreed(String username) {
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 鑷繁灏佽鐨刯avabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			Log.d(TAG, username + "鍚屾剰浜嗕綘鐨勫ソ鍙嬭姹�");
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {
			// 鍙傝�冨悓鎰忥紝琚個璇峰疄鐜版鍔熻兘,demo鏈疄鐜�

		}

	}

	/**
	 * 淇濆瓨鎻愮ず鏂版秷鎭�
	 * 
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		saveInviteMsg(msg);
		// 鎻愮ず鏈夋柊娑堟伅
		EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

		// 鍒锋柊bottom bar娑堟伅鏈鏁�
		updateUnreadAddressLable();
		// 鍒锋柊濂藉弸椤甸潰ui
		if (currentTabIndex == 1)
			contactListFragment.refresh();
	}
	/**
	 * 淇濆瓨閭�璇风瓑msg
	 * @param msg
	 */
	private void saveInviteMsg(InviteMessage msg) {
		// 淇濆瓨msg
		inviteMessgeDao.saveMessage(msg);
		// 鏈鏁板姞1
		User user = MyApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME);
		user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
	}
	
	
	/**
	 * set head
	 * @param username
	 * @return
	 */
	User setUserHead(String username) {
		User user = new User();
		user.setUsername(username);
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
			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(
					0, 1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
		return user;
	}


	/**
	 * 杩炴帴鐩戝惉listener
	 * 
	 */
	private class MyConnectionListener implements ConnectionListener {

		@Override
		public void onConnected() {
			chatHistoryFragment.errorItem.setVisibility(View.GONE);
		}

		@Override
		public void onDisConnected(String errorString) {
			if (errorString != null && errorString.contains("conflict")) {
				// 鏄剧ず甯愬彿鍦ㄥ叾浠栬澶囩櫥闄哾ialog
				showConflictDialog();
			} else {
				chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
				if(NetUtils.hasNetwork(ChatMainActivity.this))
					chatHistoryFragment.errorText.setText("杩炴帴涓嶅埌鑱婂ぉ鏈嶅姟鍣�");
				else
					chatHistoryFragment.errorText.setText("褰撳墠缃戠粶涓嶅彲鐢紝璇锋鏌ョ綉缁滆缃�");
					
			}
		}

		@Override
		public void onReConnected() {
			chatHistoryFragment.errorItem.setVisibility(View.GONE);
		}

		@Override
		public void onReConnecting() {
		}

		@Override
		public void onConnecting(String progress) {
		}

	}

	/**
	 * MyGroupChangeListener
	 */
	private class MyGroupChangeListener implements GroupChangeListener {

		@Override
		public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
			boolean hasGroup = false;
			for(EMGroup group : EMGroupManager.getInstance().getAllGroups()){
				if(group.getGroupId().equals(groupId)){
					hasGroup = true;
					break;
				}
			}
			if(!hasGroup)
				return;
			
			// 琚個璇�
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(inviter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(inviter + "閭�璇蜂綘鍔犲叆浜嗙兢鑱�"));
			// 淇濆瓨閭�璇锋秷鎭�
			EMChatManager.getInstance().saveMessage(msg);
			// 鎻愰啋鏂版秷鎭�
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 鍒锋柊ui
					if (currentTabIndex == 0)
						chatHistoryFragment.refresh();
					if (CommonUtils.getTopActivity(ChatMainActivity.this).equals(GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});

		}

		@Override
		public void onInvitationAccpted(String groupId, String inviter, String reason) {

		}

		@Override
		public void onInvitationDeclined(String groupId, String invitee, String reason) {

		}

		@Override
		public void onUserRemoved(String groupId, String groupName) {
			// 鎻愮ず鐢ㄦ埛琚玊浜嗭紝demo鐪佺暐姝ゆ楠�
			// 鍒锋柊ui
			runOnUiThread(new Runnable() {
				public void run() {
					try {
						updateUnreadLabel();
						if (currentTabIndex == 0)
							chatHistoryFragment.refresh();
						if (CommonUtils.getTopActivity(ChatMainActivity.this).equals(GroupsActivity.class.getName())) {
							GroupsActivity.instance.onResume();
						}
					} catch (Exception e) {
						Log.e("###", "refresh exception " + e.getMessage());
					}

				}
			});
		}

		@Override
		public void onGroupDestroy(String groupId, String groupName) {
			// 缇よ瑙ｆ暎
			// 鎻愮ず鐢ㄦ埛缇よ瑙ｆ暎,demo鐪佺暐
			// 鍒锋柊ui
			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					if (currentTabIndex == 0)
						chatHistoryFragment.refresh();
					if (CommonUtils.getTopActivity(ChatMainActivity.this).equals(GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});

		}

		@Override
		public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {
			// 鐢ㄦ埛鐢宠鍔犲叆缇よ亰
			InviteMessage msg = new InviteMessage();
			msg.setFrom(applyer);
			msg.setTime(System.currentTimeMillis());
			msg.setGroupId(groupId);
			msg.setGroupName(groupName);
			msg.setReason(reason);
			Log.d(TAG, applyer + " 鐢宠鍔犲叆缇よ亰锛�" + groupName);
			msg.setStatus(InviteMesageStatus.BEAPPLYED);
			notifyNewIviteMessage(msg);
		}

		@Override
		public void onApplicationAccept(String groupId, String groupName, String accepter) {
			//鍔犵兢鐢宠琚悓鎰�
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(accepter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(accepter + "鍚屾剰浜嗕綘鐨勭兢鑱婄敵璇�"));
			// 淇濆瓨鍚屾剰娑堟伅
			EMChatManager.getInstance().saveMessage(msg);
			// 鎻愰啋鏂版秷鎭�
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();
			
			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 鍒锋柊ui
					if (currentTabIndex == 0)
						chatHistoryFragment.refresh();
					if (CommonUtils.getTopActivity(ChatMainActivity.this).equals(GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			});
		}

		@Override
		public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
			//鍔犵兢鐢宠琚嫆缁濓紝demo鏈疄鐜�
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isConflict) {
			updateUnreadLabel();
			updateUnreadAddressLable();
			EMChatManager.getInstance().activityResumed();
		}

	}

	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//将Activity退出到后台
			// false代表只有根activity才执行，true为后续activity都可以
			  
			moveTaskToBack(false);
			//true 为事件消耗完了，不继续传递
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}*/

	private android.app.AlertDialog.Builder conflictBuilder;
	private boolean isConflictDialogShow;

	/**
	 * 鏄剧ず甯愬彿鍦ㄥ埆澶勭櫥褰昫ialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		MyApplication.getInstance().logout();

		if (!ChatMainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(ChatMainActivity.this);
				conflictBuilder.setTitle("涓嬬嚎閫氱煡");
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						conflictBuilder = null;
						finish();
						startActivity(new Intent(ChatMainActivity.this, LoginActivity.class));
					}
				});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				Log.e("###", "---------color conflictBuilder error" + e.getMessage());
			}

		}

	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow)
			showConflictDialog();
	}
}
