package com.dcy.psychology;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Application;

import com.dcy.psychology.util.AsyncImageCache;
import com.dcy.psychology.util.InfoShared;
import com.dcy.psychology.util.Utils;
import com.google.gson.Gson;


public class MyApplication extends Application{
	
	public static String myUserName;
	public static String myPwd;
	public static String myUserRole;
	public static String myNick;
	public static Gson mGson;
	
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
	}
	
	private void initAppInfo(){
		InfoShared mShared = new InfoShared(this);
		myUserName = mShared.getUserName();
		myPwd = mShared.getUserPwd();
		myNick = mShared.getUserNick();
		myUserRole = mShared.getUserRole();
	}
	
}
