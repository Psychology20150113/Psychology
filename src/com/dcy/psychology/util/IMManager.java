package com.dcy.psychology.util;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class IMManager {
	private XMPPConnection connection;
	private IMManager(){
		ConnectionConfiguration config = new ConnectionConfiguration(Constants.IMAddress, Constants.IMPort);
		connection = new XMPPConnection(config);
	}
	
	public static IMManager getInstance(){
		return new IMManager();
	}
	
	public boolean registerIM(String username , String password){
		try {
			connection.connect();
			if(connection.isConnected()){
				AccountManager manager = new AccountManager(connection);
				manager.createAccount(username, password);
				return true;
			}
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean loginIM(String username,String password){
		try {
			connection.connect();
			if(connection.isConnected()){
				connection.login(username, password);
				return true;
			}
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return false;
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
	
}
