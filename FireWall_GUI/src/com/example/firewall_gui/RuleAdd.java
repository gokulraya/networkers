package com.example.firewall_gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class RuleAdd extends Activity {
	public final static String Extra="com.example.myfirstapp.MESSAGE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rule_add);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_rule_add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void Add(View view)
	{
		Intent intent=new Intent(this,IP_Add.class);
		EditText edit1=(EditText)findViewById(R.id.editText1);
		EditText edit2=(EditText)findViewById(R.id.editText2);
		EditText edit3=(EditText)findViewById(R.id.editText3);
		EditText edit4=(EditText)findViewById(R.id.editText4);
		String s1=edit1.getText().toString();
		String s2=edit2.getText().toString();
		String s3=edit3.getText().toString();
		String s4=edit4.getText().toString();
		String message=(s1 + "." + s2 + "." + s3 + "." + s4);
		intent.putExtra(Extra,message);
		startActivity(intent);
	}
	public void Reject(View view)
	{
		Intent intent=new Intent(this,IP_Reject.class);
		startActivity(intent);
	}
}
