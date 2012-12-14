package com.example.androidtest;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	String receivedText;
	TextView text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void clickOnMain(View view){
		try{
			//Since establishing connection and other processes are asynch calls, creating an AsyncTask to get this job done
			new MakeConnection().execute();
		}
		catch(Exception e){
			//Catch the exception and thrown on the main screen
			text = (TextView) findViewById(R.id.textView);
			text.setText("Exception: " + e.getMessage());
		}
	}
	
	private class MakeConnection extends AsyncTask<URL,Integer,Long>{
		String received;
		int error =Constants.NO_ERROR;
		Resources res=getResources();
		String ipAddress="";
		protected Long doInBackground(URL... urls){
			Socket socket = null;
			text = (TextView) findViewById(R.id.textView);
			try {
				//Getting the IP address and password entered on the screen
				EditText et = (EditText) findViewById(R.id.editText);
				ipAddress = et.getText().toString();
								
				//Validate the IP address and handle accordingly
				if(!invalidIP(ipAddress)){
					String ipError = res.getString(R.string.INVALID_IPADDRESS);
					throw new IllegalArgumentException(ipError);
				}
				
				//Trying to open connection to the IP Address
				InetAddress serverAddr = InetAddress.getByName(ipAddress);
				socket = new Socket(serverAddr, Constants.SERVER_PORT);
			} 
			catch (UnknownHostException e1) {
				error= Constants.UNKNOWN_HOST_EXCEPTION;
			} 
			catch (IOException e1) {
				error=Constants.IO_EXCEPTION;
			}
			catch(IllegalArgumentException e){
				error = Constants.ILLEGAL_ARGUMENT_EXCEPTION;
			}
			try {
				if(error==Constants.NO_ERROR){
					EditText passwordet = (EditText) findViewById(R.id.password);
					String password = passwordet.getText().toString();
					DataInputStream in = new DataInputStream(socket.getInputStream());
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					out.writeUTF(password);
					received = in.readUTF();
					if(received.equals(Constants.SUCCESS)){
						Intent intent = new Intent(MainActivity.this, ConnectedActivity.class);
						intent.putExtra("serverAddress",ipAddress);
						intent.putExtra("password", password);
						startActivity(intent);
					}
					else{
						received = Constants.INVALID_IP;
					}
				}
			} 
			catch (UnknownHostException e) {
				error=Constants.UNKNOWN_HOST_EXCEPTION;
				e.printStackTrace();
			} catch (IOException e) {
				error=Constants.IO_EXCEPTION;
				e.printStackTrace();
			} catch (Exception e) {
				error=Constants.GENERAL_EXCEPTION;
				e.printStackTrace();
			}
			return (long)1;
		}
		protected void onPostExecute(Long result) {
			if(error==Constants.ILLEGAL_ARGUMENT_EXCEPTION){
				AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
				alert.setTitle(Constants.ERROR_DIALOG);
				String ipError = res.getString(R.string.INVALID_IPADDRESS);
				alert.setMessage(ipError);
				alert.setCancelable(true);
				AlertDialog dialog = alert.create();
				dialog.show();
			}
			else if(error==Constants.GENERAL_EXCEPTION){
				text = (TextView) findViewById(R.id.textView);
				String error = received + res.getString(R.string.GENERAL_ERROR);
				text.setText(error);
			}
			else if(error==Constants.UNKNOWN_HOST_EXCEPTION){
				text = (TextView) findViewById(R.id.textView);
				String unknownHost = res.getString(R.string.UNKNOWN_HOST);
				text.setText(unknownHost);
			}
			else if(error==Constants.IO_EXCEPTION)
			{
				text = (TextView) findViewById(R.id.textView);
				text.setText(received);
			}
			else{
				text = (TextView) findViewById(R.id.textView);
				text.setText(received);
			}
		}

		private boolean invalidIP(String ipAddr){
			Pattern p = Pattern.compile(Constants.IP_REGEX);
			Matcher m = p.matcher(ipAddr);
			if(m.find()){
				return true;
			}
			else
				return false;
		}
	}
}