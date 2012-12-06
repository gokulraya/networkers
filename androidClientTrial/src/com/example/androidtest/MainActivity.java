package com.example.androidtest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
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
			//URL url = new URL("","192.168.0.3",4445,"");
			new MakeConnection().execute();
			//			text = (TextView) findViewById(R.id.textView);
			//
			//			text.setText(receivedText);

		}
		catch(Exception e){

			text = (TextView) findViewById(R.id.textView);

			text.setText("Exception: " + e.getMessage());

		}
	}

	private class MakeConnection extends AsyncTask<URL,Integer,Long>{
		String received;
		int error =0;
		Resources res=getResources();
		protected Long doInBackground(URL... urls){
			Socket socket = null;
			text = (TextView) findViewById(R.id.textView);
			try {
				EditText et = (EditText) findViewById(R.id.editText);
				String str = et.getText().toString();
				if(!invalidIP(str)){
					String ipError = res.getString(R.string.INVALID_IPADDRESS);
					throw new IllegalArgumentException(ipError);
				}
				InetAddress serverAddr = InetAddress.getByName(str);
				socket = new Socket(serverAddr, 4445);
			} catch (UnknownHostException e1) {
				error=-3;
				e1.printStackTrace();
			} catch (IOException e1) {
				error=-2;
				e1.printStackTrace();
			}
			catch(IllegalArgumentException e){
				error = -1;
			}
			try {
				if(error==0){
					String str = "Client making connection from : ";
					PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
					out.println(str);
					//Reading information from the socket
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					received  = (String)reader.readLine();
				}
			} 
			catch (UnknownHostException e) {
				error=-2;
				e.printStackTrace();
			} catch (IOException e) {
				error=-3;
				e.printStackTrace();
			} catch (Exception e) {
				error=-2;
				e.printStackTrace();
			}
			return (long)1;
		}
		protected void onPostExecute(Long result) {
			if(error==-1){
				AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
				alert.setTitle("Error");
				String ipError = res.getString(R.string.INVALID_IPADDRESS);
				alert.setMessage(ipError);
				alert.setCancelable(true);
				AlertDialog dialog = alert.create();
				dialog.show();
			}
			else if(error==-2){
				text = (TextView) findViewById(R.id.textView);
				String error = res.getString(R.string.GENERAL_ERROR);
				text.setText(error);
			}
			else if(error==-3){
				text = (TextView) findViewById(R.id.textView);
				String unknownHost = res.getString(R.string.UNKNOWN_HOST);
				text.setText(unknownHost);
			}
			else{
				text = (TextView) findViewById(R.id.textView);
				text.setText(received);
			}
		}

		private boolean invalidIP(String ipAddr){
			Pattern p = Pattern.compile("^((\\d|\\d{2}|([0-1]\\d{2})|(2[0-5][0-9]))\\.){3}(\\d|\\d{2}|([0-1]\\d{2})|(2[0-5][0-9]))$");
			Matcher m = p.matcher(ipAddr);
			if(m.find()){
				return true;
			}
			else
				return false;
		}
	}

	//	private class MakeConnection extends AsyncTask<URL,Integer,Long>{
	//		String received;
	//		protected Long doInBackground(URL... urls){
	//			byte[] buf = new byte[256];
	//			try{
	//				text = (TextView) findViewById(R.id.textView);
	//
	//				
	//
	//				DatagramSocket socket = new DatagramSocket();
	//
	//				// send request
	//
	//				InetAddress address = InetAddress.getByName("192.168.0.51");
	//				DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
	//				socket.send(packet);
	//
	//				
	//
	//				// get response
	//				packet = new DatagramPacket(buf, buf.length);
	//				socket.receive(packet);
	//
	//				
	//
	//				// display response
	//				received = new String(packet.getData(), 0, packet.getLength());
	//				receivedText=received;
	//
	//				socket.close();
	//
	//			}
	//			catch(Exception e){
	//				text.setText(e.getMessage());
	//			}
	//			return (long)buf.length;
	//		}
	//
	//		protected void onPostExecute(Long result) {
	//			text = (TextView) findViewById(R.id.textView);
	//
	//			text.setText(received);
	//		}
	//	}
}