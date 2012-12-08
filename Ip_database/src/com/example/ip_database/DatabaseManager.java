package com.example.ip_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager {
	Context context;
	private SQLiteDatabase db;
	private final String DB_NAME="database_name";
	private final int DB_VERSION=4;
	
	private final String TABLE_NAME="database_table";
	private final String TABLE_ROW_IP="table_row_one";
	private final String TABLE_ROW_ID="id";
	private final String TABLE_ROW_TWO="table_row_two";
	public static final String TAG="insert";
	public static final String RETRIEVE="first value";
	public static final String GET="second value";
	
	public DatabaseManager(Context context)
	{
		this.context=context;
		CustomSQLiteOpenHelper helper=new CustomSQLiteOpenHelper(context);
		this.db=helper.getWritableDatabase();
	}
	public void addRow(String rowStringOne,String rowStringTwo)
	{
		String id,two;
		ContentValues values=new ContentValues();
		values.put(TABLE_ROW_IP,rowStringOne);
		values.put(TABLE_ROW_TWO, rowStringTwo);
		
		try{db.insert(TABLE_NAME, null, values);
			Log.d(TAG,"Success");
			//String select="select * from " + TABLE_NAME;
			Cursor c=db.rawQuery("select * from " + TABLE_NAME,null);
			if(c!= null)
			{
				if(c.moveToFirst())
				{
					do
					{
						id=c.getString(c.getColumnIndex(TABLE_ROW_IP));
						two=c.getString(c.getColumnIndex(TABLE_ROW_TWO));
						Log.d(RETRIEVE, id);
						Log.d(GET,two);
					}while(c.moveToNext());
				}
			
					//.rawQuery("select * from " + TABLE_NAME);
					//db.execSQL(select);
			//id=c.getString(c.getColumnIndex(TABLE_ROW_IP));
			//two=c.getString(c.getColumnIndex(TABLE_ROW_TWO));
			//Log.d(RETRIEVE, id);
			//Log.d(GET,two);
		}
		}
		catch(Exception e)
		{
			Log.e("DB ERROR",e.toString());
			e.printStackTrace();
		}
	
	}
	
	private class CustomSQLiteOpenHelper extends SQLiteOpenHelper
	{
		public CustomSQLiteOpenHelper(Context context)
		{
			super(context,DB_NAME,null,DB_VERSION);
		}
		public void onCreate(SQLiteDatabase db)
		{
			String newQuery="create table " + 
							TABLE_NAME + 
							" (" + 
							TABLE_ROW_ID + " integer primary key autoincrement not null," +
							TABLE_ROW_IP + " text," + 
							TABLE_ROW_TWO + " text" +
							");";
			db.execSQL(newQuery);
			String select="select * from " + TABLE_NAME;
			db.execSQL(select);
		}
		public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
			onCreate(db);
			
		}
	}	
}


