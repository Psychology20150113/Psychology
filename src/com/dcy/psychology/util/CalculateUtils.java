package com.dcy.psychology.util;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;

import com.dcy.psychology.R;

public class CalculateUtils {
	public static String calculateZhiyeResult(Context mContext, ArrayList<Integer> pointList){
		if(pointList == null){
			return "";
		}
		String[] typeArray = mContext.getResources().getStringArray(R.array.zhiye_array);
		int typePoint = 0;
		StringBuilder resultBuilder = new StringBuilder();
		for(int i = 0 ; i < typeArray.length; i ++){
			for(int j = 0 ; j < Constants.ZhiyeIndex[i].length; j ++){
				typePoint += pointList.get(Constants.ZhiyeIndex[i][j] - 1);
			}
			resultBuilder.append(typeArray[i]).append(typePoint).append("\n");
			typePoint = 0;
		}
		return resultBuilder.toString();
	}
	
	
	
	public static String calculateQizhiResult(Context mContext, ArrayList<Integer> pointList){
		if(pointList == null){
			return "";
		}
		String[] typeArray = mContext.getResources().getStringArray(R.array.qizhi_array);
		int typePoint = 0;
		StringBuilder resultBuilder = new StringBuilder();
		for(int i = 0 ; i < typeArray.length; i ++){
			for(int j = 0 ; j < Constants.QizhiIndex[i].length; j ++){
				typePoint += pointList.get(Constants.QizhiIndex[i][j] - 1);
			}
			resultBuilder.append(typeArray[i]).append(typePoint).append("\n");
			typePoint = 0;
		}
		return resultBuilder.toString();
	}
	
	
}
