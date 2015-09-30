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
	public static final String Api_Success = "ok";
	
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
	public static final String GetNewestClassList = "GetNewestClassList";
	public static final String GetClassListMethod = "GetClassList";
	public static final String GetClassInfo = "GetClassInfo";
	public static final String GetOnlineDoctor = "GetOnlineDoctor";
	public static final String ChangePwdMethod = "UpdateUserPwd";
	public static final String PrefectInfoMethod = "PrefectUserInfo";
	public static final String GetSpecificUserList = "GetSpecificUserList";
	public static final String FollowSpecificUser = "FollowSpecificUser";
	public static final String GetFollowSpecificUser = "GetFollowSpecificUser";
	public static final String SaveTestResult = "SaveTestResult";
	public static final String GetMatchResult = "GetSpecificUserMatch";
	public static final String RemoveSpecificUser = "RemoveSpecificUser";
	public static final String GetUserInfo = "GetUserInfo";
	public static final String GetQiniuToken = "GetUploadToken";
	public static final String GetMatchestSpecificList = "GetMostSpecificUserMatch";
	public static final String UpdataUserHeader = "UpdateUserHeadUrl";
	public static final String CheckAppVersion = "CheckAppVersion";
	
	public static final String Type_Zhiye_Test = "HollendTest";
	public static final String Type_Qiazhi_Test = "TemperamentTest";
	
	
	public static final String UserWSDL = "http://114.215.179.130/WebService/WebServiceAPPUser.asmx";
	public static final String ArticleWSDL = "http://114.215.179.130/WebService/WebServiceAPPArticle.asmx";
	public static final String ClassWSDL = "http://114.215.179.130/webservice/WebServiceAppClass.asmx";
	
	public static final String Web_Problem_Url = "http://114.215.179.130/webservice/QandAInfo.html";
	
	public static final int[] IdOfGrowMode = {1 , 2, 3 ,4};
	public static final int Id_Class_Teach = 1;
	public static final int Id_Class_Homework = 2;
	public static final int Id_Class_Game = 3;

	public static final int Apply_Wating = 0;
	public static final int Apply_Success = 1;
	public static final int Apply_end = 2;
	
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
	public static final String OnlineClassId = "onlineClassId";
	public static final String ClassCategoryId = "classCategoryId";
	public static final String ClassCategoryName = "classCategoryName";
	public static final String PhoneNum = "phoneNum";
	public static final String UserBean = "userBean";
	public static final String QizhiResult = "qizhiResult";
	public static final String ZhiyeResult = "zhiyeResult";
	public static final String Params = "params";
	public static final String TitleName = "title_name";
	public static final String Result = "result_name";
	
	public static final String UserRole = "user_role";
	public static final String RoleUser = "USER";
	public static final String RoleTeacher = "TEACHER";
	
	public static final String ListType = "listType";
	public static final String PicType = "type_pic";
	public static final String TestType = "type_test";
	public static final String SpecialTestType = "type_special_type";
	public static final String DNAType = "type_dna";
	
	public static final int[][] ZhiyeIndex = {{7, 19, 29, 39, 41, 51, 57, 5, 18, 40},
											  {2, 13, 22, 36, 43, 14, 23, 44, 47, 48},
											  {6, 8, 20, 30, 31, 42, 21, 55, 56, 58},
											  {11, 24, 28, 35, 38, 46, 60, 3, 16, 25},
											  {26, 37, 52, 59, 1, 12, 15, 27, 45, 53},
											  {4, 9, 10, 17, 33, 34, 49, 50, 54, 32}};
	
	public static final int[][] QizhiIndex = {{2, 6, 9, 14, 17, 21, 27, 31, 36, 38, 42, 48, 50, 54, 58},
											  {4, 8, 11, 16, 19, 23, 25, 29, 34, 40, 44, 46, 52, 56, 60},
											  {1, 7, 10, 13, 18, 22, 26, 30, 33, 39, 43, 45, 49, 55, 57},
											  {3, 5, 12, 15, 20, 24, 28, 32, 35, 37, 41, 47, 51, 53, 59}};
	
	public static final String[][] GrowTestTitle = {{"人际交往测试","人际交往诚信度测试","人际焦虑感测试","人际中的情绪测试"},
											{"时间管理能力测试"},{"恋爱配对测试"},{"自信"}};
	public static final String[][] SpecialGrowTestTitle = {{"抑郁测试" , "抑郁自评" , "焦虑自评"}};
	public static final String[] HomePageTestTitle = {"你容易成瘾吗？" , "你眼中乌云密布还是希望闪烁？" ,"你会怒不可遏吗？","你孤独吗？",
											"自控力测量", "你会患冠心病吗？", "你能读懂身体语言吗?", "你的幽默感怎么样?"};
}
