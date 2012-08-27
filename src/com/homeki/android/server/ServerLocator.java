package com.homeki.android.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import android.util.Log;

/**
 * Class to locate server on the local network using UDP broadcast
 * 
 * @author Patrik
 * 
 */
public class ServerLocator {

	private static String TAG = ServerLocator.class.getSimpleName();

	public static String locateServerOnWifi() {
		Log.d(TAG, "locateServerOnWifi()");
		String serverPath = "";

		try {
			byte[] data = "HOMEKI".getBytes("utf-8");

			DatagramPacket packet = new DatagramPacket(data, data.length);
			packet.setPort(53005);
			packet.setAddress(InetAddress.getByName("255.255.255.255"));

			DatagramSocket socket = new DatagramSocket();
			socket.setBroadcast(true);
			socket.send(packet);

			socket.close();

		} catch (Exception e) {
			Log.e(TAG, "locateServerOnWifi() " + e.getMessage());
			e.printStackTrace();
		}

		return serverPath;
	}
}
