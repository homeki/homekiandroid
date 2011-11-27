package com.homeki.android.communication;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class CommandSender {
	public static String sendCommand(String address) throws IOException {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(address);
		HttpResponse response = client.execute(get);
		return convertToString(response.getEntity());
	}

	public static String postCommand(String address, String value) throws IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost p = new HttpPost(address);
		p.setEntity(new StringEntity(value));
		HttpResponse response = client.execute(p);
		return convertToString(response.getEntity());
	}
	
	private static String convertToString(HttpEntity he) throws IOException {
		String s;
		
		try {
			s = EntityUtils.toString(he);
		} catch (Exception ex) {
			s = "";
		}
		
		return s;
	}
}
