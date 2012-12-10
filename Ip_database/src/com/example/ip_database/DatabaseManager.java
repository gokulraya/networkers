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
	private final int DB_VERSION=5;
	
	private final String TABLE_NAME="database_table";
	private final String TABLE_ROW_IP="table_row_ip";
	//primary key set as auto increment
	private final String TABLE_ROW_ID="id";
	
	private final String TABLE_ROW_Site="table_row_two";
	private final String TABLE_ROW_Action="table_row_rule";
	private final String TABLE_ROW_Port="table_row_port";
	//*****Initialise CTIME and UTIME here itself***************
	private final String TABLE_ROW_CTIME="table_row_ctime";
	private final String TABLE_ROW_UTIME="table_row_utime";
	public static final String TAG="delete";
	//public static final String RETRIEVE="first value";
	//public static final String GET="second value";
	
	public DatabaseManager(Context context)
	{
		this.context=context;
		CustomSQLiteOpenHelper helper=new CustomSQLiteOpenHelper(context);
		this.db=helper.getWritableDatabase();
	}
	public void addRow(String rowStringOne,String rowStringTwo,String rowStringThree,String rowStringFour)
	{
		String ip,site,action,port,ctime,utime;
		ContentValues values=new ContentValues();
		values.put(TABLE_ROW_IP,rowStringOne);
		values.put(TABLE_ROW_Site, rowStringTwo);
		values.put(TABLE_ROW_Action, rowStringThree);
		values.put(TABLE_ROW_Port, rowStringFour);
		//values.put(TABLE_ROW_CTIME, rowStringFive);
		//values.put(TABLE_ROW_UTIME, rowStringSix);
		//Seperate insert and selsct query from here
		
		try{db.insert(TABLE_NAME, null, values);
			Log.d(TAG, rowStringOne);
			Log.d(TAG, rowStringTwo);
			Log.d(TAG, rowStringThree);
			Log.d(TAG, rowStringFour);
			//String select="select * from " + TABLE_NAME;
			Cursor c=db.rawQuery("select * from " + TABLE_NAME,null);
			if(c!= null)
			{
				if(c.moveToFirst())
				{
					do
					{
						ip=c.getString(c.getColumnIndex(TABLE_ROW_IP));
						site=c.getString(c.getColumnIndex(TABLE_ROW_Site));
						action=c.getString(c.getColumnIndex(TABLE_ROW_Action));
						port=c.getString(c.getColumnIndex(TABLE_ROW_Port));
						//ctime=c.getString(c.getColumnIndex(TABLE_ROW_CTIME));
						//utime=c.getString(c.getColumnIndex(TABLE_ROW_UTIME));
						//Log.d(RETRIEVE, id);
						//Log.d(GET,two);
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
					TABLE_NAME, new String[]{TABLE_ROW_ID,TABLE_ROW_IP,TABLE_ROW_Site,TABLE_ROW_Action,TABLE_ROW_Port}, 
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
					dataList.add(cursor.getString(2));
					dataList.add(cursor.getString(3));
					dataList.add(cursor.getString(4));
					//dataList.add(cursor.getString(5));
					//dataList.add(cursor.getString(6));
					
					
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
	public void deleteRow(long rowID)
	{
		try{db.delete(TABLE_NAME, TABLE_ROW_ID + "=" + rowID,null );
			//Log.d(TAG,"success");
			}
		catch(Exception e)
		{
			Log.e("DB Error",e.toString());
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
							TABLE_ROW_Site + " text," +
							TABLE_ROW_Action + " text," +
							TABLE_ROW_Port + " integer," + 
							TABLE_ROW_CTIME + " long," +
							TABLE_ROW_UTIME + " long" +
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


