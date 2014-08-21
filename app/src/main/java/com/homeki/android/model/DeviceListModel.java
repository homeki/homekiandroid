package com.homeki.android.model;

import android.util.Log;
import com.homeki.android.model.devices.Device;
import com.homeki.android.model.devices.Device.DeviceOwner;

import java.util.ArrayList;
import java.util.List;

public class DeviceListModel implements DeviceListProvider, DeviceOwner {
	private static String TAG = DeviceListModel.class.getSimpleName();

	private static DeviceListModel instance;

	public static DeviceListModel getModel() {
		if (instance == null) instance = new DeviceListModel();
		return instance;
	}

	private ArrayList<Device> devices;
	private List<OnDeviceListChangedListener> changedListeners;

	private DeviceListModel() {
		this.devices = new ArrayList<>();
		this.changedListeners = new ArrayList<>();
	}

	@Override
	public int getDeviceCount() {
		return devices.size();
	}

	@Override
	public Device getDeviceAtPosition(int position) {
		return devices.get(position);
	}

	public Device getDeviceWithId(int id) {
		for (Device device : devices) {
			if (device.getId() == id) {
				return device;
			}
		}
		return null;
	}

	public void setDeviceList(List<Device> devices) {
		Log.d(TAG, "onDeviceListReceived()");
		this.devices.clear();

		for (Device device : devices) {
			device.setOwner(this);
		}

		this.devices.addAll(devices);

		notifyListeners();
	}

	private void notifyListeners() {
		if (changedListeners != null) {
			for (OnDeviceListChangedListener listener : changedListeners) {
				listener.onDeviceListChanged();
			}
		}
	}

	@Override
	public void addOnDeviceListChangedListener(OnDeviceListChangedListener listener) {
		changedListeners.add(listener);
	}
	
	@Override
	public void removeOnDeviceListChangedListener(OnDeviceListChangedListener listener) {
		changedListeners.remove(listener);
	}

	@Override
	public void deviceDidChange(Device device) {
		notifyListeners();
	}
}
