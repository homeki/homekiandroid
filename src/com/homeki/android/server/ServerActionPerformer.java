package com.homeki.android.server;

import java.util.Date;
import java.util.List;

import com.homeki.android.model.DataPoint;
import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.server.RestClient.Errors;
import com.homeki.android.server.RestClient.OnErrorListener;
import com.homeki.android.settings.Settings;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ServerActionPerformer implements ActionPerformer {
	private static String TAG = ServerActionPerformer.class.getSimpleName();

	private RestClient mClient;
	private Context mContext;

	public ServerActionPerformer(Context context) {
		mContext = context;
		mClient = new RestClient(mContext);
	}

	public void requestDeviceList(final OnDeviceListReceivedListener listener) {
		Log.d(TAG, "requestDeviceList()");
		new AsyncTask<Object, Integer, List<AbstractDevice>>() {
			@Override
			protected List<AbstractDevice> doInBackground(Object... params) {
				Log.d(TAG, "requestDeviceList().doInBackground()");

				List<AbstractDevice> allDevices = mClient.getAllDevices();

				if (allDevices == null || allDevices.isEmpty()) {
					String serverPath = ServerLocator.locateServerOnWifi();
					if (null != serverPath && !serverPath.isEmpty()) {
						Settings.setServerUrl(mContext, serverPath);
					}
					allDevices = mClient.getAllDevices();
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
	public void setChannelValueForDevice(final int deviceId, final int channelId, final String value, final OnChannelValueSetListener listener) {
		Log.d(TAG, "setChannelValueForDevice()");
		new AsyncTask<Object, Integer, Boolean>() {
			@Override
			protected Boolean doInBackground(Object... params) {
				Log.d(TAG, "setChannelValueForDevice().doInBackground()");

				return mClient.setChannelValueForDevice(deviceId, channelId, value);
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

				return mClient.getChannelHistoryForDevice(deviceId, channelId, start, end);
			}

			@Override
			protected void onPostExecute(List<DataPoint> result) {
				super.onPostExecute(result);
				Log.d(TAG, "getChannelHistoryForDevice().onPostExecute()");
				Log.d(TAG, "Result: " + result);

				if (listener != null) {
					listener.onChannelHistoryReceived(result);
				}
			}
		}.execute(0);
	}
}
