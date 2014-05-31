package com.homeki.android.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.homeki.android.model.devices.Device;
import com.homeki.android.model.devices.Device.DeviceOwner;

public class DeviceListModel implements DeviceListProvider, DeviceOwner {
	private static String TAG = DeviceListModel.class.getSimpleName();

	private static DeviceListModel instance;

	public static DeviceListModel getModel(Context context) {
		if (instance == null) instance = new DeviceListModel(context);
		return instance;
	}

	private ArrayList<Device> devices;
	private List<OnDeviceListChangedListener> changedListeners;
	private Context context;

	private DeviceListModel(Context context) {
		this.devices = new ArrayList<Device>();
		this.context = context;
		this.changedListeners = new ArrayList<OnDeviceListChangedListener>();
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
