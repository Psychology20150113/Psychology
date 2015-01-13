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
package com.easemob.chatuidemo.adapter;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.dcy.psychology.R;
import com.easemob.chatuidemo.utils.SmileUtils;
import com.easemob.util.DateUtils;

/**
 * 鑱婂ぉ璁板綍adpater
 * 
 */
public class ChatHistoryAdapter extends ArrayAdapter<EMContact> {

	private LayoutInflater inflater;

	public ChatHistoryAdapter(Context context, int textViewResourceId, List<EMContact> objects) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_chat_history, parent, false);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null) {
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
			holder.message = (TextView) convertView.findViewById(R.id.message);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
			holder.msgState = convertView.findViewById(R.id.msg_state);
			holder.list_item_layout=(RelativeLayout) convertView.findViewById(R.id.list_item_layout);
			convertView.setTag(holder);
		}
		if(position%2==0)
		{
			holder.list_item_layout.setBackgroundResource(R.drawable.mm_listitem);
		}else{
			holder.list_item_layout.setBackgroundResource(R.drawable.mm_listitem_grey);
		}
		
		
		EMContact user = getItem(position);
		if(user instanceof EMGroup){
			//缇よ亰娑堟伅锛屾樉绀虹兢鑱婂ご鍍�
			holder.avatar.setImageResource(R.drawable.group_icon);
		}else{
			holder.avatar.setImageResource(R.drawable.default_avatar);
		}
		
		String username = user.getUsername();
		// 鑾峰彇涓庢鐢ㄦ埛/缇ょ粍鐨勪細璇�
		EMConversation conversation = EMChatManager.getInstance().getConversation(username);
		holder.name.setText(user.getNick() != null ? user.getNick() : username);
		if (conversation.getUnreadMsgCount() > 0) {
			// 鏄剧ず涓庢鐢ㄦ埛鐨勬秷鎭湭璇绘暟
			holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
			holder.unreadLabel.setVisibility(View.VISIBLE);
		} else {
			holder.unreadLabel.setVisibility(View.INVISIBLE);
		}

		if (conversation.getMsgCount() != 0) {
			// 鎶婃渶鍚庝竴鏉℃秷鎭殑鍐呭浣滀负item鐨刴essage鍐呭
			EMMessage lastMessage = conversation.getLastMessage();
			holder.message.setText(SmileUtils.getSmiledText(getContext(), getMessageDigest(lastMessage, (this.getContext()))),
					BufferType.SPANNABLE);

			holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
			if (lastMessage.direct == EMMessage.Direct.SEND && lastMessage.status == EMMessage.Status.FAIL) {
				holder.msgState.setVisibility(View.VISIBLE);
			} else {
				holder.msgState.setVisibility(View.GONE);
			}
		}

		return convertView;
	}

	/**
	 * 鏍规嵁娑堟伅鍐呭鍜屾秷鎭被鍨嬭幏鍙栨秷鎭唴瀹规彁绀�
	 * 
	 * @param message
	 * @param context
	 * @return
	 */
	private String getMessageDigest(EMMessage message, Context context) {
		String digest = "";
		switch (message.getType()) {
		case LOCATION: // 浣嶇疆娑堟伅
			if (message.direct == EMMessage.Direct.RECEIVE) {
				//浠巗dk涓彁鍒颁簡ui涓紝浣跨敤鏇寸畝鍗曚笉鐘敊鐨勮幏鍙杝tring鏂规硶
//				digest = EasyUtils.getAppResourceString(context, "location_recv");
				digest = getStrng(context, R.string.location_recv);
				digest = String.format(digest, message.getFrom());
				return digest;
			} else {
//				digest = EasyUtils.getAppResourceString(context, "location_prefix");
				digest = getStrng(context, R.string.location_prefix);
			}
			break;
		case IMAGE: // 鍥剧墖娑堟伅
			ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
			digest = getStrng(context, R.string.picture) + imageBody.getFileName();
			break;
		case VOICE:// 璇煶娑堟伅
			digest = getStrng(context, R.string.voice);
			break;
		case VIDEO: // 瑙嗛娑堟伅
			digest = getStrng(context, R.string.video);
			break;
		case TXT: // 鏂囨湰娑堟伅
			TextMessageBody txtBody = (TextMessageBody) message.getBody();
			digest = txtBody.getMessage();
			break;
		case FILE: //鏅�氭枃浠舵秷鎭�
			digest = getStrng(context, R.string.file);
			break;
		default:
			System.err.println("error, unknow type");
			return "";
		}

		return digest;
	}

	private static class ViewHolder {
		/** 鍜岃皝鐨勮亰澶╄褰� */
		TextView name;
		/** 娑堟伅鏈鏁� */
		TextView unreadLabel;
		/** 鏈�鍚庝竴鏉℃秷鎭殑鍐呭 */
		TextView message;
		/** 鏈�鍚庝竴鏉℃秷鎭殑鏃堕棿 */
		TextView time;
		/** 鐢ㄦ埛澶村儚 */
		ImageView avatar;
		/** 鏈�鍚庝竴鏉℃秷鎭殑鍙戦�佺姸鎬� */
		View msgState;
		/**鏁翠釜list涓瘡涓�琛屾�诲竷灞�*/
		RelativeLayout list_item_layout;
		
		
	}
	
	String getStrng(Context context, int resId){
		return context.getResources().getString(resId);
	}
}
