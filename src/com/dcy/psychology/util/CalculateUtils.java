package com.dcy.psychology.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.dcy.psychology.R;
import com.dcy.psychology.gsonbean.BasicBean;

public class CalculateUtils {
	public static String[] Code_Zhiye = {"C", "R", "I", "E", "S", "A"};
	public static final String Type_Hollend = "HollendTest";
	public static final String Type_QiZhi = "TemperamentTest";
			
	private static class PointItem {
		public String nameItem;
		public String codeItem;
		public int point;
		public PointItem(String name, String code, int point) {
			this.nameItem = name;
			this.codeItem = code;
			this.point = point;
		}
	}
	
	public static HashMap<String, String> calculateZhiyeResult(Context mContext, ArrayList<Integer> pointList){
		if(pointList == null){
			return null;
		}
		String[] typeArray = mContext.getResources().getStringArray(R.array.zhiye_array);
		int typePoint = 0;
		StringBuilder resultBuilder = new StringBuilder();
		ArrayList<PointItem> typeList = new ArrayList<CalculateUtils.PointItem>();
		for(int i = 0 ; i < typeArray.length; i ++){
			for(int j = 0 ; j < Constants.ZhiyeIndex[i].length; j ++){
				typePoint += pointList.get(Constants.ZhiyeIndex[i][j] - 1);
			}
			typeList.add(new PointItem(typeArray[i], Code_Zhiye[i], typePoint));
			resultBuilder.append(typeArray[i]).append(typePoint).append("\n");
			typePoint = 0;
		}
		StringBuilder pointBuilder = new StringBuilder();
		for(Integer point : pointList){
			pointBuilder.append(point).append(",");
		}
		Collections.sort(typeList, new Comparator<PointItem>() {
			@Override
			public int compare(PointItem lhs, PointItem rhs) {
				if(lhs.point > rhs.point){
					return -1;
				} else if(lhs.point < rhs.point){
					return 1;
				} else {
					return 0;
				}
			}
		});
		StringBuilder typeBuilder = new StringBuilder();
		for(int i = 0 ; i < 3 ; i ++){
			typeBuilder.append(typeList.get(i).codeItem);
		}
		HashMap<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("testType", Type_Hollend);
		resultMap.put("showResult", resultBuilder.substring(0, resultBuilder.length() - 1));
		resultMap.put("pointResult", pointBuilder.substring(0, pointBuilder.length() - 1));
		resultMap.put("typeResult", typeBuilder.toString());
		return resultMap;
	}
	
	
	
	public static HashMap<String, String> calculateQizhiResult(Context mContext, ArrayList<Integer> pointList){
		if(pointList == null){
			return null;
		}
		String[] typeArray = mContext.getResources().getStringArray(R.array.qizhi_array);
		int typePoint = 0;
		StringBuilder resultBuilder = new StringBuilder();
		ArrayList<PointItem> typeList = new ArrayList<CalculateUtils.PointItem>();
		for(int i = 0 ; i < typeArray.length; i ++){
			for(int j = 0 ; j < Constants.QizhiIndex[i].length; j ++){
				typePoint += pointList.get(Constants.QizhiIndex[i][j] - 1);
			}
			typeList.add(new PointItem(typeArray[i], "", typePoint));
			resultBuilder.append(typeArray[i]).append(typePoint).append("\n");
			typePoint = 0;
		}
		StringBuilder pointBuilder = new StringBuilder();
		for(Integer point : pointList){
			pointBuilder.append(point).append(",");
		}
		Collections.sort(typeList, new Comparator<PointItem>() {
			@Override
			public int compare(PointItem lhs, PointItem rhs) {
				if(lhs.point > rhs.point){
					return -1;
				} else if(lhs.point < rhs.point){
					return 1;
				} else {
					return 0;
				}
			}
		});
		StringBuilder typeBuilder = new StringBuilder();
		for(int i = 0 ; i < 2 ; i ++){
			typeBuilder.append(typeList.get(i).nameItem).append(",");
		}
		HashMap<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("testType", Type_QiZhi);
		resultMap.put("showResult", resultBuilder.substring(0, resultBuilder.length() - 1));
		resultMap.put("pointResult", pointBuilder.substring(0, pointBuilder.length() - 1));
		resultMap.put("typeResult", typeBuilder.substring(0, typeBuilder.length() - 1));
		return resultMap;
	}
	
	public static class SaveTestResultTask extends AsyncTask<Void, Void, BasicBean>{
		private Context mContext;
		private HashMap<String, String> resultMap;
		
		public SaveTestResultTask(Context mContext, HashMap<String, String> resultMap) {
			this.mContext = mContext;
			this.resultMap = resultMap;
		}
		
		@Override
		protected BasicBean doInBackground(Void... params) {
			if(resultMap == null){
				return null;
			}
			return Utils.saveTestResult(resultMap.get("testType"), resultMap.get("typeResult")
					, resultMap.get("pointResult"));
		}
		
		@Override
		protected void onPostExecute(BasicBean result) {
			if(result.isResult()){
				Toast.makeText(mContext, mContext.getString(R.string.Upload_Result_Success), Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, result.getReason(), Toast.LENGTH_SHORT).show();
			}
		}
	}
}
