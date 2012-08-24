package com.homeki.android.model.devices;

import java.util.Date;


public class SwitchDevice extends AbstractDevice {
	
	public static final int CHANNEL_ID_VALUE = 0;
	
	public SwitchDevice(DeviceTypes type, int id, String name, String description, String added, boolean active) {
		mType = type;
		mId = id;
		mName = name;
		mDescription = description;
		mAdded = added;
		mActive = active;
	}
}
