package com.homeki.android.model;

import android.content.Context;
import android.util.Log;
import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.model.devices.AbstractDevice.DeviceOwner;
import com.homeki.android.model.devices.DeviceType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeviceListModel implements DeviceListProvider, DeviceOwner {
	private static String TAG = DeviceListModel.class.getSimpleName();

	private static DeviceListModel instance;

	public static DeviceListModel getModel(Context context) {
		if (instance == null) instance = new DeviceListModel(context);
		return instance;
	}

	private ArrayList<AbstractDevice> devices;
	private List<OnDeviceListChangedListener> changedListeners;
	private Context context;

	private DeviceListModel(Context context) {
		this.devices = new ArrayList<AbstractDevice>();
		this.context = context;
		this.changedListeners = new ArrayList<OnDeviceListChangedListener>();
	}

	@Override
	public int getDeviceCount() {
		return devices.size();
	}

	@Override
	public AbstractDevice getDeviceAtPosition(int position) {
		return devices.get(position);
	}

	public AbstractDevice getDeviceWithId(int id) {
		for (AbstractDevice device : devices) {
			if (device.getId() == id) {
				return device;
			}
		}
		return null;
	}

	public void setDeviceList(List<AbstractDevice> devices) {
		Log.d(TAG, "onDeviceListReceived()");
		this.devices.clear();

		HashMap<DeviceType, Integer> typeCountMap = new HashMap<DeviceType, Integer>();
		for (AbstractDevice device : devices) {
			device.setOwner(this);
			if (device.getName() == null || device.getName().isEmpty()) {
				device.setName(getDeviceName(typeCountMap, device));
			}
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

	private String getDeviceName(HashMap<DeviceType, Integer> typeCountMap, AbstractDevice device) {
		int number;
		if (!typeCountMap.containsKey(device.getType())) {
			number = 1;
		} else {
			number = typeCountMap.get(device.getType()) + 1;
		}
		typeCountMap.put(device.getType(), number);

		return getTypeName(device) + " " + number;
	}

	private String getTypeName(AbstractDevice device) {
		String name = context.getPackageName() + ":string/device_type_name_" + device.getType().toString();
		int id = context.getResources().getIdentifier(name, null, null);

		return context.getString(id);
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
	public void deviceDidChange(AbstractDevice device) {
		notifyListeners();
	}
}
