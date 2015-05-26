package com.dcy.psychology.db;

import java.io.IOException;
import java.io.InputStream;

import com.dcy.psychology.util.IOUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

public class PreInstallDbHelper extends SQLiteOpenHelper{
	private Context mContext;
	private static final String DbName = "city.db";
	private static final int DbVersion = 1;
	
	public PreInstallDbHelper(Context context){
		super(context, DbName, null, DbVersion);
		mContext = context;
	}
	
	//只有在有数据操作(延迟调用)的时候，才会创建表，如果表已存在，则不会再调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			Log.i("mylog", "start >>>>>>>>>>>>>>>>>>>>>>>");
			executeSQL(db, mContext.getAssets().open("tbl_cities.sql"));
			executeSQL(db, mContext.getAssets().open("tbl_provinces.sql"));
			executeSQL(db, mContext.getAssets().open("tbl_university.sql"));
			Log.i("mylog", "end <<<<<<<<<<<<<<<<<<<<<<<<<<<");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void executeSQL(SQLiteDatabase db, InputStream inputStream) {
		db.beginTransaction();
		try {
			final String content = IOUtils.readInputStream(inputStream);
			final String[] statements = content.split(";");
			for (String str : statements) {
				str = str.trim();
				if (!TextUtils.isEmpty(str))
					db.execSQL(str);
			}
			db.setTransactionSuccessful();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
	
	//更新版本时使用,只有版本号不一致时调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("DbHelper","onUpgrade");
//		String drop = "drop table if exists %s";
//		for(String table:tableNames){
//			String sql = String.format(drop, table);
//			db.execSQL(sql);
//		}
		onCreate(db);
	}
	//数据库的操作
	public long insert(String table,ContentValues values){
		return getWritableDatabase().insert(table, null, values);
	}
	
	public int update(String table,ContentValues values,
			String where,String...whereArgs){
		return getWritableDatabase().update(table, values, where, whereArgs);
	}

	public int delete(String table,String where,String...whereArgs){
		return getWritableDatabase().delete(table, where, whereArgs);
	}
	
	public Cursor query(String table,String[] columns,String orderBy,
			String selection,String...selectionArgs){
		return getReadableDatabase().query(table, columns, selection, selectionArgs, null, null, orderBy);
	}
	
	public Cursor query(String sql,String...args){
		return getReadableDatabase().rawQuery(sql, args);
	}
}
