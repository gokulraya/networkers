package com.example.androidtest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ConnectedActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connected);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_connected, menu);
		return true;
	}

	public void getContent(View view){
		try{
			new MakeHttpRequest().execute();
		}
		catch(Exception e){

		}
	}

	private class MakeHttpRequest extends AsyncTask<URL,Integer,Long>{
		int error = Constants.NO_ERROR;
		String received="";
		Resources res = getResources();
		String website="";
		public Long doInBackground(URL...urls){
			Socket socket = null;
			Bundle extras = getIntent().getExtras();
			try{
				EditText websiteEt = (EditText) findViewById(R.id.websiteAddress);
				website = websiteEt.getText().toString();
				if(!invalidWebsite(website)){
					String websiteError = res.getString(R.string.INVALID_WEBSITE);
					throw new IllegalArgumentException(websiteError);
				}
				String serverAddr = extras.getString("serverAddress");
				InetAddress serverAddress = InetAddress.getByName(serverAddr);
				socket = new Socket(serverAddress,Constants.SECURE_PORT);
			}
			catch(IOException e){
				received += e.getMessage();
				error = Constants.IO_EXCEPTION;
			}
			catch(IllegalArgumentException e){
				received += e.getMessage();
				error = Constants.ILLEGAL_ARGUMENT_EXCEPTION;
			}
			try{
				if(error ==Constants.NO_ERROR ){
					String password = extras.getString("password");
					DataInputStream in = new DataInputStream(socket.getInputStream());
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					out.writeUTF(password+Constants.DELIMITER+website.toLowerCase());
					received = in.readUTF();
				}
				if(error == Constants.GENERAL_EXCEPTION){
					received+=res.getString(R.string.GENERAL_ERROR);
				}
			}
			catch(Exception e){
				received += e.getMessage();
				received += e.getStackTrace();
				//received=res.getString(R.string.GENERAL_ERROR);
			}
			return (long)1;
		}
		
		protected void onPostExecute(Long result){
			TextView tv = (TextView) findViewById(R.id.content);
			tv.setText(received);
		}

		private boolean invalidWebsite(String website){
			Pattern p = Pattern.compile(Constants.WEBSITE_REGEX);
			Matcher match = p.matcher(website);
			if(match.find()){
				return true;
			}
			else
				return false;
		}
	}
}
