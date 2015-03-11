package com.dcy.psychology.db;

public class SqlConstants {
	
	public static final String DBName = "record.db";
	public static final String TableName = "records";
	public static final String MissionKey = "mission";
	public static final String IndexKey = "itemIndex";
	public static final String DescriptionKey = "description";
	public static final String TimeKey = "time";
	
	public static final String CreateTableSql = 
			"create table if not exists records(_id integer primary key,"
			+ "itemIndex integer,"
			+ "mission text not null,"
			+ "description text,"
			+ "time text)";
	
	public static final String SelectSql = 
			"select itemIndex,description from records " +
			"where mission = ?";
	
	public static final String SelectCountByTimeSql = 
			"select itemIndex from records " + 
			"where mission = ? and time > ? and time < ?";
}
