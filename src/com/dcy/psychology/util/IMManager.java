package com.dcy.psychology.util;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class IMManager {
	private XMPPConnection connection;
	private Chat mChat;
	private static IMManager mManager;
	
	private IMManager(){
		ConnectionConfiguration config = new ConnectionConfiguration(Constants.IMAddress, Constants.IMPort);
		connection = new XMPPConnection(config);
	}
	
	public static IMManager getInstance(){
		if(mManager == null)
			mManager = new IMManager();
		return mManager;
	}
	
	public boolean registerIM(String username , String password){
		try {
			Log.i("chat", "register connection start");
//			connection.connect();
			Log.i("chat", "register connection end");
			if(connection.isConnected()){
				Log.i("chat", "register start");
				AccountManager manager = new AccountManager(connection);
				manager.createAccount(username, password);
				Log.i("chat", "register end");
				return true;
			}
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean isLogined(){
		return connection.isConnected() && !TextUtils.isEmpty(connection.getUser());
	}
	
	public boolean loginIM(String username,String password){
		try {
			if(isLogined()){
				return true;
			}
			if(!connection.isConnected()){
				Log.i("chat", "login connect start");
//				connection.connect();
				Log.i("chat", "login connect end");
			}
			if(connection.isConnected() && TextUtils.isEmpty(connection.getUser())){
				Log.i("chat", "login start");
				connection.login(username, password);
				Log.i("chat", "login end");
				return true;
			}
		} catch (XMPPException e) {
			Log.i("chat", "login exception : " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public void logoutIM(){
		if(connection.isConnected())
			connection.disconnect();
		mChat = null;
	}
	
	public boolean addFriend(String friendName){
		try {
			connection.getRoster().createEntry(friendName, friendName, new String[]{Constants.IMDefaultGroup});
			return true;
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void getChatMessage(final Handler mHandler, String chatJID){
		if(!connection.isConnected() || TextUtils.isEmpty(connection.getUser())){
			return;
		}
		mChat = connection.getChatManager().createChat(chatJID, new MessageListener() {
			@Override
			public void processMessage(Chat chat, Message message) {
				mHandler.sendMessage(mHandler.obtainMessage(1, message.getBody()));
			}
		});
	}
	
	public void getPublicMessage(final Handler mHandler){
		if(!connection.isConnected() || TextUtils.isEmpty(connection.getUser())){
			return;
		}
		connection.addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				mHandler.sendMessage(mHandler.obtainMessage(1, 
						((Message) packet).getBody()));
			}
		}, new PacketTypeFilter(Message.class));
	}
	
	public boolean pushChatMessage(String message){
		try {
			if(mChat == null || !connection.isConnected() || 
					TextUtils.isEmpty(connection.getUser()))
				return false;
			mChat.sendMessage(message);
			return true;
		} catch (XMPPException e) {
			e.printStackTrace();
			return false;
		}
	}
}
