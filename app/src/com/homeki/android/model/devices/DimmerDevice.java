package com.homeki.android.model.devices;


public class DimmerDevice extends Device {
	public DimmerDevice(DeviceType type, int id, String name, String description, boolean active) {
		super(type, id, name, description, active);
	}

	public boolean getValue() {
		return "1".equals(getChannelValue("Switch"));
	}
	
	public void setValue(boolean value) {
		setChannelValue("Switch", value ? "1" : "0");
	}

	public int getLevel() {
		return Integer.parseInt(getChannelValue("Level"));
	}
	
	public void setLevel(int level) {
		setChannelValue("Level", String.valueOf(level));
	}
}
