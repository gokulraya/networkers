package com.example.androidserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView text;
	String received="";
	String mClientMsg = "";
	Thread myCommsThread = null;
	String actualPassword="";
	protected static final int MSG_ID = 0x1337;
	ServerSocket ss = null;
	ArrayList<Socket> sockets = new ArrayList<Socket>();
	ArrayList<Socket> secureSockets = new ArrayList<Socket>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextView ipTv = (TextView) findViewById(R.id.ipAddress);
		try{
//			WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
//			WifiInfo info = wifi.getConnectionInfo();
//			int rawLocalAddress = info.getIpAddress();
//			byte[] rawBytes = BigInteger.valueOf(rawLocalAddress).toByteArray();
//			InetAddress actualAddress = InetAddress.getByAddress(rawBytes);
//			String ipAddress = actualAddress.getHostAddress().toString();
			String ipAddress="";
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			for(;en.hasMoreElements();){
				NetworkInterface interf = en.nextElement();
				for(Enumeration<InetAddress> addresses=interf.getInetAddresses();addresses.hasMoreElements();){
					InetAddress inetAddr = addresses.nextElement();
					if(!inetAddr.isLoopbackAddress())
						ipAddress = inetAddr.getHostAddress().toString();
				}
			}
			ipTv.setText("Your IP Address is " + ipAddress);
		}
		catch(Exception e){
			ipTv.setText("Unable to fetch IP Address now");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void startCapture(View view){
		TextView tv = (TextView) findViewById(R.id.textView);
		tv.setText("Nothing from client yet");
		new MakeConnectionServer().execute();
	}

	private class MakeConnectionServer extends AsyncTask<URL,Integer,Long>{
		Resources res = getResources();
		int success = 0;
		protected Long doInBackground(URL...urls ){


			try {
				ss = new ServerSocket(Constants.NORMAL_PORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			int count=0;
			while(count<Constants.THREAD_COUNT){
				Socket s=null;
				Message m = new Message();
				m.what = 0x1337;
				try {
					if(s == null)
						s=ss.accept();
					sockets.add(s);

					EditText password = (EditText) findViewById(R.id.password);
					actualPassword = password.getText().toString();
					DataInputStream in = new DataInputStream(s.getInputStream());
					DataOutputStream out = new DataOutputStream(s.getOutputStream());
					String sentPassword = in.readUTF();
					String sourceIp = s.getInetAddress().toString();
					received +=  res.getString(R.string.CONNECTION_FROM_MESSAGE) + sourceIp;

					if(actualPassword.equals(sentPassword)){
						out.writeUTF(Constants.SUCCESS);
						received += Constants.SUCCESS_MESSAGE + s.getInetAddress() +"\n";
						success=1;
					}
					else
						out.writeUTF(Constants.FAILED);
					myUpdateHandler.sendMessage(m);
					sockets.add(s);
					count++;
					if(success==1){
						ServerSocket secured=null;
						try{
							secured = new ServerSocket(Constants.SECURE_PORT);
						}
						catch(IOException e){
							received += Constants.SOCKET_CONNECTION_FAILED + s.getInetAddress();
							received += e.getMessage();
						}

						Socket securedSocket = null;
						Message msg = new Message();
						msg.what = 0x1337;
						try{
							if(securedSocket==null)
								securedSocket = secured.accept();
							secureSockets.add(securedSocket);

							EditText passwordEt = (EditText) findViewById(R.id.password);
							actualPassword = passwordEt.getText().toString();
							DataInputStream secureIn = new DataInputStream(securedSocket .getInputStream());
							DataOutputStream secureOut = new DataOutputStream(securedSocket .getOutputStream());
							String sentMsg = secureIn.readUTF();
							String sentSecurePassword = sentMsg.substring(0, sentMsg.indexOf(Constants.DELIMITER));
							String website=sentMsg.substring(sentMsg.indexOf(Constants.DELIMITER)+1);

							received += Constants.CONNECTED_TO_MESSAGE + website + "\n";

							if(!sentSecurePassword.equals(actualPassword)){
								throw new SecurityException();
							}

							URL url = new URL(website);
							BufferedReader buf = new BufferedReader(new InputStreamReader(url.openStream()));

							String inputLine="",tempLine;
							while((tempLine=buf.readLine())!=null){
								inputLine += tempLine;
							}

							secureOut.writeUTF(Constants.SENT_MESSAGE + inputLine);
							secureSockets.add(securedSocket);
						}
						catch(Exception e){
							received += "Error while writing data";
							received += e.getMessage();
						}
						myUpdateHandler.sendMessage(msg);
					}

				}
				catch(SecurityException e){
					received += Constants.ACCESS_DENIED +s.getInetAddress();
				}
				catch (Exception e) {
					received += Constants.GENERAL_EXCEPTION + s.getInetAddress();
				}
			}
			return (long)count;
		}
		protected void onPostExecute(Long result){
			TextView tv = (TextView) findViewById(R.id.textView);
			tv.setText(received);
		}
	}

	Handler myUpdateHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ID:
				TextView tv = (TextView) findViewById(R.id.textView);
				tv.setText(received);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
}