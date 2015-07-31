package com.dcy.psychology.emchat;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.dcy.psychology.R;
import com.dcy.psychology.util.InfoShared;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;

public class HXChatManager {
	private static HXChatManager mChatManager;
	private InfoShared mShared;
	private Context mContext;
	
	private HXChatManager(Context context){
		mContext = context;
		mShared = new InfoShared(context);
	}
	
	public static HXChatManager getInstance(Context context){
		if(mChatManager == null){
			mChatManager = new HXChatManager(context);
		}
		return mChatManager;
	}
	
	public void chatLogin(String name, String pwd){
		chatLogin(name, pwd, null, null);
	}
	
	public void chatLogin(final String name, final String pwd, 
			final Runnable successRunnable, final Runnable errorRunnable){
		if(TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)){
			return;
		}
		EMChatManager.getInstance().login(name, pwd, new EMCallBack() {
			@Override
			public void onSuccess() {
				mShared.saveChatInfo(name, pwd);
				Toast.makeText(mContext, R.string.login_chat_failed, Toast.LENGTH_SHORT).show();
				try {
					EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
				} catch (Exception e) {
				}
				if(successRunnable != null){
					successRunnable.run();
				}
			}
			
			@Override
			public void onProgress(int arg0, String arg1) {
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(mContext, R.string.login_chat_failed, Toast.LENGTH_SHORT).show();
				if(errorRunnable != null){
					errorRunnable.run();
				}
			}
		});
	}
	
	public void chatLoginOut(){
		EMChatManager.getInstance().logout();
	}
}
