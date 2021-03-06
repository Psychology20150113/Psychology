package com.dcy.psychology.db;

public class SqlConstants {
	
	public static final int DbVersion = 1;
	public static final String DBName = "record.db";
	public static final String TableName = "records";
	public static final String MissionKey = "mission";
	public static final String IndexKey = "itemIndex";
	public static final String DescriptionKey = "description";
	public static final String MissionTypeKey = "missionType";
	public static final String LibTypeKey = "libType";
	public static final String ThemeIndexKey = "themeIndex";
	public static final String LevelKey = "level";
	public static final String TimeKey = "time";
	
	public static final String CreateTableSql = 
			"create table if not exists records(_id integer primary key,"
			+ "itemIndex integer,"
			+ "mission text not null,"
			+ "description text,"
			+ "missionType text,"
			+ "libType integer,"
			+ "themeIndex integer,"
			+ "level integer,"
			+ "time text)";
	
	public static final String SelectSql = 
			"select itemIndex,description from records " +
			"where mission = ? and level = ?";
	
	public static final String SelectCountByTimeSql = 
			"select itemIndex from records " + 
			"where mission = ? and time > ? and time < ?";

	public static final String SelectNewRecordsSql = 
			"select * from records order by time desc";

	/* pre_install_db */
	public static final String SelectProvinceSql = 
			"select * from tbl_provinces";
	
	public static final String SelectCitySql = 
			"select * from tbl_cities where ProvinceID = ?";
	
	public static final String SelectUniversitySql = 
			"select * from tbl_university where ProvinceID = ?";
}
