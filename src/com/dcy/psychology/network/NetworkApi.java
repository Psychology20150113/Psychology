package com.dcy.psychology.network;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.util.Constants;

/**
 * Created by dcy123 on 2015/3/19.
 */
public class NetworkApi {
	private static final String PhpUrl = "http://114.215.179.130:8011/1/";
	
	private static String ApplyTalkUrl = PhpUrl + "InsertBesPeak.php";
	
	public static void applyTalk(String doctorPhone, String question, String info, 
			String startTime, String endTime, Listener<String> listener, ErrorListener errorListener){
		Map<String, String> params = new HashMap<String, String>();
		params.put("userphone", MyApplication.myPhoneNum);
		params.put("doctorphone", doctorPhone);
		params.put("question", question);
		params.put("instr", info);
		params.put("starttime", startTime);
		params.put("endtime", endTime);
		MyApplication.getInstance().getNetworkManager().getPostResultString(ApplyTalkUrl, params, listener, errorListener);
	}

	
}
