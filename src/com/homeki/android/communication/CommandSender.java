package com.homeki.android.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommandSender {
	
	public static String sendCommand(String address) throws IOException {
		URL url = new URL(address);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			return processServerResponse(in);
		} finally {
			urlConnection.disconnect();
		}
	}
	
	private static String processServerResponse(BufferedReader in) throws IOException {
		StringBuilder sb = new StringBuilder();
		while (in.ready()) {
			sb.append(in.readLine() + "\n");
		}
		return sb.toString();
	}
}
