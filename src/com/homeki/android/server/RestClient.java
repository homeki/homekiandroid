package com.homeki.android.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.impl.conn.DefaultClientConnection;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.model.devices.DeviceTypes;
import com.homeki.android.model.devices.DimmerDevice;
import com.homeki.android.model.devices.SwitchDevice;

public class RestClient {
	private static String TAG = RestClient.class.getSimpleName();

	public List<AbstractDevice<?>> getAllDevices() {

		Log.d(TAG, "getAllDevices()");
//		try {
//			URL url = new URL("http://192.168.0.12:5000/device/list");
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//			InputStream inputStream = connection.getInputStream();
//			String body = readStreamToEnd(inputStream);
//			
//			Log.d(TAG, "Got Body: " + body);
//			
//			GsonBuilder builder = new GsonBuilder();
//			builder.registerTypeAdapter(DeviceJSON.class, new Test());
//		
//			Gson gson = builder.create();
//			
//			DeviceJSON[] list = gson.fromJson(body, DeviceJSON[].class);
//			
//			for (int i = 0; i < list.length; i++) {
//				Log.d(TAG, list[i].state.toString());
//			}
//
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<AbstractDevice<?>> devices = new ArrayList<AbstractDevice<?>>();
		devices.add(new DimmerDevice(DeviceTypes.DIMMER, 0, "Vardagsrum 1", "Lampan vid soffan.", new Date(), true, 100, 1));
		devices.add(new DimmerDevice(DeviceTypes.DIMMER, 1, "Vardagsrum 2", "Lampan över bordet.", new Date(), true, 200, 1));
		devices.add(new SwitchDevice(DeviceTypes.SWITCH, 2, "Vardagsrum 3", "Den lilla röda fönsterlampan som är så fin.", new Date(), true, 1));
		devices.add(new DimmerDevice(DeviceTypes.DIMMER, 3, "Sovrum", "Taklampan.", new Date(), true, 100, 1));
		devices.add(new SwitchDevice(DeviceTypes.SWITCH, 4, "Terrassen", "Terraslampan.", new Date(), true, 1));

		return devices;
	}

	public DeviceData getDataForDevice(int deviceId, long start, long end) {
		return new DeviceData();
	}

	public void setChannelValueForDevice(int deviceId, int channel, double value) {

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
	
//	private class Test implements JsonDeserializer<DeviceJSON> {
//
//		@Override
//		public DeviceJSON deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
//			
//		}
//		
//	}
	
	private class DeviceJSON {
		@SerializedName("type")
		public String type;

		@SerializedName("id")
		public int id;
		
		@SerializedName("name")
		public String name;
		
		@SerializedName("description")
		public String added;
		
		@SerializedName("active")
		public boolean active;
		
		@SerializedName("state")
		public Object state;
	}
}
