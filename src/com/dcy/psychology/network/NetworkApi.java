package com.dcy.psychology.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.dcy.psychology.MyApplication;
import com.dcy.psychology.gsonbean.ApplyInfoBean;
import com.dcy.psychology.gsonbean.SpecificUserBean;
import com.dcy.psychology.gsonbean.UserInfoBean;
import com.dcy.psychology.util.Constants;
import com.google.gson.reflect.TypeToken;
import com.tencent.connect.UserInfo;

/**
 * Created by dcy123 on 2015/3/19.
 */
public class NetworkApi {
	private static final String PhpUrl = "http://114.215.179.130:8011/1/";
	
	private static String GetUserUrl = PhpUrl + "GetUserInfo.php";
	private static String ApplyTalkUrl = PhpUrl + "InsertBesPeak.php";
	private static String GetApplyList = PhpUrl + "GetBespeakList.php";
	
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

	public static void getDetailInfo(long doctorId, Listener<UserInfoBean> listener){
		Map<String, String> params = new HashMap<String, String>();
		params.put("DoctorUserID", String.valueOf(doctorId));
		params.put("userPhone", MyApplication.myPhoneNum);
		MyApplication.getInstance().getNetworkManager().getResultClass(GetUserUrl, params, UserInfoBean.class, listener);
	}
	
	public static void getApplyList(Listener<ArrayList<ApplyInfoBean>> mListener){
		Map<String, String> params = new HashMap<String, String>();
		params.put("phone", MyApplication.myPhoneNum);
		params.put("page", "1");
		params.put("pagesize", String.valueOf(Integer.MAX_VALUE));
		params.put("type", Constants.RoleUser.equals(MyApplication.myUserRole) ? "1" : "2");
		MyApplication.getInstance().getNetworkManager().getResultClass(GetApplyList, params, 
				new TypeToken<ArrayList<ApplyInfoBean>>(){}.getType(), mListener);
	}
}
