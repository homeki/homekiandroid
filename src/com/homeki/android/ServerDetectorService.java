package com.homeki.android;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import android.app.IntentService;
import android.content.Intent;

public class ServerDetectorService extends IntentService {
	public ServerDetectorService() {
		super("okej");
	}
	
	public ServerDetectorService(String name) {
		super(name);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		DatagramSocket replySocket;
		try {
			replySocket = new DatagramSocket(1337);
			byte[] buf = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			replySocket.setSoTimeout(15000);
			replySocket.receive(packet);
			Intent retIntent = new Intent("FOUNDSERVER");
			retIntent.putExtra("ip", packet.getAddress().getHostAddress());
			sendBroadcast(retIntent);
		} catch (SocketTimeoutException e) {
			Intent retIntent = new Intent("NOSERVER");
			sendBroadcast(retIntent);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
