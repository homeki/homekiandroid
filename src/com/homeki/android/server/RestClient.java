package com.homeki.android.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.HttpClient;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.model.devices.DeviceBuilder;
import com.homeki.android.model.devices.DeviceTypes;
import com.homeki.android.model.devices.DimmerDevice;
import com.homeki.android.model.devices.SwitchDevice;

public class RestClient {
	private static String TAG = RestClient.class.getSimpleName();

	private static String SERVER_URL = "http://192.168.0.12:5000/";

	public List<AbstractDevice> getAllDevices() {
		Log.d(TAG, "getAllDevices()");

		ArrayList<AbstractDevice> devices = new ArrayList<AbstractDevice>();
		HttpURLConnection connection = null;

		try {
			connection = (HttpURLConnection) new URL(SERVER_URL + "device/list").openConnection();
			connection.setConnectTimeout(2000);

			String response = readStreamToEnd(connection.getInputStream());
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
			connection.disconnect();
		}

		return devices;
	}

	public DeviceData getDataForDevice(int deviceId, long start, long end) {
		return new DeviceData();
	}

	public boolean setChannelValueForDevice(int deviceId, int channel, String value) {
		HttpURLConnection connection = null;

		try {
			connection = (HttpURLConnection) new URL(SERVER_URL + "device/" + deviceId + "/channel/" + channel + "/set?value=" + value).openConnection();
			connection.setConnectTimeout(2000);
			connection.getInputStream();
		} catch (Exception e) {
			Log.e(TAG, "setChannelValueForDevice() " + e.getMessage());
			e.printStackTrace();
			
			return false;
		} finally {
			Log.d(TAG, "setChannelValueForDevice() disconnect");
			connection.disconnect();
		}
		
		return true;
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

	// private class Test implements JsonDeserializer<DeviceJSON> {
	//
	// @Override
	// public DeviceJSON deserialize(JsonElement arg0, Type arg1,
	// JsonDeserializationContext arg2) throws JsonParseException {
	//
	// }
	//
	// }

}
