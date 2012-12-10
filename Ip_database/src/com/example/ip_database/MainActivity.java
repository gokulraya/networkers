package com.example.ip_database;


import java.util.ArrayList;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
//import android.R;


public class MainActivity extends Activity {

	
	EditText editText_Port,editText_Action,editText_id,editText_site,editText_address;
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
		emptyFormFields();
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
		
		editText_Port=(EditText)findViewById(R.id.editText1);
		
		button=(Button)findViewById(R.id.button1);
		
		editText_Action=(EditText)findViewById(R.id.editText2);
		
		button_2=(Button)findViewById(R.id.button2);
		
		editText_id=(EditText)findViewById(R.id.editText3);
		
		editText_site=(EditText)findViewById(R.id.editText4);
		
		editText_address=(EditText)findViewById(R.id.editText5);
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
		button_2.setOnClickListener
		(
			new View.OnClickListener() 
			{
				
				@Override public void onClick(View v) {deleteRow();}
				
			}
		);

	}
	/*private void addButtonListener()
	{
		button_2.setOnClickListener
		(
			new View.OnClickListener() 
			{
				
				@Override public void onClick(View v) {deleteRow();}
				
			}
		);
	}
	*/
	private void addRow()
	{
		 final String IP_REGEX="^((\\d|\\d{2}|([0-1]\\d{2})|(2[0-4][0-9])|(25[0-5]))\\.){3}(\\d|\\d{2}|([0-1]\\d{2})|(2[0-4][0-9])|(25[0-5]))$";
		 final String WEBSITE_REGEX="^(((http|https)://)?)([A-Za-z0-9.]*)$";
		String address_Validation=editText_address.getText().toString();
		String site_Validation=editText_site.getText().toString();
		Pattern p=Pattern.compile(address_Validation);
		boolean m=Pattern.matches(IP_REGEX, address_Validation);
		Pattern s=Pattern.compile(WEBSITE_REGEX);
		boolean Bsite_check=Pattern.matches(WEBSITE_REGEX, site_Validation);
		if(m==true && Bsite_check==true)
		{
			
			try
			{
				db.addRow
				(
						editText_address.getText().toString(),
						editText_site.getText().toString(),
						editText_Action.getText().toString(),
						editText_Port.getText().toString()
						//edit_Text5.getText().toString(),
						//edit_text6.getText().toString()
				);
				updateTable();
				emptyFormFields();
				//deleteRow();
			}
			catch(Exception e)
			{
				Log.e("Add error",e.toString());
				e.printStackTrace();
			}
			
		}
		else
		{
			AlertDialog.Builder altDialog= new AlertDialog.Builder(this);
			altDialog.setMessage("IP address/Site format not correct,Please check again.");
			altDialog.setTitle("Invalid Input");
			AlertDialog alert=altDialog.create();
			alert.show();
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
	private void deleteRow()
	{
		try
		{
			db.deleteRow(Long.parseLong(editText_id.getText().toString()));
			updateTable();
			emptyFormFields();
		}
		catch(Exception e)
		{
			Log.e("Delete Error",e.toString());
			e.printStackTrace();
		}
	}
	/*private void retrieveRow()
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
    }*/
	private void emptyFormFields()
	{
		//editText_Port.setText("");
		//editText_Action.setText("");
		//editText_site.setText("");
		//editText_address.setText("");
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
			
			TextView text_id = new TextView(this);
    		text_id.setText(row.get(0).toString());
			tablerow.addView(text_id);
			
			TextView text_address = new TextView(this);
    		text_address.setText(row.get(1).toString());
    		tablerow.addView(text_address);
    		    		
    		TextView text_site=new TextView(this);
    		text_site.setText(row.get(3).toString());
    		tablerow.addView(text_site);
    		
    		TextView text_Action=new TextView(this);
    		text_Action.setText(row.get(4).toString());
    		tablerow.addView(text_Action);
    		
    		
    		
    		TextView text_Port=new TextView(this);
    		text_Port.setText(row.get(5).toString());
    		tablerow.addView(text_Port);
    		
    		
    		
    		dataTable.addView(tablerow);
    	}
    }
	
}
