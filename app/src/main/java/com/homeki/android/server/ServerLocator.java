package com.homeki.android.server;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerLocator {
	private static String TAG = ServerLocator.class.getSimpleName();
	
	private static int PORT_RECEIVE = 1337;
	private static int PORT_SEND = 53005;
	private static int TIMEOUT_RECEIVE = 5000;

	public static String locateServerOnWifi() {
		Log.d(TAG, "locateServerOnWifi()");
		String hostname = "";
		
		new Thread(new SendBroadcastTask()).start();
		
		DatagramSocket socket = null;
		try {
			byte[] receiveData = new byte[512];
			DatagramPacket pack = new DatagramPacket(receiveData, receiveData.length);

			socket = new DatagramSocket(PORT_RECEIVE);
			socket.setSoTimeout(TIMEOUT_RECEIVE);
			socket.receive(pack);

			String[] data = new String(receiveData, 0, pack.getLength()).split("\\|");
			String name = data[0];
			if (data.length > 1) hostname = data[1].trim();

			Log.i(TAG, "Server " + name + " (" + pack.getAddress().getHostAddress() + ") answered.");

			if (hostname.isEmpty()) {
				Log.i(TAG, "No hostname received in broadcast response, using source IP as hostname.");
				hostname = pack.getAddress().getHostAddress();
			}

			if (hostname.startsWith("http://")) hostname = hostname.replace("http://", "");
			if (hostname.endsWith("/")) hostname = hostname.substring(0, hostname.length() - 2);
		} catch (IOException e) {
			Log.i(TAG, "Exception: " + e);
		} finally {
			if (socket != null) {
				socket.close();
			}
		}

		return hostname;
	}

	private static class SendBroadcastTask implements Runnable {
		@Override
		public void run() {
			Log.i(TAG, "SendBroadcastTask.run()");
			try {
				// Send a broadcast in a few ms. This to avoid a racing scenario on sending request and starting to listen for response.
				Thread.sleep(500);
				
				byte[] data = "HOMEKI".getBytes("utf-8");

				DatagramPacket packet = new DatagramPacket(data, data.length);
				packet.setPort(PORT_SEND);
				packet.setAddress(InetAddress.getByName("255.255.255.255"));

				DatagramSocket socket = new DatagramSocket();
				socket.setBroadcast(true);
				
				socket.send(packet);

				socket.close();

			} catch (Exception e) {
				Log.e(TAG, "locateServerOnWifi() " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
