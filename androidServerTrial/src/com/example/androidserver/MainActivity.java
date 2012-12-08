package com.example.androidserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView text;
	String received;
	String mClientMsg = "";
	Thread myCommsThread = null;
	protected static final int MSG_ID = 0x1337;
	ServerSocket ss = null;
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

	public void startCapture(View view){
		//		new QuoteServer().execute();
		TextView tv = (TextView) findViewById(R.id.textView);
		tv.setText("Nothing from client yet");
		this.myCommsThread = new Thread(new CommsThread());
		this.myCommsThread.start();
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

	class CommsThread implements Runnable {
		public void run() {
			Socket s = null;
			try {
				ss = new ServerSocket(4445);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (!Thread.currentThread().isInterrupted()) {
				Message m = new Message();
				m.what = 0x1337;
				try {
					if (s == null)
						s = ss.accept();
					BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
					String st = null;
					st = input.readLine();
					received += st + "IP Address : " + s.getInetAddress();
					myUpdateHandler.sendMessage(m);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
//	private class QuoteServer extends AsyncTask<URL,Integer,Long>{
//
//		protected Long doInBackground(URL... urls){
//			try{
//				new QuoteServerThread().start();
//			}
//			catch(Exception e){
//				received = received + e.getMessage();
//			}
//			return (long)1;
//		}
//
//		protected void onPostExecute(Long result) {
//			text = (TextView) findViewById(R.id.textView);
//
//			text.setText(received);
//		}
//	}
//
//	private class QuoteServerThread extends Thread {
//
//		protected DatagramSocket socket = null;
//		protected BufferedReader in = null;
//		protected boolean moreQuotes = true;
//
//		public QuoteServerThread() throws IOException {
//			this("QuoteServerThread");
//		}
//
//		public QuoteServerThread(String name) throws IOException {
//			super(name);
//			socket = new DatagramSocket(4445);
//		}
//
//		public void run() {
//			while(true){
//				try {
//					byte[] buf = new byte[256];
////					ServerSocket serverSocket = new ServerSocket(4445);
////					received += "Waiting for Connection";
////					updateText(received);
////					Socket socket2 = serverSocket.accept();
////					received = received + "IP Address:" + socket2.getInetAddress();
////					updateText(received);
//
//									// receive request
//									DatagramPacket packet = new DatagramPacket(buf, buf.length);
//									received += "Waiting for Connection";
//									socket.receive(packet);
//					
//									String dString = "You are connected";
//									updateText(dString);
//					                buf = dString.getBytes();
//									
//									// send the response to the client at "address" and "port"
//									InetAddress address = packet.getAddress();
//									received = received + "IP Address of Current request:" + address;
//									updateText(received);
//									int port = packet.getPort();
//									packet = new DatagramPacket(buf, buf.length, address, port);
//									socket.send(packet);
//				} 
//				catch (Exception e) {
//					e.printStackTrace();
//				}
//				socket.close();
//			}
//		}
//
//		private void updateText(String textUpdate){
//			text = (TextView) findViewById(R.id.textView);
//
//			text.setText(textUpdate);
//		}
//	}
//}
