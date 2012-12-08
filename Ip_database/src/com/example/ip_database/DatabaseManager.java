package com.example.ip_database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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
	public ArrayList<ArrayList<Object>> getAllRowsAsArrays()
	{
		ArrayList<ArrayList<Object>> dataArrays=new ArrayList<ArrayList<Object>>();
		Cursor cursor;
		try
		{
			cursor=db.query(
					TABLE_NAME, new String[]{TABLE_ROW_ID,TABLE_ROW_IP,TABLE_ROW_TWO}, 
					null, null, null, null, null);
			cursor.moveToFirst();
			if(!cursor.isAfterLast())
			{
				do
				{
					ArrayList<Object> dataList=new ArrayList<Object>();
					dataList.add(cursor.getLong(0));
					dataList.add(cursor.getString(1));
					dataList.add(cursor.getString(2));
					
					dataArrays.add(dataList);
				}while(cursor.moveToNext());
			}
		}
		catch(SQLException e)
		{
			Log.e("DB Error",e.toString());
			e.printStackTrace();
		}
		return dataArrays;
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


