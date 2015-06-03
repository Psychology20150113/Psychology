package com.dcy.psychology.gsonbean;

public class LoginBean {
	private String Result;
	private String Reason;
	private String LoginState;
	private boolean IsPrefectUserInfo;
	
	public String getResult() {
		return Result;
	}
	public void setResult(String result) {
		Result = result;
	}
	public String getReason() {
		return Reason;
	}
	public void setReason(String reason) {
		Reason = reason;
	}
	public String getLoginState() {
		return LoginState;
	}
	public void setLoginState(String loginState) {
		LoginState = loginState;
	}
	public boolean isIsPrefectUserInfo() {
		return IsPrefectUserInfo;
	}
	public void setIsPrefectUserInfo(boolean isPrefectUserInfo) {
		IsPrefectUserInfo = isPrefectUserInfo;
	}
}
