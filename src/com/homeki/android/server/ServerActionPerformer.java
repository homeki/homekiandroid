package com.homeki.android.server;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.homeki.android.model.devices.Device;
import com.homeki.android.settings.Settings;

public class ServerActionPerformer implements ActionPerformer {
	private static String TAG = ServerActionPerformer.class.getSimpleName();

	private RestClient client;
	private Context context;

	public ServerActionPerformer(Context context) {
		this.context = context;
		this.client = new RestClient(context);
	}

	public void requestDeviceList(final OnDeviceListReceivedListener listener) {
		Log.d(TAG, "requestDeviceList()");
		new AsyncTask<Object, Integer, List<Device>>() {
			@Override
			protected List<Device> doInBackground(Object... params) {
				Log.d(TAG, "requestDeviceList().doInBackground()");

				List<Device> allDevices = client.getAllDevices();

				if (allDevices == null || allDevices.isEmpty()) {
					String serverPath = ServerLocator.locateServerOnWifi();
					if (null != serverPath && !serverPath.isEmpty()) {
						Settings.setServerUrl(context, serverPath);
					}
					allDevices = client.getAllDevices();
				}
								
				return allDevices;
			}

			@Override
			protected void onPostExecute(List<Device> result) {
				super.onPostExecute(result);
				Log.d(TAG, "requestDeviceList().onPostExecute()");
				if (listener != null) {
					listener.onDeviceListReceived(result);
				}
			}
		}.execute(0);
	}

	@Override
	public void setChannelValueForDevice(final int deviceId, final int channelId, final int value, final OnChannelValueSetListener listener) {
		Log.d(TAG, "setChannelValueForDevice()");
		new AsyncTask<Object, Integer, Boolean>() {
			@Override
			protected Boolean doInBackground(Object... params) {
				Log.d(TAG, "setChannelValueForDevice().doInBackground()");

				return client.setChannelValueForDevice(deviceId, channelId, value);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				Log.d(TAG, "setChannelValueForDevice().onPostExecute()");
				Log.d(TAG, "Result: " + result);

				if (listener != null) {
					listener.result(result);
				}
			}
		}.execute(0);
	}
}
