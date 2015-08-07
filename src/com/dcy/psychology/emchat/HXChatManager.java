package com.dcy.psychology.emchat;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
	private final int Msg_Login_Success = 100;
	private final int Msg_Login_Fail = 101;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Msg_Login_Success:
				Toast.makeText(mContext, R.string.login_chat_success, Toast.LENGTH_SHORT).show();
				break;
			case Msg_Login_Fail:
				Toast.makeText(mContext, (String)msg.obj, Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};
	
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
				mHandler.sendMessage(mHandler.obtainMessage(Msg_Login_Success));
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
				mHandler.sendMessage(mHandler.obtainMessage(Msg_Login_Fail, arg1));
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
