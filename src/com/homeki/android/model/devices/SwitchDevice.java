package com.homeki.android.model.devices;

public class SwitchDevice extends AbstractDevice {

	public static final int CHANNEL_ID_VALUE = 0;

	public SwitchDevice(DeviceTypes type, int id, String name, String description, String added, boolean active) {
		super(type,id,name,description,added,active);
	}

	public boolean getValue() {
		return "1".equals(getChannelValue(CHANNEL_ID_VALUE));
	}

	public void setValue(boolean isChecked) {
		setChannelValue(CHANNEL_ID_VALUE, isChecked ? "1" : "0");
	}
}
