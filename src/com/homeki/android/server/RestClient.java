package com.homeki.android.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.homeki.android.model.DataPoint;
import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.model.devices.DeviceBuilder;
import com.homeki.android.settings.Settings;

public class RestClient {
	private static String TAG = RestClient.class.getSimpleName();

	private static SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public enum Errors {
		RequestFailed
	}

	private Context mContext;

	public RestClient(Context context) {
		mContext = context;
	}

	private String getServerURL() {
		StringBuilder builder = new StringBuilder();
		builder.append("http://");
		builder.append(Settings.getServerUrl(mContext));
		builder.append(":");
		builder.append(Settings.getServerPort(mContext));
		builder.append("/");

		return builder.toString();
	}

	public List<AbstractDevice> getAllDevices() {
		Log.d(TAG, "getAllDevices()");

		ArrayList<AbstractDevice> devices = new ArrayList<AbstractDevice>();
		HttpURLConnection connection = null;

		try {
			connection = (HttpURLConnection) new URL(getServerURL() + "device/list").openConnection();
			connection.setConnectTimeout(2000);

			InputStream stream = connection.getInputStream();

			String response = readStreamToEnd(stream);
			Gson gson = new Gson();
			DeviceBuilder.Device[] deviceArray = gson.fromJson(response, DeviceBuilder.Device[].class);

			for (int i = 0; i < deviceArray.length; i++) {
				AbstractDevice device = DeviceBuilder.build(deviceArray[i].type, deviceArray[i].id, deviceArray[i].name, deviceArray[i].description, deviceArray[i].added, deviceArray[i].active,
						deviceArray[i].channelValues);
				devices.add(device);
			}
		} catch (Exception e) {
			Log.e(TAG, "getAllDevices() " + e.getMessage());
			e.printStackTrace();

		} finally {
			Log.d(TAG, "getAllDevices() disconnect");
			if (connection != null) {
				connection.disconnect();
			}
		}

		return devices;
	}

	public boolean setChannelValueForDevice(int deviceId, int channel, String value) {
		HttpURLConnection connection = null;

		try {
			connection = (HttpURLConnection) new URL(getServerURL() + "device/" + deviceId + "/channel/" + channel + "/set?value=" + value).openConnection();
			connection.setConnectTimeout(2000);
			connection.getInputStream();
		} catch (Exception e) {
			Log.e(TAG, "setChannelValueForDevice() " + e.getMessage());
			e.printStackTrace();

			return false;
		} finally {
			Log.d(TAG, "setChannelValueForDevice() disconnect");
			if (connection != null) {
				connection.disconnect();
			}
		}

		return true;
	}

	public List<DataPoint> getChannelHistoryForDevice(int deviceId, int channelId, Date start, Date end) {
		Log.d(TAG, "getChannelHistoryForDevice()");

		ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();
		HttpURLConnection connection = null;

		try {
			String from = URLEncoder.encode(FORMAT_DATE.format(start), "utf-8");
			String to = URLEncoder.encode(FORMAT_DATE.format(end), "utf-8");
			String url = getServerURL() + String.format("device/%d/channel/%d/list?from=%s&to=%s", deviceId, channelId, from, to);
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setConnectTimeout(2000);

			InputStream stream = connection.getInputStream();

			String response = readStreamToEnd(stream);
			Gson gson = new Gson();
			JSONDataPoint[] points = gson.fromJson(response, JSONDataPoint[].class);
			for (int i = 0; i < points.length; i++) {
				dataPoints.add(new DataPoint(FORMAT_DATE.parse(points[i].registered), points[i].value));
			}

		} catch (Exception e) {
			Log.e(TAG, "getChannelHistoryForDevice() " + e.getMessage());
			e.printStackTrace();

		} finally {
			Log.d(TAG, "getChannelHistoryForDevice() disconnect");
			if (connection != null) {
				connection.disconnect();
			}
		}

		return dataPoints;
	}

	private String readStreamToEnd(InputStream inputStream) throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder total = new StringBuilder();
		String line;
		while ((line = r.readLine()) != null) {
			total.append(line);
		}
		return total.toString();
	}

	public interface OnErrorListener {
		public void onError(Errors errorCode);
	}

	private class JSONDataPoint {
		@SerializedName("channel")
		public int channel;

		@SerializedName("value")
		public double value;
		
		@SerializedName("registered")
		public String registered;		
	}
}
