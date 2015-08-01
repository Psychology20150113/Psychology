package com.dcy.psychology;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;

import com.dcy.psychology.db.PreInstallDbHelper;
import com.dcy.psychology.emchat.HXChatManager;
import com.dcy.psychology.emchat.HXSDKHelper;
import com.dcy.psychology.network.NetworkManager;
import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.Utils;
import com.google.gson.Gson;


public class MyApplication extends Application{
	
	public static String myUserName;
	public static String myPhoneNum;
	public static boolean hasPrefectInfo;
	public static String myPwd;
	public static String myUserRole;
	public static String myNick;
	public static String myHeadUrl;
	public static Gson mGson;
	public static PreInstallDbHelper preInstallDbHelper;
	private static MyApplication instance;
	private NetworkManager mNetworkManager;
	private HXSDKHelper mHelper = new HXSDKHelper();
	private HXChatManager mChatManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		AsyncImageCache.setDiskCacheEnable(true);
        AsyncImageCache.setDiskCacheDir(Utils.getSDPath()+"/Psychology/download/cache");
        AsyncImageCache.setDiskCacheSize(1024 * 1024 * 50);     //50MB
        AsyncImageCache.setDiskCacheCount(1024);                //1024 item
        AsyncImageCache.setMemoryCacheSize(1024 * 1024 * 10);    //10MB
        instance = this;
        JPushInterface.init(this);
        initAppInfo();
//        MobclickAgent.setDebugMode(true);
        mGson = new Gson();
        preInstallDbHelper = new PreInstallDbHelper(this);
        mHelper.onInit(this);
	}
	
	public NetworkManager getNetworkManager(){
		if(mNetworkManager == null){
			mNetworkManager = NetworkManager.getInstance(this);
		}
		return mNetworkManager;
	}
	
	public HXChatManager getChatManager(){
		if(mChatManager == null){
			mChatManager = HXChatManager.getInstance(this);
		}
		return mChatManager;
	}
	
	public static Application getInstance(){
		return instance;
	}
	
	private void initAppInfo(){
		InfoShared mShared = new InfoShared(this);
		myUserName = mShared.getUserName();
		myPhoneNum = mShared.getPhoneNum();
		myPwd = mShared.getUserPwd();
		myNick = mShared.getUserNick();
		myUserRole = mShared.getUserRole();
		myHeadUrl = mShared.getHeaderUrl();
		hasPrefectInfo = mShared.hasPrefectInfo();
	}
	
}
