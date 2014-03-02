package com.homeki.android.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.homeki.android.model.DataPoint;
import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.settings.Settings;

import java.util.Date;
import java.util.List;

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
		new AsyncTask<Object, Integer, List<AbstractDevice>>() {
			@Override
			protected List<AbstractDevice> doInBackground(Object... params) {
				Log.d(TAG, "requestDeviceList().doInBackground()");

				List<AbstractDevice> allDevices = client.getAllDevices();

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
			protected void onPostExecute(List<AbstractDevice> result) {
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

	@Override
	public void getChannelHistoryForDevice(final int deviceId, final int channelId, final Date start, final Date end, final OnChannelHistoryReceivedListener listener) {
		Log.d(TAG, "getChannelHistoryForDevice()");
		new AsyncTask<Object, Integer, List<DataPoint>>() {
			@Override
			protected List<DataPoint> doInBackground(Object... params) {
				Log.d(TAG, "getChannelHistoryForDevice().doInBackground()");

				return client.getChannelHistoryForDevice(deviceId, channelId, start, end);
			}

			@Override
			protected void onPostExecute(List<DataPoint> result) {
				super.onPostExecute(result);
				Log.d(TAG, "getChannelHistoryForDevice().onPostExecute()");
				Log.d(TAG, "Result: " + result);

				if (listener != null) {
					listener.onChannelHistoryReceived(deviceId, channelId, result);
				}
			}
		}.execute(0);
	}
}
