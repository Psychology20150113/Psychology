package com.dcy.psychology;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.Utils;
import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.OnNotificationClickListener;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chatuidemo.activity.ChatActivity;
import com.easemob.chatuidemo.activity.ChatMainActivity;
import com.easemob.chatuidemo.db.DbOpenHelper;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.PreferenceUtils;
import com.google.gson.Gson;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class MyApplication extends Application{
	
	public static String myUserName;
	public static String myPwd;
	public static String myUserRole;
	public static String myNick;
	public static Gson mGson;
	
	//chat 鍙橀噺
	public static Context applicationContext;
	private static MyApplication instance;
	public final String PREF_USERNAME = "username";
	private String userName = null;
	private static final String PREF_PWD = "pwd";
	private String password = null;
	private Map<String, User> contactList;
	public static String currentUserNick = "";
	
	@Override
	public void onCreate() {
		super.onCreate();
		AsyncImageCache.setDiskCacheEnable(true);
        AsyncImageCache.setDiskCacheDir(Utils.getSDPath()+"/Wallpapers/download/cache");
        AsyncImageCache.setDiskCacheSize(1024 * 1024 * 50);     //50MB
        AsyncImageCache.setDiskCacheCount(1024);                //1024 item
        AsyncImageCache.setMemoryCacheSize(1024 * 1024 * 10);    //10MB
        initAppInfo();
        mGson = new Gson();
        
        int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		if (processAppName == null || processAppName.equals("")) {
			return;
		}
		applicationContext = this;
		instance = this;
		EMChat.getInstance().setDebugMode(true);
		EMChat.getInstance().init(applicationContext);
		Log.d("EMChat Demo", "initialize EMChat SDK");
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		options.setAcceptInvitationAlways(false);
		options.setNotificationEnable(PreferenceUtils.getInstance(applicationContext).getSettingMsgNotification());
		options.setNoticeBySound(PreferenceUtils.getInstance(applicationContext).getSettingMsgSound());
		options.setNoticedByVibrate(PreferenceUtils.getInstance(applicationContext).getSettingMsgVibrate());
		options.setUseSpeaker(PreferenceUtils.getInstance(applicationContext).getSettingMsgSpeaker());
		options.setOnNotificationClickListener(new OnNotificationClickListener() {
			@Override
			public Intent onNotificationClick(EMMessage message) {
				Intent intent = new Intent(applicationContext, ChatActivity.class);
				ChatType chatType = message.getChatType();
				if (chatType == ChatType.Chat) { 
					intent.putExtra("userId", message.getFrom());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
				} else { 
					intent.putExtra("groupId", message.getTo());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
				}
				return intent;
			}
		});
		EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
//		options.setNotifyText(new OnMessageNotifyListener() {
//
//			@Override
//			public String onNewMessageNotify(EMMessage message) {
//				return "娴ｇ姷娈戞總钘夌唨閸欙拷" + message.getFrom() + "閸欐垶娼垫禍鍡曠閺夆剝绉烽幁顖氭憹";
//			}
//
//			@Override
//			public String onLatestMessageNotify(EMMessage message, int fromUsersNum, int messageNum) {
//				return fromUsersNum + "娑擃亜鐔�閸欏绱濋崣鎴炴降娴滐拷" + messageNum + "閺夆剝绉烽幁锟�";
//			}
//
//			@Override
//			public String onSetNotificationTitle(EMMessage message) {
//				//娣囶喗鏁奸弽鍥暯
//				return "閻滎垯淇妌otification";
//			}
//		});
	}
	
	private void initAppInfo(){
		InfoShared mShared = new InfoShared(this);
		myUserName = mShared.getUserName();
		myPwd = mShared.getUserPwd();
		myNick = mShared.getUserNick();
		myUserRole = mShared.getUserRole();
	}
	
	//chat init
	public static MyApplication getInstance() {
		return instance;
	}

	// List<String> list = new ArrayList<String>();
	// list.add("1406713081205");
	// options.setReceiveNotNoifyGroup(list);
	public Map<String, User> getContactList() {
		if (getUserName() != null && contactList == null) {
			UserDao dao = new UserDao(applicationContext);
			contactList = dao.getContactList();
		}
		return contactList;
	}

	public void setContactList(Map<String, User> contactList) {
		this.contactList = contactList;
	}

	public void setStrangerList(Map<String, User> List) {

	}

	public String getUserName() {
		if (userName == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			userName = preferences.getString(PREF_USERNAME, null);
		}
		return userName;
	}

	public String getPassword() {
		if (password == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			password = preferences.getString(PREF_PWD, null);
		}
		return password;
	}

	public void setUserName(String username) {
		if (username != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString(PREF_USERNAME, username).commit()) {
				userName = username;
			}
		}
	}

	public void setPassword(String pwd) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putString(PREF_PWD, pwd).commit()) {
			password = pwd;
		}
	}

	public void logout() {
		EMChatManager.getInstance().logout();
		DbOpenHelper.getInstance(applicationContext).closeDB();
		setPassword(null);
		setContactList(null);

	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}

	class MyConnectionListener implements ConnectionListener {
		@Override
		public void onReConnecting() {
		}

		@Override
		public void onReConnected() {
		}

		@Override
		public void onDisConnected(String errorString) {
			if (errorString != null && errorString.contains("conflict")) {
				Intent intent = new Intent(applicationContext, ChatMainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("conflict", true);
				startActivity(intent);
			}

		}

		@Override
		public void onConnecting(String progress) {

		}

		@Override
		public void onConnected() {
		}
	}
}
