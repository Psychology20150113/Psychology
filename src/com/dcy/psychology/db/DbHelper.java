package com.dcy.psychology.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper{
	private String[] createTables;
	private String[] tableNames;
	
	public DbHelper(Context context, String name,
			int version,String...createTables) {
		super(context, name, null, version);
		this.createTables = createTables;
		//���в�ֻ�ñ���
		tableNames =  parseTables(createTables);
	}
	private String[] parseTables(String...createTables){
		if(tableNames != null)return tableNames;
		int count = createTables.length;
		if(count == 0)
			return null;
		String[] tableNames = new String[count];
		for (int i = 0; i < count; i++) {
			tableNames[i] = parse(createTables[i]);
		}
		return tableNames;
	}
	//CREATE  table tableName 
	//create table if not exists tableName
	private String parse(String sql) {
//		if(null == sql || sql.isEmpty())
//			return null;
		//�Կո�ָ�ÿ��sql��䣬ȡ�ñ���
		String[] arr = sql.split("\\s+|\\(");
		int i = find(arr,"exists");
		//�ж�������sql��������е�����
		if (i == -1)
			i = find(arr, "table");
		return arr[i+1];
	}
	private int find(String[] arr, String string) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equalsIgnoreCase(string)) 
				return i;
		}
		return -1;
	}
	//ֻ���������ݲ���(�ӳٵ���)��ʱ�򣬲Żᴴ����������Ѵ��ڣ��򲻻��ٵ���
	@Override
	public void onCreate(SQLiteDatabase db) {
		for (String sql:createTables) {
			db.execSQL(sql);
		}
	}
	//���°汾ʱʹ��,ֻ�а汾�Ų�һ��ʱ����
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("DbHelper","onUpgrade");
		String drop = "drop table if exists %s";
		for(String table:tableNames){
			String sql = String.format(drop, table);
			db.execSQL(sql);
		}
		onCreate(db);
	}
	//���ݿ�Ĳ���
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
