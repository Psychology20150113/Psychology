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
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.easemob.chatuidemo.adapter.ChatAllHistoryAdapter;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.easemob.chatuidemo.domain.User;

/**
 * 鏄剧ず鎵�鏈変細璇濊褰曪紝姣旇緝绠�鍗曠殑瀹炵幇锛屾洿濂界殑鍙兘鏄妸闄岀敓浜哄瓨鍏ユ湰鍦帮紝杩欐牱鍙栧埌鐨勮亰澶╄褰曟槸鍙帶鐨�
 * 
 */
public class ChatAllHistoryFragment extends Fragment {

	private InputMethodManager inputMethodManager;
	private ListView listView;
	private Map<String, User> contactList;
	private ChatAllHistoryAdapter adapter;
	private EditText query;
	private ImageButton clearSearch;
	public RelativeLayout errorItem;
	public TextView errorText;
	private boolean hidden;
	private List<EMGroup> groups;

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
		adapter = new ChatAllHistoryAdapter(getActivity(), 1, loadConversationsWithRecentChat());

		groups = EMGroupManager.getInstance().getAllGroups();

		// 璁剧疆adapter
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				EMConversation conversation = adapter.getItem(position);
				String username = conversation.getUserName();
				if (username.equals(MyApplication.getInstance().getUserName()))
					Toast.makeText(getActivity(), "涓嶈兘鍜岃嚜宸辫亰澶�", 0).show();
				else {
					// 杩涘叆鑱婂ぉ椤甸潰
					Intent intent = new Intent(getActivity(), ChatActivity.class);
					EMContact emContact = null;
					groups = EMGroupManager.getInstance().getAllGroups();
					for (EMGroup group : groups) {
						if (group.getGroupId().equals(username)) {
							emContact = group;
							break;
						}
					}
					if (emContact != null && emContact instanceof EMGroup) {
						// it is group chat
						intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
						intent.putExtra("groupId", ((EMGroup) emContact).getGroupId());
					} else {
						// it is single chat
						intent.putExtra("userId", username);
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
			EMConversation tobeDeleteCons = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 鍒犻櫎姝や細璇�
			EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(),tobeDeleteCons.isGroup());
			InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
			inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
			adapter.remove(tobeDeleteCons);
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
		adapter = new ChatAllHistoryAdapter(getActivity(), R.layout.row_chat_history, loadConversationsWithRecentChat());
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	/**
	 * 鑾峰彇鎵�鏈変細璇�
	 * 
	 * @param context
	 * @return
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 鑾峰彇鎵�鏈変細璇濓紝鍖呮嫭闄岀敓浜�
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
		List<EMConversation> conversationList = new ArrayList<EMConversation>();
		//杩囨护鎺塵essages seize涓�0鐨刢onversation
		for(EMConversation conversation : conversations.values()){
			if(conversation.getAllMessages().size() != 0)
				conversationList.add(conversation);
		}
		// 鎺掑簭
		sortConversationByLastChatTime(conversationList);
		return conversationList;
	}

	/**
	 * 鏍规嵁鏈�鍚庝竴鏉℃秷鎭殑鏃堕棿鎺掑簭
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(List<EMConversation> conversationList) {
		Collections.sort(conversationList, new Comparator<EMConversation>() {
			@Override
			public int compare(final EMConversation con1, final EMConversation con2) {

				EMMessage con2LastMessage = con2.getLastMessage();
				EMMessage con1LastMessage = con1.getLastMessage();
				if (con2LastMessage.getMsgTime() == con1LastMessage.getMsgTime()) {
					return 0;
				} else if (con2LastMessage.getMsgTime() > con1LastMessage.getMsgTime()) {
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
