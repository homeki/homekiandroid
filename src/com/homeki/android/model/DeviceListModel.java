package com.homeki.android.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.model.devices.ThermometerDevice;
import com.homeki.android.model.devices.AbstractDevice.DeviceOwner;
import com.homeki.android.model.devices.DeviceType;

public class DeviceListModel implements DeviceListProvider, DeviceOwner {
	private static String TAG = DeviceListModel.class.getSimpleName();

	private static DeviceListModel mInstance;

	public static DeviceListModel getModel(Context context) {
		if (mInstance == null) {
			mInstance = new DeviceListModel(context);
		}
		return mInstance;
	}

	private ArrayList<AbstractDevice> mDevices;
	private List<OnDeviceListChangedListener> mChangedListeners;
	private Context mContext;

	private DeviceListModel(Context context) {
		mDevices = new ArrayList<AbstractDevice>();
		mContext = context;
		mChangedListeners = new ArrayList<OnDeviceListChangedListener>();
	}

	@Override
	public int getDeviceCount() {
		return mDevices.size();
	}

	@Override
	public AbstractDevice getDeviceAtPosition(int position) {
		return mDevices.get(position);
	}

	public AbstractDevice getDeviceWithId(int id) {
		for (AbstractDevice device : mDevices) {
			if (device.getId() == id) {
				return device;
			}
		}
		return null;
	}

	public void setDeviceList(List<AbstractDevice> devices) {
		Log.d(TAG, "onDeviceListReceived()");
		mDevices.clear();

		HashMap<DeviceType, Integer> typeCountMap = new HashMap<DeviceType, Integer>();
		for (AbstractDevice device : devices) {
			device.setOwner(this);
			if (device.getName() == null || device.getName().isEmpty()) {
				device.setName(getDeviceName(typeCountMap, device));
			}
		}
		mDevices.addAll(devices);

		ThermometerDevice temp = new ThermometerDevice(DeviceType.THERMOMETER, 10000, "THERMO", "This is a thermometer", "20120101", true);
		temp.setChannelValue(0, "22");
		
		mDevices.add(temp);
		notifyListeners();
	}

	private void notifyListeners() {
		if (mChangedListeners != null) {
			for (OnDeviceListChangedListener listener : mChangedListeners) {
				listener.onDeviceListChanged();
			}
		}
	}

	private String getDeviceName(HashMap<DeviceType, Integer> typeCountMap, AbstractDevice device) {
		int number = 0;
		if (!typeCountMap.containsKey(device.getType())) {
			number = 1;
		} else {
			number = typeCountMap.get(device.getType()) + 1;
		}
		typeCountMap.put(device.getType(), number);

		return getTypeName(device) + " " + number;
	}

	private String getTypeName(AbstractDevice device) {
		String name = mContext.getPackageName() + ":string/device_type_name_" + device.getType().toString();
		int id = mContext.getResources().getIdentifier(name, null, null);

		return mContext.getString(id);
	}

	@Override
	public void addOnDeviceListChangedListener(OnDeviceListChangedListener listener) {
		mChangedListeners.add(listener);
	}
	
	@Override
	public void removeOnDeviceListChangedListener(OnDeviceListChangedListener listener) {
		mChangedListeners.remove(listener);
	}

	@Override
	public void deviceDidChange(AbstractDevice device) {
		notifyListeners();
	}
}
