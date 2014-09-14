package com.homeki.android.view;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.homeki.android.server.ApiClient;

import java.util.NoSuchElementException;

public abstract class DeviceListItem extends LinearLayout {
	private static final String TAG = DeviceListItem.class.getSimpleName();

	protected TextView nameView;

	private final Context context;
	private final ApiClient apiClient;
	private final ApiClient.JsonDevice jsonDevice;

	public DeviceListItem(Context context, ApiClient apiClient, ApiClient.JsonDevice jsonDevice) {
		super(context);
		this.context = context;
		this.apiClient = apiClient;
		this.jsonDevice = jsonDevice;
		inflate((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		nameView.setText(jsonDevice.name);
		updateView();
	}

	protected abstract void inflate(LayoutInflater inflater);
	protected abstract void updateView();

	protected void setChannelValue(String name, final int value) {
		final int channelId = getChannel(name).id;

		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					apiClient.setChannelValueForDevice(jsonDevice.deviceId, channelId, value);
					return true;
				} catch (Exception e) {
					Log.e(TAG, "Failed to set channel value.", e);
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean succeeded) {
				if (succeeded) return;
				Toast.makeText(context, "Failed to set value for " + jsonDevice.name + ".", Toast.LENGTH_SHORT).show();
			}
		}.execute();
	}

	protected Number getChannelValue(String name) {
		return getChannel(name).lastValue;
	}

	private ApiClient.JsonDeviceChannel getChannel(String name) {
		for (ApiClient.JsonDeviceChannel channel : jsonDevice.channels) {
			if (name.equals(channel.name)) return channel;
		}

		throw new NoSuchElementException("Found no channel called " + name + " on device " + jsonDevice.name + ".");
	}
}
