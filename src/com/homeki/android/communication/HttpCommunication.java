package com.homeki.android.communication;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

public class HttpCommunication {
	private HttpClient client;
	
	public HttpCommunication() {
		HttpParams params = setupParams();
		SchemeRegistry schemeRegistry = setupRegistry();
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
		client = new DefaultHttpClient(cm, params);
	}
			
	private SchemeRegistry setupRegistry() {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register( new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));			
		return schemeRegistry;
	}

	private HttpParams setupParams() {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 40 * 1000);
		HttpConnectionParams.setSoTimeout(params, 40 * 1000);
		ConnManagerParams.setMaxTotalConnections(params, 100);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		return params;
	}

	public synchronized String sendCommand(String address) throws IOException {
		HttpGet get = new HttpGet(address);
		HttpResponse response = client.execute(get);
		return convertToString(response.getEntity());
	}
	
	public synchronized String postCommand(String address, String value) throws IOException {
		HttpPost p = new HttpPost(address);
		p.setEntity(new StringEntity(value));
		HttpResponse response = client.execute(p);
		return convertToString(response.getEntity());
	}
	
	private synchronized String convertToString(HttpEntity he) throws IOException {
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
