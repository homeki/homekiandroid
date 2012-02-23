package com.homeki.android.communication;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import android.net.http.AndroidHttpClient;

public class HttpCommunication {
	public static String sendCommand(String address) throws IOException {
		final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		final HttpGet getRequest = new HttpGet(address);
		
		HttpResponse response = client.execute(getRequest);
		
		String temp = convertToString(response.getEntity());
		client.close();
		return temp;
	}
	
	public static String postCommand(String address, String value) throws IOException {
		final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		HttpPost p = new HttpPost(address);
		p.setEntity(new StringEntity(value));
		HttpResponse response = client.execute(p);
		String temp = convertToString(response.getEntity());
		client.close();
		return temp;
	}
	
	private static String convertToString(HttpEntity he) throws IOException {
		String s;
		try {
			s = EntityUtils.toString(he);
		} catch (Exception ex) {
			s = "";
		}
		he.consumeContent();
		return s;
	}
}
