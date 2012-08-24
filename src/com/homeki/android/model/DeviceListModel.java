package com.homeki.android.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.Log;

import com.homeki.android.model.devices.AbstractDevice;
import com.homeki.android.server.ActionPerformer.OnDeviceListReceivedListener;

public class DeviceListModel implements DeviceListProvider {
	private static String TAG = DeviceListModel.class.getSimpleName();

	private static DeviceListModel mInstance;

	public static DeviceListModel getModel() {
		if (mInstance == null) {
			mInstance = new DeviceListModel();
		}
		return mInstance;
	}

	private ArrayList<AbstractDevice> mDevices;
	private OnDeviceListChangedListener mChangedListener;

	private DeviceListModel() {
		mDevices = new ArrayList<AbstractDevice>();
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
			if(device.getId() == id) {
				return device;
			}
		}
		return null;
	}

	public void setDeviceList(List<AbstractDevice> devices) {
		Log.d(TAG, "onDeviceListReceived()");
		mDevices.clear();
		mDevices.addAll(devices);
		
		if(mChangedListener != null) {
			mChangedListener.onDeviceListChanged();
		}
	}

	@Override
	public void setOnDeviceListChangedListener(OnDeviceListChangedListener listener) {
		mChangedListener = listener;		
	}
}
