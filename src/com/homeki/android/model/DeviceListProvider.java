package com.homeki.android.model;

import com.homeki.android.model.devices.AbstractDevice;

public interface DeviceListProvider {
	public int getDeviceCount();
	public AbstractDevice getDeviceAtPosition(int position);
	public void addOnDeviceListChangedListener(OnDeviceListChangedListener listener);
	public void removeOnDeviceListChangedListener(OnDeviceListChangedListener listener);

	public interface OnDeviceListChangedListener {
		public void onDeviceListChanged();
	}
}
