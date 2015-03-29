package com.dcy.psychology.util;

import com.dcy.psychology.MyApplication;
import com.dcy.psychology.LoginActivity.ChatLoginTask;

import android.content.Context;
import android.content.SharedPreferences;

public class InfoShared {
	private final String InfoSharedName = "app_info_1.0";
	private SharedPreferences mShared;
	private Context mContext;
	
	private final String AppVersion = "info_version";
	private final String UserName = "info_name";
	private final String UserPwd = "info_pwd";
	private final String UserNick = "info_nick";
	private final String UserRole = "info_role";
	
	public String ThemeFormat = "Theme%s_%d";
	
	public InfoShared(Context context) {
		this.mContext = context;
		mShared = context.getSharedPreferences(InfoSharedName, Context.MODE_PRIVATE);
	}
	
	public void setAppVersion(String version){
		mShared.edit().putString(AppVersion, version).commit();
	}
	
	public String getAppVersion(){
		return mShared.getString(AppVersion, "");
	}
	
	public void setUserName(String userName){
		mShared.edit().putString(UserName, userName).commit();
	}
	
	public String getUserName(){
		return mShared.getString(UserName, "");
	}
	
	public void setUserPwd(String pwd){
		mShared.edit().putString(UserPwd, pwd).commit();
	}
	
	public String getUserPwd(){
		return mShared.getString(UserPwd, "");
	}
	
	public void setUserNick(String nick){
		mShared.edit().putString(UserNick, nick).commit();
	}
	
	public String getUserNick(){
		return mShared.getString(UserNick, "");
	}
	
	public void setUserRole(String role){
		mShared.edit().putString(UserRole, role).commit();
	}
	
	public String getUserRole(){
		return mShared.getString(UserRole, "");
	}
	
	public void putInt(String key , int value){
		mShared.edit().putInt(key, value).commit();
	}
	
	public int getInt(String key){
		return mShared.getInt(key, 0);
	}
	
	public void saveInfo(String account, String pwd, String role){
		setUserName(account);
		setUserPwd(pwd);
		setUserRole(role);
		MyApplication.myUserName = account;
		MyApplication.myPwd = pwd;
		MyApplication.myUserRole = role;
		new ChatLoginTask(mContext).execute(account, pwd);
	}
	
	public void clearInfo(){
		mShared.edit().clear().commit();
	}
}
