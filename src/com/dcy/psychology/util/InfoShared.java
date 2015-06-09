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
	private final String UserPhoneNum = "info_phone";
	private final String PrefectInfo = "prefect_info";
	private final String TestHollendData = "hollend_data";
	private final String TestHollendResult = "hollend_result";
	private final String TestHollendAllResult = "hollend_all_result";
	private final String TestQizhiData = "qizhi_data";
	private final String TestQiZhiResult = "qizhi_result";
	private final String TestQiZhiAllResult = "qizhi_all_result";
	
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
	
	public void setPhoneNum(String phoneNum){
		mShared.edit().putString(UserPhoneNum, phoneNum).commit();
	}
	
	public String getPhoneNum(){
		return mShared.getString(UserPhoneNum, "");
	}
	
	public void setIsPrefectInfo(boolean prefectInfo){
		mShared.edit().putBoolean(PrefectInfo, prefectInfo).commit();
	}
	
	public boolean hasPrefectInfo(){
		return mShared.getBoolean(PrefectInfo, false);
	}
	
	public void setHollendResult(String hollendData, String hollendResult, String allHollendResult){
		mShared.edit().putString(TestHollendData, hollendData).putString(TestHollendResult, hollendResult)
			.putString(TestHollendAllResult, allHollendResult).commit();
	}
	
	public String getHollendResult(){
		return mShared.getString(TestHollendResult, "");
	}
	
	public String getHollendData(){
		return mShared.getString(TestHollendData, "");
	}
	
	public void setQizhiResult(String qizhiData, String qizhiResult, String allQizhiResult){
		mShared.edit().putString(TestQizhiData, qizhiData).putString(TestQiZhiResult, qizhiResult)
			.putString(TestQiZhiAllResult, allQizhiResult).commit();
	}
	
	public String getQizhiData(){
		return mShared.getString(TestQizhiData, "");
	}
	
	public String getQizhiResult(){
		return mShared.getString(TestQiZhiResult, "");
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
	
	public void savePhoneInfo(String phoneNum, String pwd, String role, boolean isPrefectInfo){
		setPhoneNum(phoneNum);
		setUserPwd(pwd);
		setUserRole(role);
		setIsPrefectInfo(isPrefectInfo);
		MyApplication.myPhoneNum = phoneNum;
		MyApplication.myPwd = pwd;
		MyApplication.myUserRole = role;
		MyApplication.hasPrefectInfo = isPrefectInfo;
	}
	
	public void clearInfo(){
		String version = getAppVersion();
		mShared.edit().clear().commit();
		mShared.edit().putString(AppVersion, version).commit();
	}
}
