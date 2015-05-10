package com.dcy.psychology.util;

public interface Constants {
	public static final boolean DebugMode = false;
	
	public static final String SpaceUrl = "http://114.215.179.130/";
	public static final String SpaceName = "http://114.215.179.130";
	public static final String IMAddress = "114.215.179.130";
	public static final int IMPort = 6222;
	public static final String IMDefaultGroup = "IMFriends";
	public static final int TimeOut = 15000;
	public static final int PageCount = 8;
	
	public static final String LoginMethod = "Login"; 
	public static final String RegisterUserMethod = "RegisterUser";
	public static final String PublishComment = "PublishHeartWeiBo";
	public static final String GetCommentList = "GetHeartWeiBoList";
	public static final String CommentItem = "CommentHertWeiBo";
	public static final String GetCommentDetail = "GetCommentDetail";
	public static final String InputBlackHole = "InputBlackHole";
	public static final String SendSMS = "SendSMS";
	public static final String SendFindSMS = "SendSMSFindPwd";
	public static final String VerifySmsCode = "VerifySmsCode";
	public static final String VerifyFindSmsCode = "VerifySmsCodeFindPwd";
	public static final String GetArticleListMethod = "GetArticleList";
	public static final String GetArticleInfo = "GetArticleInfo";
	public static final String GetOnlineDoctor = "GetOnlineDoctor";
	public static final String ChangePwdMethod = "UpdateUserPwd";
	
	public static final String UserWSDL = "http://114.215.179.130/WebService/WebServiceAPPUser.asmx";
	public static final String ArticleWSDL = "http://114.215.179.130/WebService/WebServiceAPPArticle.asmx";
	public static final String ClassWSDL = "http://114.215.179.130/webservice/WebServiceAppClass.asmx";
	
	public static final int[] IdOfGrowMode = {1 , 2, 3 ,4};
	public static final int Id_Class_Teach = 1;
	public static final int Id_Class_Homework = 2;
	public static final int Id_Class_Game = 3;

	public static final String ReceiverAction_CursorChange = "cursor_change";
	public static final String ReceiverAction_LoginSuccess = "login_success";
	
	public static final String ThemeIndex = "themeIndex";
	public static final String IsSpecial = "isSpecial";
	public static final String BeanList = "beanList";
	public static final String Level = "level";
	public static final String ThemeTitle = "themeTitle";
	public static final String PictureBean = "pictureBean";
	public static final String GrowModelBean = "growModelBean";
	public static final String OnlineArticleId = "onlineArticleId";
	
	public static final String RoleUser = "USER";
	public static final String RoleDoctor = "DOCTOR";
	
	public static final String ListType = "listType";
	public static final String PicType = "type_pic";
	public static final String TestType = "type_test";
	public static final String SpecialTestType = "type_special_type";
	
	public static final String[][] GrowTestTitle = {{"人际交往测试","人际交往诚信度测试","人际焦虑感测试","人际中的情绪测试"},
													{"时间管理能力测试"},{"恋爱配对测试"},{"自信"}};

	public static final String[][] SpecialGrowTestTitle = {{"抑郁测试" , "抑郁自评" , "焦虑自评"}};
	public static final String[] HomePageTestTitle = {"你容易成瘾吗？" , "你眼中乌云密布还是希望闪烁？" ,"你会怒不可遏吗？","你孤独吗？",
													"自控力测量", "你会患冠心病吗？", "你能读懂身体语言吗?", "你的幽默感怎么样?"};
}
