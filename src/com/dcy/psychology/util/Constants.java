package com.dcy.psychology.util;

public interface Constants {
	public static final boolean DebugMode = true;
	
	public static final String SpaceUrl = "http://114.215.179.130/";
	public static final String SpaceName = "http://114.215.179.130";
	public static final String IMAddress = "114.215.179.130";
	public static final int IMPort = 6222;
	public static final String IMDefaultGroup = "IMFriends";
	public static final int TimeOut = 15000;
	public static final int PageCount = 8;
	
	public static final String LoginMethod = "Login"; 
	public static final String GetArticleMethod = "GetArticleList";
	public static final String RegisterUserMethod = "RegisterUser";
	public static final String PublishComment = "PublishHeartWeiBo";
	public static final String GetCommentList = "GetHeartWeiBoList";
	public static final String CommentItem = "CommentHertWeiBo";
	public static final String GetCommentDetail = "GetCommentDetail";
	public static final String InputBlackHole = "InputBlackHole";
	public static final String ChangePwdMethod = "UpdateUserPwd";
	
	public static final String UserWSDL = "http://114.215.179.130/WebService/WebServiceAPPUser.asmx";
	public static final String ArticleWSDL = "http://114.215.179.130/WebService/WebServiceAPPArticle.asmx";

	public static final int[] IdOfGrowMode = {1 , 2, 3 ,4};

	public static final String ReceiverAction_CursorChange = "cursor_change";
	
	public static final String ThemeIndex = "themeIndex";
	public static final String IsSpecial = "isSpecial";
	public static final String BeanList = "beanList";
	public static final String Level = "level";
	public static final String ThemeTitle = "themeTitle";
	public static final String PictureBean = "pictureBean";
	public static final String GrowModelBean = "growModelBean";
	
	public static final String RoleUser = "USER";
	public static final String RoleDoctor = "DOCTOR";
	
	public static final String ListType = "listType";
	public static final String PicType = "type_pic";
	public static final String TestType = "type_test";
	public static final String SpecialTestType = "type_special_type";
	
	public static final String[][] GrowTestTitle = {{"�˼ʽ�������","�˼ʽ������ŶȲ���","�˼ʽ��Ǹв���","�˼��е���������"},
													{"ʱ�������������"},{"������Բ���"},{"����"}};

	public static final String[][] SpecialGrowTestTitle = {{"��������" , "��������" , "��������"}};
	public static final String[] HomePageTestTitle = {"�����׳����" , "�����������ܲ�����ϣ����˸��" ,"���ŭ���ɶ���","��¶���"};
	
	
}
