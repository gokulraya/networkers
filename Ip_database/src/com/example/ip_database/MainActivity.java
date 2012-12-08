package com.example.ip_database;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

public class MainActivity extends Activity {

	
	EditText edit_Text,edit_Text2;
	Button button;
	TableLayout dataTable;
	DatabaseManager db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db=new DatabaseManager(this);
		setupViews();
		addButtonListeners();
		//updateTable();
		//Log.v("Successful", null);
		}
		catch( Exception e)
		{
			Log.e("EEROR",e.toString());
			e.printStackTrace();
		}
	}


	private void setupViews()
	{
		dataTable=(TableLayout)findViewById(R.id.data_table);
		edit_Text=(EditText)findViewById(R.id.editText1);
		button=(Button)findViewById(R.id.button1);
		edit_Text2=(EditText)findViewById(R.id.editText2);
		
	}
	private void addButtonListeners()
	{
		button.setOnClickListener
		(
			new View.OnClickListener() 
			{
				
				@Override public void onClick(View v) {addRow();}
				
			}
		);
	}
	
	
	private void addRow()
	{
		try
		{
			db.addRow
			(
					edit_Text.getText().toString(),
					edit_Text2.getText().toString()
			);
			//updateTable();
			//emptyFormFields();
		}
		catch(Exception e)
		{
			Log.e("Add error",e.toString());
			e.printStackTrace();
		}
	}
	/*private void deleteRow()
	{
		try
		{
			db.deleteRow(edit_Text.getText().toString());
			updateTable();
			emptyFormFields();
		}
		catch(Exception e)
		{
			Log.e("Delete Error",e.toString());
			e.printStackTrace();
		}
	}
	private void retrieveRow()
	{
		try
		{
			ArrayList<Object> row;
			row=db.getRowAsArray(edit_Text.getText().toString());
			edit_Text.setText(String)row.get(1);
		}
		catch(Exception e)
		{
			Log.e("Retrieve Error", e.toString());
    		e.printStackTrace();
		}
	}
	private void updateRow()
	{
		try
		{
			db.updateRow
			(
					edit_Text.getText().toString()
			);
			updateTable();
			emptyFormFields();
		}
		catch (Exception e)
    	{
    		Log.e("Update Error", e.toString());
    		e.printStackTrace();
    	}
    }
	private void emptyFormFields()
	{
		edit_Text.setText("");
	}
	private void updateTable()
	{
		while(dataTable.getChildCount()>1)
		{
			dataTable.removeViewAt(1);
		}
		ArrayList<ArrayList<Object>> data=db.getAllRowsAsArrays();
		for(int position=0;position<data.size();position++)
		{
			TableRow tablerow=new TableRow(this);
			ArrayList<Object> row =data.get(position);
			TextView idText = new TextView(this);
    		idText.setText(row.get(0).toString());
			tablerow.addView(idText);
			TextView textOne = new TextView(this);
    		textOne.setText(row.get(1).toString());
    		tablerow.addView(textOne);
    		dataTable.addView(tablerow);
    	}
    }*/
	
}
