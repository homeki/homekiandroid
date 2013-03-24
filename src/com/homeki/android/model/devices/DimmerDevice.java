package com.homeki.android.model.devices;


public class DimmerDevice extends AbstractDevice {

	public static final int CHANNEL_ID_VALUE = 0;
	public static final int CHANNEL_ID_LEVEL = 1;

	public DimmerDevice(DeviceType type, int id, String name, String description, String added, boolean active) {
		super(type, id, name, description, added, active);
	}

	public boolean getValue() {
		return "1".equals(getChannelValue(CHANNEL_ID_VALUE));
	}
	
	public void setValue(boolean value) {
		setChannelValue(CHANNEL_ID_VALUE, value ? "1" : "0");
	}

	public int getLevel() {
		return Integer.parseInt(getChannelValue(CHANNEL_ID_LEVEL));
	}
	
	public void setLevel(int level) {
		setChannelValue(CHANNEL_ID_LEVEL, String.valueOf(level));
	}
}
