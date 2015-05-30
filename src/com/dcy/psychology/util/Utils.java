package com.dcy.psychology.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.dcy.psychology.MyApplication;
import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.ArticleBean;
import com.dcy.psychology.gsonbean.BasicBean;
import com.dcy.psychology.gsonbean.ClassBean;
import com.dcy.psychology.gsonbean.CommentBean;
import com.dcy.psychology.gsonbean.CommentDetailBean;
import com.dcy.psychology.gsonbean.LoginBean;
import com.dcy.psychology.gsonbean.RegisterBean;
import com.dcy.psychology.gsonbean.SmsCodeBean;
import com.dcy.psychology.gsonbean.SpecificUserBean;
import com.dcy.psychology.model.UserInfoModel;
import com.dcy.psychology.view.QuestionView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;
import android.widget.Toast;

public class Utils {
	public static String loadRawString(Context context , int resId) {
		StringBuffer stringBuffer = new StringBuffer();
		InputStream is = null;
		BufferedReader br = null;
		try {
			is = context.getResources().openRawResource(resId);
			String temp = null;
			br = new BufferedReader(new InputStreamReader(is, "GBK"));
			while ((temp = br.readLine()) != null) {
				stringBuffer.append(temp);
				stringBuffer.append("\n");
			}
		} catch (NotFoundException e) {
			Toast.makeText(context, R.string.not_found_exception,
					Toast.LENGTH_SHORT).show();
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(context, R.string.unsupported_encoding,
					Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Toast.makeText(context, R.string.io_exception, Toast.LENGTH_SHORT)
					.show();
		} finally {
			try {
				if (is != null)
					is.close();
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return stringBuffer.toString();
	}
	
	private static SoapObject getResultFromRequest(SoapObject request) {
		return getResultFromRequest(request, null);
	}
	
	private static SoapObject getResultFromRequest(SoapObject request, String url) {
		//生成调用WebService的SOAP请求
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
		envelope.bodyOut = request;
		envelope.dotNet = true;
		HttpTransportSE transportSE = new HttpTransportSE(TextUtils.isEmpty(url) ? 
				Constants.UserWSDL : url, Constants.TimeOut);
		try {
			//1.1版本需要使用第一个参数SoapAction(例http://114.215.179.130/Login),1.2不需要SoapAction
			transportSE.call("", envelope);
		} catch (HttpResponseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		/*SoapObject result = null;
		try {
			result = (SoapObject) envelope.getResponse();
		} catch (SoapFault e) {
			e.printStackTrace();
		}*/
		//SoapFault error = (SoapFault)envelope.bodyIn;
		//Log.i("mylog", error.toString());
		
		SoapObject result = null;
		try {
			result = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			if(envelope.bodyIn instanceof SoapFault){
				Log.i("mylog" , envelope.bodyIn.toString());
			}else {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static LoginBean getLoginWeb(String name,String pwd){
		//此处后面没有“/”，不然会导致“没有有效的参数操作<1.2>”或者1.0里面无法传递参数
		SoapObject request = new SoapObject(Constants.SpaceName,Constants.LoginMethod);
		//设置调用参数,1.2版本前面命名需要与服务器一致
		request.addProperty("loginName", name);
		request.addProperty("loginPwd", pwd);
		SoapObject result = getResultFromRequest(request);
		if(result == null){
			return new LoginBean();
		}
		Log.i("mylog", "login : " + result.getPropertyAsString(0));
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), LoginBean.class);
	}
	
	public static SmsCodeBean sendSMS(String phoneNum){
		SoapObject request = new SoapObject(Constants.SpaceName, Constants.SendSMS);
		request.addProperty("phoneNum", phoneNum);
		SoapObject result = getResultFromRequest(request);
		if(result == null)
			return new SmsCodeBean();
		Log.i("mylog", "getCode : " + result.getPropertyAsString(0));
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), SmsCodeBean.class);
	}
	
	public static SmsCodeBean sendFindSMS(String phoneNum){
		SoapObject request = new SoapObject(Constants.SpaceName, Constants.SendFindSMS);
		request.addProperty("phoneNum", phoneNum);
		SoapObject result = getResultFromRequest(request);
		if(result == null)
			return new SmsCodeBean();
		Log.i("mylog", "getCode : " + result.getPropertyAsString(0));
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), SmsCodeBean.class);
	}
	
	public static BasicBean getVerifySmsCode(String phoneNum, String smsCode, String userPwd){
		SoapObject request = new SoapObject(Constants.SpaceName, Constants.VerifySmsCode);
		request.addProperty("phoneNum", phoneNum);
		request.addProperty("smsCode", smsCode);
		request.addProperty("userPwd", userPwd);
		SoapObject result = getResultFromRequest(request);
		if(result == null)
			return new BasicBean();
		Log.i("mylog", "register : " + result.getPropertyAsString(0));
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), BasicBean.class);
	}
	
	public static BasicBean prefectUserInfo(Map<String, String> infoMap){
		if(infoMap == null || TextUtils.isEmpty(MyApplication.myPhoneNum)){
			return new BasicBean();
		}
		SoapObject request = new SoapObject(Constants.SpaceName, Constants.PrefectInfoMethod);
		request.addProperty("userPhone", MyApplication.myPhoneNum);
		request.addProperty("userLoginName", MyApplication.myPhoneNum);
		request.addProperty("userHeadUrl", "");
		for(Entry<String, String> item : infoMap.entrySet()){
			request.addProperty(item.getKey(), item.getValue());
		}
		SoapObject result = getResultFromRequest(request);
		if(result == null){
			return new BasicBean();
		}
		Log.i("mylog", "prefect info : " + result.getPropertyAsString(0));
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), BasicBean.class);
	}
	
	public static BasicBean getVerifyFindSmsCode(String phoneNum, String smsCode, String userPwd){
		SoapObject request = new SoapObject(Constants.SpaceName, Constants.VerifyFindSmsCode);
		request.addProperty("phoneNum", phoneNum);
		request.addProperty("smsCode", smsCode);
		request.addProperty("userPwd", userPwd);
		SoapObject result = getResultFromRequest(request);
		if(result == null)
			return new BasicBean();
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), BasicBean.class);
	}
	
	public static RegisterBean getRegisterResult(UserInfoModel user){
		SoapObject request = new SoapObject(Constants.SpaceName,Constants.RegisterUserMethod);
		request.addProperty("userLoginName", user.getUserLoginName());
		request.addProperty("userPwd", user.getUserPwd());
		request.addProperty("userName", user.getUserName());
		request.addProperty("userSex", user.getUserSex());
		request.addProperty("userAge", user.getUserAge());
		request.addProperty("userPhone", user.getUserPhone());
		request.addProperty("userEmail", user.getUserEmail());
		request.addProperty("pwdQuestion", user.getPwdQuestion());
		request.addProperty("pwdAnswer", user.getPwdAnswer());
		SoapObject result = getResultFromRequest(request);
		if(result == null){
			return new RegisterBean();
		}
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), RegisterBean.class);
	}
	
	public static String publishComment(String loginName , String comment , int id){
		SoapObject request = new SoapObject(Constants.SpaceName,Constants.PublishComment);
		request.addProperty("userLoginName", loginName);
		request.addProperty("fileName", "");
		request.addProperty("heartWeiBo", comment);
		request.addProperty("img", null);
		request.addProperty("classificationId", id);
		SoapObject result = getResultFromRequest(request);
		if(result == null){
			return "";
		}
		try {
			return (new JSONObject(result.getPropertyAsString(0))).getString("PublishState");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<CommentBean> getCommentList(int pageIndex , int id){
		SoapObject request = new SoapObject(Constants.SpaceName,Constants.GetCommentList);
		request.addProperty("pageIndex", pageIndex);
		request.addProperty("pageSize", Constants.PageCount);
		request.addProperty("classificationId", id);
		SoapObject result = getResultFromRequest(request);
		if(result == null){
			return new ArrayList<CommentBean>();
		}
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), new TypeToken<ArrayList<CommentBean>>(){}.getType());
	}
	
	public static BasicBean commentItem(int id, String content){
		SoapObject request = new SoapObject(Constants.SpaceName, Constants.CommentItem);
		request.addProperty("heartWeiBoID", id);
		request.addProperty("reviewUserLoginName", MyApplication.myPhoneNum);
		request.addProperty("reviewContent", content);
		SoapObject result = getResultFromRequest(request);
		if(result == null)
			return new BasicBean();
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), BasicBean.class);
	}
	
	public static ArrayList<CommentDetailBean> getCommentDetail(int id){
		SoapObject request = new SoapObject(Constants.SpaceName, Constants.GetCommentDetail);
		request.addProperty("heartWeiBoID", id);
		SoapObject result = getResultFromRequest(request);
		if(result == null)
			return new ArrayList<CommentDetailBean>();
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), new TypeToken<ArrayList<CommentDetailBean>>(){}.getType());
	}
	
	public static BasicBean inputBlackHole(String input){
		SoapObject request = new SoapObject(Constants.SpaceName, Constants.InputBlackHole);
		request.addProperty("blackHoleContext", input);
		SoapObject result = getResultFromRequest(request);
		if(result == null)
			return new BasicBean();
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), BasicBean.class);
	}
	
	public static ArrayList<ArticleBean> getArticleList(int pageIndex){
		if(pageIndex <= 0){
			return new ArrayList<ArticleBean>();
		}
		SoapObject request = new SoapObject(Constants.SpaceName,Constants.GetArticleListMethod);
		request.addProperty("pageIndex", pageIndex);
		request.addProperty("pgeSize", Constants.PageCount);
		SoapObject result = getResultFromRequest(request, Constants.ArticleWSDL);
		if(result == null)
			return new ArrayList<ArticleBean>();
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), new TypeToken<ArrayList<ArticleBean>>(){}.getType());
	}

	public static ArticleBean getNewestArticle(){
		SoapObject request = new SoapObject(Constants.SpaceName,Constants.GetArticleListMethod);
		request.addProperty("pageIndex", 1);
		request.addProperty("pgeSize", 1);
		SoapObject result = getResultFromRequest(request, Constants.ArticleWSDL);
		if(result == null)
			return new ArticleBean();
		ArrayList<ArticleBean> resultList = MyApplication.mGson.fromJson(result.getPropertyAsString(0), new TypeToken<ArrayList<ArticleBean>>(){}.getType());
		if(resultList.size() > 0){
			return resultList.get(0);
		}
		return new ArticleBean();
	}
	
	public static ArticleBean getArticleInfo(int articleId){
		SoapObject request = new SoapObject(Constants.SpaceName,Constants.GetArticleInfo);
		request.addProperty("articleid", articleId);
		SoapObject result = getResultFromRequest(request, Constants.ArticleWSDL);
		if(result == null)
			return new ArticleBean();
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), ArticleBean.class);
	}
	
	public static ArrayList<ClassBean> getNewestClassList(){
		SoapObject request = new SoapObject(Constants.SpaceName,Constants.GetNewestClassList);
		SoapObject result = getResultFromRequest(request, Constants.ClassWSDL);
		if(result == null)
			return new ArrayList<ClassBean>();
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), new TypeToken<ArrayList<ClassBean>>(){}.getType());
	}
	
	public static ArrayList<ClassBean> getClassList(int pageIndex, int classCategoryId){
		if(pageIndex <= 0){
			return new ArrayList<ClassBean>();
		}
		SoapObject request = new SoapObject(Constants.SpaceName,Constants.GetClassListMethod);
		request.addProperty("pageIndex", pageIndex);
		request.addProperty("pageSize", Constants.PageCount);
		request.addProperty("classificationID", classCategoryId);
		SoapObject result = getResultFromRequest(request, Constants.ClassWSDL);
		if(result == null)
			return new ArrayList<ClassBean>();
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), new TypeToken<ArrayList<ClassBean>>(){}.getType());
	}
	
	public static ArrayList<SpecificUserBean> getSpecificUserList(int pageIndex){
		if(pageIndex <= 0){
			return new ArrayList<SpecificUserBean>();
		}
		SoapObject request = new SoapObject(Constants.SpaceName,Constants.GetSpecificUserList);
		request.addProperty("pageIndex", pageIndex);
		request.addProperty("pageSize", Constants.PageCount);
		SoapObject result = getResultFromRequest(request);
		if(result == null)
			return new ArrayList<SpecificUserBean>();
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), new TypeToken<ArrayList<SpecificUserBean>>(){}.getType());
	}
	
	public static BasicBean followSpecificUser(long specificUserId){
		SoapObject request = new SoapObject(Constants.SpaceName, Constants.FollowSpecificUser);
		request.addProperty("userPhone", MyApplication.myPhoneNum);
		request.addProperty("specificUserID", String.valueOf(specificUserId));
		SoapObject result = getResultFromRequest(request);
		if(result == null)
			return new BasicBean();
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), BasicBean.class);
	}
	
	public static ArrayList<SpecificUserBean> getFollowSpecificUserList(int pageIndex){
		if(pageIndex <= 0){
			return new ArrayList<SpecificUserBean>();
		}
		SoapObject request = new SoapObject(Constants.SpaceName,Constants.GetFollowSpecificUser);
		request.addProperty("pageIndex", pageIndex);
		request.addProperty("pgeSize", Constants.PageCount);
		SoapObject result = getResultFromRequest(request);
		if(result == null)
			return new ArrayList<SpecificUserBean>();
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), new TypeToken<ArrayList<SpecificUserBean>>(){}.getType());
	}
	
	public static BasicBean saveTestResult(String type, String testResult, String allResult){
		if(TextUtils.isEmpty(type) || TextUtils.isEmpty(testResult) || TextUtils.isEmpty(MyApplication.myPhoneNum)){
			return new BasicBean();
		}
		SoapObject request = new SoapObject(Constants.SpaceName, Constants.SaveTestResult);
		request.addProperty("userPhone", MyApplication.myPhoneNum);
		request.addProperty("testTypeName", type);
		request.addProperty("testResult", testResult);
		request.addProperty("allResult", allResult);
		SoapObject result = getResultFromRequest(request);
		if(result == null)
			return new BasicBean();
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), BasicBean.class);
	}
	
	public static BasicBean getMatchResult(long specificId){
		SoapObject request = new SoapObject(Constants.SpaceName, Constants.GetSpecificUserList);
		request.addProperty("UserPhone", MyApplication.myPhoneNum);
		request.addProperty("specificUserIDList", String.valueOf(specificId));
		SoapObject result = getResultFromRequest(request);
		if(result == null)
			return new BasicBean();
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), BasicBean.class);
	}
	
	public static ClassBean getClassInfo(int classId){
		SoapObject request = new SoapObject(Constants.SpaceName,Constants.GetClassInfo);
		request.addProperty("classid", classId);
		SoapObject result = getResultFromRequest(request, Constants.ClassWSDL);
		if(result == null)
			return new ClassBean();
		return MyApplication.mGson.fromJson(result.getPropertyAsString(0), ClassBean.class);
	}
	
	public static String getOnlineDoctor(String userName){
		SoapObject request = new SoapObject(Constants.SpaceName,Constants.GetOnlineDoctor);
		request.addProperty("userName", userName);
		SoapObject result = getResultFromRequest(request);
		if(result == null)
			return "";
		return result.getPropertyAsString(0);
	}
	
	public static class MainTabAdapter extends PagerAdapter{
		private FragmentManager fm;
		private FragmentTransaction ft;
		private List<Fragment> dataList;
		
		public MainTabAdapter(FragmentManager fm , List<Fragment> dataList) {
			this.fm = fm;
			ft = fm.beginTransaction();
			this.dataList = dataList;
		}
		
		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return ((Fragment)object).getView() == view;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Log.i("mylog", "++++++++++++");
			if(ft == null)
				ft = fm.beginTransaction();
			Fragment item = dataList.get(position);
			ft.show(item);
			return item;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			Log.i("mylog", "-------------");
			if(ft == null)
				ft = fm.beginTransaction();
			ft.hide((Fragment)object);
		}
		
		@Override
		public void finishUpdate(ViewGroup container) {
			Log.i("mylog", "uppppppppppppp");
			if(ft != null){
				ft.commit();
				ft = null;
			}
		}
	}
	
	public static class ViewAdapter extends PagerAdapter {
		private ArrayList<View> viewList;

		public ViewAdapter(ArrayList<View> viewList) {
			this.viewList = viewList;
		}

		@Override
		public int getCount() {
			return viewList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(viewList.get(position));
			return viewList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewList.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
	
	public static String getSDPath(){
		boolean isExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if(isExist){
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}
	
	public static boolean validatePhoneNumber(String phoneNumber) {
		Pattern pattern = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = pattern.matcher(phoneNumber);
		return m.matches();
	}
	
	/**
	 * 获取更新的时间
	 * 
	 * @param dateStr
	 * @return
	 * @throws Exception
	 */
	public static String getDateString(Calendar date) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (calendar.get(Calendar.YEAR) - date.get(Calendar.YEAR) > 0) {
			return sdf.format(date.getTime());
		} else if (calendar.get(Calendar.MONTH) - date.get(Calendar.MONTH) > 0) {
			return sdf.format(date.getTime());
		} else if (calendar.get(Calendar.DAY_OF_MONTH) - date.get(Calendar.DAY_OF_MONTH) > 6) {
			return sdf.format(date.getTime());
		} else if ((calendar.get(Calendar.DAY_OF_MONTH) - date.get(Calendar.DAY_OF_MONTH) > 0) && 
				(calendar.get(Calendar.DAY_OF_MONTH) - date.get(Calendar.DAY_OF_MONTH) < 6)) {
			int i = calendar.get(Calendar.HOUR_OF_DAY) - date.get(Calendar.HOUR_OF_DAY);
			return i + "天前";
		} else if (calendar.get(Calendar.HOUR_OF_DAY) - date.get(Calendar.HOUR_OF_DAY) > 0) {
			int i = calendar.get(Calendar.HOUR_OF_DAY) - date.get(Calendar.HOUR_OF_DAY);
			return i + "小时前";
		} else if (calendar.get(Calendar.MINUTE) - date.get(Calendar.MINUTE) > 0) {
			int i = calendar.get(Calendar.MINUTE) - date.get(Calendar.MINUTE);
			return i + "分钟前";
		} else if (calendar.get(Calendar.SECOND) - date.get(Calendar.SECOND) > 0) {
			int i = calendar.get(Calendar.SECOND) - date.get(Calendar.SECOND);
			return i + "秒前";
		} else if (calendar.get(Calendar.SECOND) - date.get(Calendar.SECOND) == 0) {
			return "刚刚";
		} else {
			return sdf.format(date);
		}
	}
	
	
	/*static HostnameVerifier hv = new HostnameVerifier() {  
		@Override
		public boolean verify(String urlHostName, SSLSession session) {  
	        System.out.println("Warning: URL Host: " + urlHostName + " vs. "  
	                           + session.getPeerHost());  
	        return true;  
	    }
	
	};  
	  
	private static void trustAllHttpsCertificates() throws Exception {  
	    javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];  
	    javax.net.ssl.TrustManager tm = new miTM();  
	    trustAllCerts[0] = tm;  
	    javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext  
	            .getInstance("SSL");  
	    sc.init(null, trustAllCerts, null);  
	    javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc  
	            .getSocketFactory());  
	}  
	
	static class miTM implements javax.net.ssl.TrustManager,  
	        javax.net.ssl.X509TrustManager {  
	    public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
	        return null;  
	    }  
	
	    public boolean isServerTrusted(  
	            java.security.cert.X509Certificate[] certs) {  
	        return true;  
	    }  
	
	    public boolean isClientTrusted(  
	            java.security.cert.X509Certificate[] certs) {  
	        return true;  
	    }  
	
	    public void checkServerTrusted(  
	            java.security.cert.X509Certificate[] certs, String authType)  
	            throws java.security.cert.CertificateException {  
	        return;  
	    }  
	
	    public void checkClientTrusted(  
	            java.security.cert.X509Certificate[] certs, String authType)  
	            throws java.security.cert.CertificateException {  
	        return;  
	    }  
	}  
	
		try {
			trustAllHttpsCertificates();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	*/
}
