package com.example.ip_database;


import java.util.ArrayList;

//import android.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class MainActivity extends Activity {

	
	EditText edit_Text,edit_Text2;
	//TableLayout dataTable;
	Button button,button_2;
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
		updateTable();
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
		//button_2=(Button)findViewById(R.id.button2);
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
	/*private void addButtonListeners1()
	{
		button_2.setOnClickListener
		(
			new View.OnClickListener() 
			{
				
				@Override public void onClick(View v) {RetrieveRow();}
				
			}
		);
	}*/
	
	private void addRow()
	{
		try
		{
			db.addRow
			(
					edit_Text.getText().toString(),
					edit_Text2.getText().toString()
			);
			updateTable();
			//emptyFormFields();
		}
		catch(Exception e)
		{
			Log.e("Add error",e.toString());
			e.printStackTrace();
		}
		
	}
	
	/*private void RetrieveRow()
	{
		try
    	{
    		// The ArrayList that holds the row data
    		ArrayList<Object> row;
    		// ask the database manager to retrieve the row with the given rowID
    		row = db.getRowAsArray();
 
    		// update the form fields to hold the retrieved data
    		//updateTextFieldOne.setText((String)row.get(1));
    		//updateTextFieldTwo.setText((String)row.get(2));
    	}
    	catch (Exception e)
    	{
    		Log.e("Retrieve Error", e.toString());
    		e.printStackTrace();
    	}
	}*/
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
	}*/
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
    		TextView text2=new TextView(this);
    		text2.setText(row.get(2).toString());
    		tablerow.addView(text2);
    		tablerow.addView(textOne);
    		
    		dataTable.addView(tablerow);
    	}
    }
	
}
