package com.homeki.android.server;

import com.homeki.android.model.devices.Device;

import java.util.List;

public interface ActionPerformer {
	public void requestDeviceList(OnDeviceListReceivedListener listener);
	public void setChannelValueForDevice(int deviceId, int channelId, int value, OnChannelValueSetListener listener);

	public interface OnDeviceListReceivedListener {
		void onDeviceListReceived(List<Device> devices);
	}

	public interface OnChannelValueSetListener {
		void result(boolean success);
	}
}
