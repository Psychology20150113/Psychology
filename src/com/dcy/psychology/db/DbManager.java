package com.dcy.psychology.db;

import java.util.ArrayList;

import android.database.Cursor;

import com.dcy.psychology.MyApplication;
import com.dcy.psychology.model.IdAndName;

public class DbManager {
	public static ArrayList<IdAndName> getProvince(){
		ArrayList<IdAndName> provinceList = new ArrayList<IdAndName>();
		Cursor mCursor = MyApplication.preInstallDbHelper.query(SqlConstants.SelectProvinceSql);
		if(mCursor.getCount() == 0){
			return provinceList;
		}
		while (mCursor.moveToNext()) {
			IdAndName itemMode = new IdAndName();
			itemMode.id = mCursor.getInt(mCursor.getColumnIndex("ProvinceID"));
			itemMode.name = mCursor.getString(mCursor.getColumnIndex("Province"));
			provinceList.add(itemMode);
		}
		mCursor.close();
		return provinceList;
	}

	public static ArrayList<IdAndName> getCity(int provinceId){
		ArrayList<IdAndName> cityList = new ArrayList<IdAndName>();
		Cursor mCursor = MyApplication.preInstallDbHelper.query(SqlConstants.SelectCitySql, String.valueOf(provinceId));
		while (mCursor.moveToNext()) {
			IdAndName itemMode = new IdAndName();
			itemMode.id = mCursor.getInt(mCursor.getColumnIndex("CityID"));
			itemMode.name = mCursor.getString(mCursor.getColumnIndex("City"));
			cityList.add(itemMode);
		}
		mCursor.close();
		return cityList;
	}
	
	public static ArrayList<IdAndName> getUniversity(int provinceId){
		ArrayList<IdAndName> cityList = new ArrayList<IdAndName>();
		Cursor mCursor = MyApplication.preInstallDbHelper.query(SqlConstants.SelectUniversitySql, String.valueOf(provinceId));
		while (mCursor.moveToNext()) {
			IdAndName itemMode = new IdAndName();
			itemMode.id = mCursor.getInt(mCursor.getColumnIndex("UniversityID"));
			itemMode.name = mCursor.getString(mCursor.getColumnIndex("UniveName"));
			cityList.add(itemMode);
		}
		mCursor.close();
		return cityList;
	}
}
