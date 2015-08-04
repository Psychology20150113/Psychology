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
package com.dcy.psychology.emchat;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;

/**
 * The developer can derive from this class to talk with HuanXin SDK
 * All the Huan Xin related initialization and global listener are implemented in this class which will 
 * help developer to speed up the SDK integration�?
 * this is a global instance class which can be obtained in any codes through getInstance()
 * 
 * @author easemob
 *
 */
public class HXSDKHelper {

	/**
	 * 群组更新完成
	 */
	static public interface HXSyncListener {
		public void onSyncSucess(boolean success);
	}
	
    private static final String TAG = "HXSDKHelper";
    
    /**
     * application context
     */
    protected Context appContext = null;
    
    /**
     * MyConnectionListener
     */
    protected EMConnectionListener connectionListener = null;
    
    /**
     * HuanXin ID in cache
     */
    protected String hxId = null;
    
    /**
     * password in cache
     */
    protected String password = null;
    
    /**
     * init flag: test if the sdk has been inited before, we don't need to init again
     */
    private boolean sdkInited = false;

    /**
     * the global HXSDKHelper instance
     */
    private static HXSDKHelper me = null;
    
	/**
	 * HuanXin sync groups status listener
	 */
	private List<HXSyncListener> syncGroupsListeners;

	/**
	 * HuanXin sync contacts status listener
	 */
	private List<HXSyncListener> syncContactsListeners;

	/**
	 * HuanXin sync blacklist status listener
	 */
	private List<HXSyncListener> syncBlackListListeners;

	private boolean isSyncingGroupsWithServer = false;

	private boolean isSyncingContactsWithServer = false;

	private boolean isSyncingBlackListWithServer = false;
	
	private boolean isGroupsSyncedWithServer = false;

	private boolean isContactsSyncedWithServer = false;

	private boolean isBlackListSyncedWithServer = false;
	
	private boolean alreadyNotified = false;
	
	public boolean isVoiceCalling;
    public boolean isVideoCalling;

    public HXSDKHelper(){
        me = this;
    }
    
    /**
     * this function will initialize the HuanXin SDK
     * 
     * @return boolean true if caller can continue to call HuanXin related APIs after calling onInit, otherwise false.
     * 
     * 环信初始化SDK帮助函数
     * 返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代�?
     * 
     * for example:
     * 例子�?
     * 
     * public class DemoHXSDKHelper extends HXSDKHelper
     * 
     * HXHelper = new DemoHXSDKHelper();
     * if(HXHelper.onInit(context)){
     *     // do HuanXin related work
     * }
     */
    public synchronized boolean onInit(Context context){
        if(sdkInited){
            return true;
        }
        appContext = context;
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果app启用了远程的service，此application:onCreate会被调用2�?
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1�?
        // 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返�?
        if (processAppName == null || 
        		!processAppName.equalsIgnoreCase("com.dcy.psychology")) {
            return false;
        }
        // 初始化环信SDK,�?定要先调用init()
        EMChat.getInstance().init(context);
        return true;
    }
    
    /**
     * get global instance
     * @return
     */
    public static HXSDKHelper getInstance(){
        return me;
    }
    
    public Context getAppContext(){
        return appContext;
    }
    
    
    /**
     * logout HuanXin SDK
     */
    public void logout(final EMCallBack callback){
//        setPassword(null);
//        reset();
        EMChatManager.getInstance().logout(new EMCallBack(){

            @Override
            public void onSuccess() {
                if(callback != null){
                    callback.onSuccess();
                }
            }

            @Override
            public void onError(int code, String message) {                
            }

            @Override
            public void onProgress(int progress, String status) {
                if(callback != null){
                    callback.onProgress(progress, status);
                }
            }
            
        });
    }
    
    /**
     * �?查是否已经登录过
     * @return
     */
    public boolean isLogined(){
       return EMChat.getInstance().isLoggedIn();
    }
    

    /**
     * init HuanXin listeners
     */
    protected void initListener(){
        Log.d(TAG, "init listener");
        
        // create the global connection listener
        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
            	if (error == EMError.USER_REMOVED) {
            		onCurrentAccountRemoved();
            	}else if (error == EMError.CONNECTION_CONFLICT) {
                    onConnectionConflict();
                }else{
                    onConnectionDisconnected(error);
                }
            }

            @Override
            public void onConnected() {
                onConnectionConnected();
            }
        };
        
        //注册连接监听
        EMChatManager.getInstance().addConnectionListener(connectionListener);       
    }

    /**
     * the developer can override this function to handle connection conflict error
     */
    protected void onConnectionConflict(){}

    
    /**
     * the developer can override this function to handle user is removed error
     */
    protected void onCurrentAccountRemoved(){}
    
    
    /**
     * handle the connection connected
     */
    protected void onConnectionConnected(){}
    
    /**
     * handle the connection disconnect
     * @param error see {@link EMError}
     */
    protected void onConnectionDisconnected(int error){}

    /**
     * check the application process name if process name is not qualified, then we think it is a service process and we will not init SDK
     * @param pID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = appContext.getPackageManager();
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
    
        
    public void addSyncGroupListener(HXSyncListener listener) {
	    if (listener == null) {
		    return;
	    }
	    if (!syncGroupsListeners.contains(listener)) {
		    syncGroupsListeners.add(listener);
	    }
    }

    public void removeSyncGroupListener(HXSyncListener listener) {
	    if (listener == null) {
		    return;
	    }
	    if (syncGroupsListeners.contains(listener)) {
		    syncGroupsListeners.remove(listener);
	    }
    }

    public void addSyncContactListener(HXSyncListener listener) {
	    if (listener == null) {
		    return;
	    }
	    if (!syncContactsListeners.contains(listener)) {
		    syncContactsListeners.add(listener);
	    }
    }

    public void removeSyncContactListener(HXSyncListener listener) {
	    if (listener == null) {
		    return;
	    }
	    if (syncContactsListeners.contains(listener)) {
		    syncContactsListeners.remove(listener);
	    }
    }

    public void addSyncBlackListListener(HXSyncListener listener) {
	    if (listener == null) {
		    return;
	    }
	    if (!syncBlackListListeners.contains(listener)) {
		    syncBlackListListeners.add(listener);
	    }
    }

    public void removeSyncBlackListListener(HXSyncListener listener) {
	    if (listener == null) {
		    return;
	    }
	    if (syncBlackListListeners.contains(listener)) {
		    syncBlackListListeners.remove(listener);
	    }
    }
   
    public void noitifyGroupSyncListeners(boolean success){
        for (HXSyncListener listener : syncGroupsListeners) {
            listener.onSyncSucess(success);
        }
    }

    public void notifyContactsSyncListener(boolean success){
        for (HXSyncListener listener : syncContactsListeners) {
            listener.onSyncSucess(success);
        }
    }
    
    public void notifyBlackListSyncListener(boolean success){
        for (HXSyncListener listener : syncBlackListListeners) {
            listener.onSyncSucess(success);
        }
    }
    
    public boolean isSyncingGroupsWithServer() {
	    return isSyncingGroupsWithServer;
    }

    public boolean isSyncingContactsWithServer() {
	    return isSyncingContactsWithServer;
    }

    public boolean isSyncingBlackListWithServer() {
	    return isSyncingBlackListWithServer;
    }
    
    public boolean isGroupsSyncedWithServer() {
	    return isGroupsSyncedWithServer;
    }

    public boolean isContactsSyncedWithServer() {
	    return isContactsSyncedWithServer;
    }

    public boolean isBlackListSyncedWithServer() {
	    return isBlackListSyncedWithServer;
    }
    
    public synchronized void notifyForRecevingEvents(){
        if(alreadyNotified){
            return;
        }
        
        // 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast�?
        EMChat.getInstance().setAppInited();
        alreadyNotified = true;
    }
}
