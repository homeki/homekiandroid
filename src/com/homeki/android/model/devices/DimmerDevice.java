package com.homeki.android.model.devices;

import java.util.Date;

public class DimmerDevice extends AbstractDevice {

	public static final int CHANNEL_ID_VALUE = 0;
	public static final int CHANNEL_ID_LEVEL = 1;	
	
	public DimmerDevice(DeviceTypes type, int id, String name, String description, String added, boolean active) {
		mType = type;
		mId = id;
		mName = name;
		mDescription = description;
		mAdded = added;
		mActive = active;
	}
	
	public int getValue() {
		return Integer.parseInt(getChannelValue(CHANNEL_ID_VALUE));
	}
	
	public int getLevel() {
		return Integer.parseInt(getChannelValue(CHANNEL_ID_LEVEL));
	}
	
}
