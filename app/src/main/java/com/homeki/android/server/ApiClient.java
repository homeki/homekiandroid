package com.homeki.android.server;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.homeki.android.misc.Settings;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ApiClient {
	private static final String TAG = ApiClient.class.getSimpleName();
	private static final Gson GSON = new Gson();

	private Context context;

	public ApiClient(Context context) {
		this.context = context;
	}

	private String getServerUrl() {
		return "http://" + Settings.getServerUrl(context) + ":" + Settings.getServerPort(context) + "/api";
	}

	public List<JsonDevice> getDevices() {
		Log.i(TAG, "Fetching all devices.");
		return get(getServerUrl() + "/devices", new TypeToken<List<JsonDevice>>(){}.getType());
	}

	public List<JsonActionGroup> getActionGroups() {
		Log.i(TAG, "Fetching all action groups.");
		return get(getServerUrl() + "/actiongroups", new TypeToken<List<JsonActionGroup>>(){}.getType());
	}

	public void triggerActionGroup(int id) {
		Log.i(TAG, "Triggering action group " + id + ".");
		get(getServerUrl() + "/actiongroups/" + id + "/trigger", null);
	}

	public LatLng getServerLocation() {
		Log.i(TAG, "Fetching server location.");
		JsonServer server = get(getServerUrl() + "/server", JsonServer.class);
		return new LatLng(server.locationLatitude, server.locationLongitude);
	}

	public void registerClient(String id) {
		Log.i(TAG, "Registering client " + id + ".");
		post(getServerUrl() + "/clients", new JsonClient(id));
	}

	public void unregisterClient(String id) {
		Log.i(TAG, "Unregistering client " + id + ".");
		delete(getServerUrl() + "/clients/" + id);
	}

	public void setChannelValueForDevice(int deviceId, int channelId, int value) {
		Log.i(TAG, "Setting channel " + channelId + " for device " + deviceId + " to " + value + ".");
		post(getServerUrl() + "/devices/" + deviceId + "/channels/" + channelId, new JsonChannelValue(value));
	}

	private void post(String url, Object data) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type", "application/json; charset=utf-8");

		try {
			httpPost.setEntity(new StringEntity(GSON.toJson(data)));
			HttpResponse response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			switch (statusCode) {
				case 200:
				case 201:
					finish(response);
					return;
				default:
					finish(response);
					throw new RuntimeException("Unhandled response code " + statusCode + ".");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private <T> T get(String url, Type type) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);

		try {
			HttpResponse response = httpClient.execute(httpGet);

			int statusCode = response.getStatusLine().getStatusCode();
			switch (statusCode) {
				case 200:
					if (type == null) {
						finish(response);
						return null;
					} else {
						String json = EntityUtils.toString(response.getEntity());
						return GSON.fromJson(json, type);
					}
				default:
					finish(response);
					throw new RuntimeException("Unhandled response code " + statusCode + ".");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void delete(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpDelete httpDelete = new HttpDelete(url);

		try {
			HttpResponse response = httpClient.execute(httpDelete);

			int statusCode = response.getStatusLine().getStatusCode();
			switch (statusCode) {
				case 200:
				case 201:
					finish(response);
					return;
				default:
					finish(response);
					throw new RuntimeException("Unhandled response code " + statusCode + ".");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void finish(HttpResponse response) throws IOException {
		if (response.getEntity() == null) return;
		// consumeContent() will be renamed to finish() in next major, as it actually releases all resources
		// and allows the underlying http connection to be reused.
		// http://developer.android.com/reference/org/apache/http/HttpEntity.html#consumeContent()
		response.getEntity().consumeContent();
	}

	private static class JsonServer {
		public double locationLatitude;
		public double locationLongitude;
		public String name;
	}

	public static enum DeviceType {
		@SerializedName("switch")
		SWITCH,

		@SerializedName("switchmeter")
		SWITCH_METER,

		@SerializedName("dimmer")
		DIMMER,

		@SerializedName("thermometer")
		THERMOMETER
	}

	public static class JsonDevice {
		public DeviceType type;
		public int deviceId;
		public String name;
		public String description;
		public String added;
		public boolean active;
		public List<JsonDeviceChannel> channels;
	}

	public static class JsonDeviceChannel {
		public int id;
		public String name;
		public Number lastValue;
	}

	public static class JsonActionGroup {
		public int actionGroupId;
		public String name;
	}

	private static class JsonClient {
		public String id;

		public JsonClient(String id) {
			this.id = id;
		}
	}

	private static class JsonChannelValue {
		public int value;

		public JsonChannelValue(int value) {
			this.value = value;
		}
	}
}
