package com.homeki.android.server;

import android.util.Log;
import com.homeki.android.misc.Misc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;

public class ServerLocator {
	private static String TAG = ServerLocator.class.getSimpleName();

	public static String locateServerOnWifi() {
		new Thread(new SendBroadcastTask()).start();
		
		DatagramSocket socket = null;

		try {
			byte[] receiveData = new byte[512];
			DatagramPacket pack = new DatagramPacket(receiveData, receiveData.length);

			socket = new DatagramSocket(1337);
			socket.setSoTimeout(5000);
			socket.receive(pack);

			String[] data = new String(receiveData, 0, pack.getLength()).split("\\|");
			String name = data[0];
			String hostname = "";
			if (data.length > 1) hostname = data[1].trim();

			Log.i(TAG, "Server " + name + " (" + pack.getAddress().getHostAddress() + ") answered.");

			if (hostname.isEmpty()) {
				Log.i(TAG, "No hostname received in broadcast response, using source IP as hostname.");
				hostname = pack.getAddress().getHostAddress();
			}

			if (hostname.startsWith("http://")) hostname = hostname.replace("http://", "");
			if (hostname.endsWith("/")) hostname = hostname.substring(0, hostname.length() - 2);

			return hostname;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (socket != null) socket.close();
		}
	}

	private static class SendBroadcastTask implements Runnable {
		@Override
		public void run() {
			// wait to avoid a racing scenario on sending request and starting to listen for response
			Misc.sleep(500);

			try {
				byte[] data = "HOMEKI".getBytes("utf-8");
				DatagramPacket packet = new DatagramPacket(data, data.length);
				packet.setPort(53005);
				packet.setAddress(InetAddress.getByName("255.255.255.255"));

				DatagramSocket socket = new DatagramSocket();
				socket.setBroadcast(true);
				socket.send(packet);
				socket.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
