package com.homeki.android.server;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.homeki.android.model.DataPoint;
import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.model.devices.DeviceBuilder;
import com.homeki.android.settings.Settings;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestClient {
	private static String TAG = RestClient.class.getSimpleName();
	private static SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private Context context;

	public RestClient(Context context) {
		this.context = context;
	}

	private String getServerURL() {
		StringBuilder builder = new StringBuilder();
		builder.append("http://");
		builder.append(Settings.getServerUrl(context));
		builder.append(":");
		builder.append(Settings.getServerPort(context));
		builder.append("/api");
		return builder.toString();
	}

	public List<AbstractDevice> getAllDevices() {
		Log.d(TAG, "getAllDevices()");

		ArrayList<AbstractDevice> devices = new ArrayList<AbstractDevice>();
		HttpURLConnection connection = null;

		try {
			connection = (HttpURLConnection) new URL(getServerURL() + "/devices").openConnection();
			connection.setConnectTimeout(2000);

			InputStream stream = connection.getInputStream();

			String response = readStreamToEnd(stream);
			Gson gson = new Gson();
			DeviceBuilder.Device[] deviceArray = gson.fromJson(response, DeviceBuilder.Device[].class);

			for (int i = 0; i < deviceArray.length; i++) {
				AbstractDevice device = DeviceBuilder.build(deviceArray[i].type, deviceArray[i].deviceId, deviceArray[i].name, deviceArray[i].description, deviceArray[i].added, deviceArray[i].active, deviceArray[i].channelValues);
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
	
	public void registerClient(String id) throws Exception {
		HttpURLConnection connection = null;

		try {
			Gson gson = new Gson();
			connection = (HttpURLConnection) new URL(getServerURL() + "/clients").openConnection();
      connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(1000);
			
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			String json = gson.toJson(new JSONClient(id));
			writer.write(json);
			writer.flush();
			
			connection.getInputStream();
		} finally {
			Log.d(TAG, "registerClient() disconnect");
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
	
	public void unregisterClient(String id) throws Exception {
		HttpURLConnection connection = null;

		try {
			connection = (HttpURLConnection) new URL(getServerURL() + "/clients/" + id).openConnection();
			connection.setRequestMethod("DELETE");
			connection.setConnectTimeout(1000);

			connection.getInputStream();
		} finally {
			Log.d(TAG, "unregisterClient() disconnect");
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public boolean setChannelValueForDevice(int deviceId, int channel, int value) {
		HttpURLConnection connection = null;

		try {
      Gson gson = new Gson();
			connection = (HttpURLConnection) new URL(getServerURL() + "/devices/" + deviceId + "/channels/" + channel).openConnection();
      connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
      connection.setRequestMethod("POST");
			connection.setConnectTimeout(1000);

      OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
      String json = gson.toJson(new JSONChannelValue(value));
      writer.write(json);
      writer.flush();

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
			String url = getServerURL() + String.format("/devices/%d/channels/%d?from=%s&to=%s", deviceId, channelId, from, to);
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

	private class JSONDataPoint {
		public int channel;
		public double value;
		public String registered;
	}
	
	private class JSONClient {
		public String id;

		public JSONClient(String id) {
			this.id = id;
		}
	}

  private class JSONChannelValue {
    public int value;

    public JSONChannelValue(int value) {
      this.value = value;
    }
  }
}
