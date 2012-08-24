package com.homeki.android.server;

import java.util.List;

import com.homeki.android.model.devices.AbstractDevice;
import android.os.AsyncTask;
import android.util.Log;

public class ServerActionPerformer implements ActionPerformer {
	private static String TAG = ServerActionPerformer.class.getSimpleName();
	private OnDeviceListReceivedListener onDeviceListReceivedListener;

	public void requestDeviceList() {		
		Log.d(TAG, "requestDeviceList()");
		new AsyncTask<Object, Integer, List<AbstractDevice>>() {
			@Override
			protected List<AbstractDevice> doInBackground(Object... params) {
				Log.d(TAG, "requestDeviceList().doInBackground()");
				
				RestClient client = new RestClient();
				return client.getAllDevices();
			}
			
			@Override
			protected void onPostExecute(List<AbstractDevice> result) {
				super.onPostExecute(result);
				Log.d(TAG, "requestDeviceList().onPostExecute()");
				if(onDeviceListReceivedListener != null) {
					onDeviceListReceivedListener.onDeviceListReceived(result);
				}
			}
		}.execute(0);
	}

	@Override
	public void setOnDeviceListReceivedListener(OnDeviceListReceivedListener listener) {
		onDeviceListReceivedListener = listener;
	}

	@Override
	public void setChannelValueForDevice(final int deviceId, final int channelId, final String value) {
		Log.d(TAG, "setChannelValueForDevice()");
		new AsyncTask<Object, Integer, Boolean>() {
			@Override
			protected Boolean doInBackground(Object... params) {
				Log.d(TAG, "setChannelValueForDevice().doInBackground()");
				
				RestClient client = new RestClient();
				return client.setChannelValueForDevice(deviceId, channelId, value);
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				Log.d(TAG, "setChannelValueForDevice().onPostExecute()");
				Log.d(TAG, "Result: " + result);
			}
		}.execute(0);
	}
}
