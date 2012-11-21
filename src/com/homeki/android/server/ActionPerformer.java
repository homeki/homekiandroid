package com.homeki.android.server;

import java.util.Date;
import java.util.List;

import com.homeki.android.model.DataPoint;
import com.homeki.android.model.devices.AbstractDevice;

public interface ActionPerformer {

	public void requestDeviceList(OnDeviceListReceivedListener listener);

	public void setChannelValueForDevice(int deviceId, int channelId, String value, OnChannelValueSetListener listener);

	public void getChannelHistoryForDevice(int deviceId, int channelId, Date start, Date end, OnChannelHistoryReceivedListener listener);

	public interface OnDeviceListReceivedListener {
		void onDeviceListReceived(List<AbstractDevice> devices);
	}

	public interface OnChannelValueSetListener {
		void result(boolean success);
	}
	
	public interface OnChannelHistoryReceivedListener {
		void onChannelHistoryReceived(List<DataPoint> data);
	}

}
