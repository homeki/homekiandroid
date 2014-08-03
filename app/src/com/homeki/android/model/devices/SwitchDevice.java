package com.homeki.android.model.devices;

public class SwitchDevice extends Device {
	public SwitchDevice(DeviceType type, int id, String name, String description, boolean active) {
		super(type, id, name, description, active);
	}

	public boolean getValue() {
		return "1".equals(getChannelValue("Switch"));
	}

	public void setValue(boolean isChecked) {
		setChannelValue("Switch", isChecked ? "1" : "0");
	}
}
